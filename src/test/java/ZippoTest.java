import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class ZippoTest {

    @Test
    void Test1() {

        given()
                .when()
                .then();
    }

    @Test
    void statusTest() {
given()
        .when()
        .get("http://api.zippopotam.us/US/90210")
        .then()
        .log().body()
        .log().status()
        .statusCode(200);













    }


}

