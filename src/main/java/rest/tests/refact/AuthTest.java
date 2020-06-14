package rest.tests.refact;

import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;
import org.junit.Test;
import rest.core.BaseTest;


import static io.restassured.RestAssured.given;

public class AuthTest extends BaseTest {

    @Test
    public void naoDeveAcessarAPISemToken() {
        FilterableRequestSpecification filterableRequestSpecification = (FilterableRequestSpecification) RestAssured.requestSpecification;
        filterableRequestSpecification.removeHeader("Authorization");
        given()
        .when()
            .get("/contas")
        .then()
            .statusCode(401)
        ;
    }


}
