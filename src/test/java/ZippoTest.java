import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.baseURI;
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
    void multipleTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")
                .then()
                .log().body()
                .statusCode(200)
                .body("places", hasSize(71))
                .body("places.'place name'", hasItem("Büyükdikili Köyü"))
                .contentType(ContentType.JSON);
    }

    @Test
    void pathParameterTest() { // the parameters that are separated with / are called path parameters
        given()
                .pathParam("Country", "us")
                .pathParam("ZipCode", "90210")
                .log().uri() // prints the request url
                .when()
                .get("http://api.zippopotam.us/{Country}/{ZipCode}")
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test
    void pathParameterTest2() {
        // send a get request for zipcodes between 90210 and 90213 and verify that in all responses the size
        // of the places array is 1

        for (int i = 90210; i <= 90213; i++) {

            given()
                    .pathParam("ZipCode", i)
                    .when()
                    .get("http://api.zippopotam.us/us/{ZipCode}")
                    .then()
                    .log().body()
                    .body("places", hasSize(1)); // checks the size of the array in the body
        }
    }

    @Test
    void queryParamTest() { // If the parameter is separated by ? it is called query parameter
        given()
                .param("page", 3) // https://gorest.co.in/public/v1/users?page=3
                .pathParam("APIName", "users")
                .log().uri()
                .when()
                .get("https://gorest.co.in/public/v1/{APIName}")
                .then()
                .log().body()
                .statusCode(200)
                .body("meta.pagination.page", equalTo(3));
    }

    @Test
    void queryParamTest1() {
        // send the same request for the pages between 1-10 and check if
        // the page number we send from request and page number we get from response are the same
        for (int i = 1; i <= 10; i++) {
            given()
                    .param("page", i)
                    .pathParam("APIName", "users")
                    .log().uri()
                    .when()
                    .get("https://gorest.co.in/public/v1/{APIName}")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("meta.pagination.page", equalTo(i));
        }
    }

    @Test(dataProvider = "pageNumbers")
    void queryParamTestWithDataProvider(int page) {
        // send the same request for the pages between 1-10 and check if
        // the page number we send from request and page number we get from response are the same

        given()
                .param("page", page)
                .pathParam("APIName", "users")
                .log().uri()
                .when()
                .get("https://gorest.co.in/public/v1/{APIName}")
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("meta.pagination.page", equalTo(page));

    }

    @DataProvider
    public Object[][] pageNumbers() {

        Object[][] pageNumberList = {{1}, {2}, {3}, {4}, {5}, {6}, {7}, {8}, {9}, {10}};

        return pageNumberList;
    }

    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;

    @BeforeClass
    public void setUp() {
        baseURI = "https://gorest.co.in/public/v1"; // if the request url in the request method doesn't have http part
        // rest assured puts baseURI to the beginning of the url in the request method


        requestSpecification = new RequestSpecBuilder()
                .log(LogDetail.URI)
                .setContentType(ContentType.JSON)
                .addPathParam("APIName", "users")
                .addParam("page", 2)
                .build();
    }
}