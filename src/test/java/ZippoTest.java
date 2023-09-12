import io.restassured.http.ContentType;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasSize;

public class ZippoTest {

    @Test
    void test1() {

        given() // preparation(token, request body, parameters, cookies...)
                .when() //for url, request method(get, post, put, patch, delete)
                //get, post, put, patch, delete don't belong to postman.
                // they are known as http methods. All programming languages use these methods
                .then();// response(response body, tests, extract data from the response)
    }

    @Test
    void statusCodeTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body() // prints the response body to the console
                .log().status() // prints the status of the request
                .statusCode(200); // tests if the status code is the same as the expected
    }

    @Test
    void contentTypeTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .contentType(ContentType.JSON); // tests if the response body is in JSON format
    }

    @Test
    void checkCountryFromResponseBody() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("country", equalTo("United States"));

// Hamcrest: Lets us to test values from the response body

    }

    // Postman                                      Rest Assured
    // pm.response.json() -> body                       body()
    // pm.response.json().country                       body("country")
    // pm.response.json().places[0].'places name'       body("places[0].'place name'") //gives the place name variable of the first element of places list
    // if the variable name has spaces in it write it between ' '

    @Test
    void checkStateFromResponse() {
        // Send a request to "http://api.zippopotam.us/us/90210"
        // and check if the state is "California"
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .statusCode(200)
                .body("places[0].state", equalTo("California"));
    }

    @Test
    void checkStateAbbreviationFromResponse() {
        // Send a request to "http://api.zippopotam.us/us/90210"
        // and check if the state abbreviation is "CA"

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("places[0].'state abbreviation'", equalTo("CA"));
    }

    @Test
    void bodyArrayHasItem() {
        // Send a request to "http://api.zippopotam.us/tr/01000"
        // and check if the body has "Büyükdikili Köyü"

        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")
                .then()
                .log().body()
                .body("places.'place name'", hasItem("Büyükdikili Köyü"));
        // When we don't use index it gets all place names from the response and creates an array with them.
        // hasItem checks if that array contains "Büyükdikili Köyü" value in it
    }

    @Test
    void arraySizeTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("places", hasSize(1)); // checks the size of the array in the body
    }

    @Test
    void arraySizeTest2() {
        // Send a request to "http://api.zippopotam.us/tr/01000"
        // and check if the place name array's size is 71

        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")
                .then()
                .log().body()
                .body("places.'place name'", hasSize(71));
    }
    @Test
    void multipleTest(){
        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")
                .then()
                .log().body()
                .statusCode(200)
                .body("places",hasSize(71))
                .body("places.'place name'", hasItem("Büyükdikili Köyü"))
                .contentType(ContentType.JSON);
    }

    @Test
    void pathParameterTest(){ // the parameters that are separated with / are called path parameters
        given()
                .pathParam("Country", "us")
                .pathParam("ZipCode","90210")
                .log().uri() // prints the request url
                .when()
                .get("http://api.zippopotam.us/{Country}/{ZipCode}")
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test
    void pathParameterTest2(){
        // send a get request for zipcodes between 90210 and 90213 and verify that in all responses the size
        // of the places array is 1

        for (int i=90210; i<=90213; i++){

            given()
                    .pathParam("ZipCode",i)
                    .when()
                    .get("http://api.zippopotam.us/us/{ZipCode}")
                    .then()
                    .log().body()
                    .body("places",hasSize(1)); // checks the size of the array in the body
        }
    }
}