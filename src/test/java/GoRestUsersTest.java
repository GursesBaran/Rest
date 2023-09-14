import POJOClasses.User;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class GoRestUsersTest {

    public String createRandomName() {
        return RandomStringUtils.randomAlphabetic(8);
    }

    public String createRandomEmail() {
        return RandomStringUtils.randomAlphabetic(10) + "@techno.com";
    }

    @BeforeClass
    public void setUp() {

        baseURI = "https://gorest.co.in/public/v2/users";

    }


    @Test
    void createNewUser() {
        given()
                .header("Authorization", "Bearer 07a0637daa1168c03f668358e917ecafd9ee10fbc7b0df4899707e3476af87fe")
                .contentType(ContentType.JSON)
                .body("{\"name\":\"" + createRandomName() + "\",\"gender\":\"male\",\"email\":\"" + createRandomEmail() + "\",\"status\":\"active\"}")
                .when()
                .post()
                .then()
                .log().body()
                .statusCode(201)
                .contentType(ContentType.JSON);
    }

    @Test
    void createNewUserWithMaps() {
        Map<String, String> user = new HashMap<>();
        user.put("name", createRandomName());
        user.put("gender", "female");
        user.put("email", createRandomEmail());
        user.put("status", "active");

        given()
                .header("Authorization", "Bearer 07a0637daa1168c03f668358e917ecafd9ee10fbc7b0df4899707e3476af87fe")
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post()
                .then()
                .log().body()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("email", equalTo(user.get("email")));
    }

    @Test
    void createNewUserWithObject() {
        User user = new User();
        user.setName(createRandomName());
        user.setEmail(createRandomEmail());
        user.setGender("female");
        user.setStatus("active");

        given()
                .header("Authorization", "Bearer 07a0637daa1168c03f668358e917ecafd9ee10fbc7b0df4899707e3476af87fe")
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post()
                .then()
                .log().body()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("email", equalTo(user.getEmail()));
    }
    //TODO
    /** Write create user negative test**/
}













