package rest.tests.refact;

import org.junit.Test;
import rest.core.BaseTest;
import rest.tests.Movimentacao;
import rest.tests.utils.BarrigaUtils;
import rest.tests.utils.DataUtils;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class MovimentacaoTest extends BaseTest {

    @Test
    public void deveInserirMovimentacaoSucesso() {
        Movimentacao movimentacao = getMovimentacaoValida();

        given()
            .body(movimentacao)
        .when()
            .post("/transacoes")
        .then()
            .statusCode(201)
        ;
    }

    @Test
    public void deveValidarCamposObrigatoriosMovimentacao() {
        given()
            .body("{}")
        .when()
            .post("/transacoes")
        .then()
            .statusCode(400)
            .body("$", hasSize(8))
            .body("msg", hasItems(
                "Data da Movimentação é obrigatório",
                "Data do pagamento é obrigatório",
                "Descrição é obrigatório",
                "Interessado é obrigatório",
                "Valor é obrigatório",
                "Valor deve ser um número",
                "Conta é obrigatório",
                "Situação é obrigatório"
        ))
        ;
    }

    @Test
    public void naoDeveInserirMovimentacaoDataFutura() {
        Movimentacao movimentacao = getMovimentacaoValida();
        movimentacao.setData_transacao(DataUtils.getDataDiferencaDias(5));

        given()
            .body(movimentacao)
        .when()
            .post("/transacoes")
        .then()
            .statusCode(400)
            .body("$", hasSize(1))
            .body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual"))
        ;
    }

    @Test
    public void naoDeveRemoverContaComMovimentacao() {
        Integer CONTA_ID = BarrigaUtils.getIdContaPeloNome("Conta com movimentacao");

        given()
            .pathParam("id", CONTA_ID)
        .when()
            .delete("/contas/{id}")
        .then()
            .statusCode(500)
            .body("constraint", is("transacoes_conta_id_foreign"))
        ;
    }

    @Test
    public void deveRemoverMovimentacao() {
        Integer MOVIMENTACAO_ID = BarrigaUtils.getIdMovimentacaoPelaDescricao("Movimentacao para exclusao");

        given()
            .pathParam("id", MOVIMENTACAO_ID)
        .when()
            .delete("/transacoes/{id}")
        .then()
            .statusCode(204)
        ;
    }

    private Movimentacao getMovimentacaoValida() {
        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setConta_id(BarrigaUtils.getIdContaPeloNome("Conta para movimentacoes"));
        movimentacao.setDescricao("Descricao da movimentacao");
        movimentacao.setEnvolvido("Envolvido da movimentacao");
        movimentacao.setTipo("REC");
        movimentacao.setData_transacao(DataUtils.getDataDiferencaDias(-1));
        movimentacao.setData_pagamento(DataUtils.getDataDiferencaDias(5));
        movimentacao.setValor(100f);
        movimentacao.setStatus(true);
        return movimentacao;
    }

}
