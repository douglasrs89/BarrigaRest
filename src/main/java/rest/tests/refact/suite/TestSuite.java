package rest.tests.refact.suite;

import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import rest.core.BaseTest;
import rest.tests.refact.AuthTest;
import rest.tests.refact.ContasTest;
import rest.tests.refact.MovimentacaoTest;
import rest.tests.refact.SaldoTest;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@RunWith(Suite.class)
@SuiteClasses({
        ContasTest.class,
        MovimentacaoTest.class,
        SaldoTest.class,
        AuthTest.class
})
public class TestSuite extends BaseTest {

    @BeforeClass
    public static void login() {
        Map<String, String> login = new HashMap<String, String>();
        login.put("email", "douglasrs89@gmail.com");
        login.put("senha", "123456");

        String TOKEN =
        given()
            .body(login)
        .when()
            .post("/signin")
        .then()
            .statusCode(200)
            .extract().path("token")
        ;

        RestAssured.requestSpecification.header("Authorization", "JWT " + TOKEN);
        RestAssured.get("/reset").then().statusCode(200);

    }

}
