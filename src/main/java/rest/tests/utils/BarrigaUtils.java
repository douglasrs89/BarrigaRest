package rest.tests.utils;

import io.restassured.RestAssured;

public class BarrigaUtils {

    public static Integer getIdContaPeloNome(String nome) {
        return RestAssured.get("/contas?nome=" + nome).then().extract().path("id[0]");
    }

    public static Integer getIdMovimentacaoPelaDescricao(String descricao) {
        return RestAssured.get("/transacoes?descricao=" + descricao).then().extract().path("id[0]");
    }

}
