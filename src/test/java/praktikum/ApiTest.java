package praktikum;

import com.github.javafaker.Faker;
import com.github.javafaker.Internet;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import org.apache.http.params.CoreConnectionPNames;
import org.junit.Before;
import praktikum.model.*;

import java.util.List;
import java.util.Locale;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;

@SuppressWarnings("FieldMayBeFinal")
@Epic("QA Java Diploma 2")
@Feature("Stellar Burger API")
public class ApiTest {

    protected Faker faker;
    protected RestAssuredConfig config;

    protected String email;
    protected String password;
    protected String name;

    protected String accessToken;

    @Before
    public void setup() {
        System.out.println("setup()");
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api";
        RestAssured.port = 443;
        config = RestAssured.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam(CoreConnectionPNames.SO_TIMEOUT, 10_000));

        faker = new Faker(Locale.ENGLISH);
    }

    protected void generateUserData() {
        Internet internet = faker.internet();
        email = internet.emailAddress();
        password = internet.password();
        name = faker.name().name();
    }

    protected CreateUserResponseVO createUser(
            String email,
            String password,
            String name) {
        return createUser(email, password, name, true, SC_OK);
    }

    protected CreateUserResponseVO createUser(
            String email,
            String password,
            String name,
            boolean expectedSuccess,
            int expectedCode) {
        CreateUserResponseVO response = given()
                .body(CreateUserRequestVO.builder()
                        .email(email)
                        .password(password)
                        .name(name)
                        .build())
                .contentType(JSON)
                .log().method().log().uri().log().body()
                .when()
                .post("/auth/register")
                .then()
                .log().status().log().body()
                .statusCode(expectedCode)
                .body("success", equalTo(expectedSuccess))
                .extract().as(CreateUserResponseVO.class);
        if (response.getAccessToken() != null) {
            accessToken = response.getAccessToken();
        }
        return response;
    }

    protected void deleteUser() {
        deleteUser(this.accessToken);
        accessToken = null;
    }

    protected void deleteUser(String accessToken) {
        if (accessToken == null) {
            System.out.println("user delete not needed");
            return;
        }
        given()
                .contentType(JSON)
                .log().method().log().uri().log().body().log().headers()
                .when()
                .header("Authorization", accessToken)
                .delete("/auth/user")
                .then()
                .log().status().log().body()
                .statusCode(SC_ACCEPTED)
                .body("success", equalTo(true),
                        "message", equalTo("User successfully removed"));
    }

    protected LoginResponseVO login(String email, String password) {
        return login(email, password, true, SC_OK);
    }

    protected LoginResponseVO login(
            String email,
            String password,
            boolean expectedSuccess,
            int expectedCode) {
        LoginResponseVO response = given()
                .contentType(JSON)
                .log().method().log().uri().log().body().log().headers()
                .when()
                .body(LoginRequestVO.builder()
                        .email(email)
                        .password(password)
                        .build())
                .post("/auth/login")
                .then()
                .log().status().log().body()
                .statusCode(expectedCode)
                .body("success", equalTo(expectedSuccess))
                .extract().as(LoginResponseVO.class);
        return response;
    }

    protected GetUserResponseVO getUser(
            String accessToken,
            Object body) {
        return getUser(accessToken, body, true, SC_OK);
    }

    protected GetUserResponseVO getUser(
            String accessToken,
            Object body,
            boolean expectedSuccess,
            int expectedCode) {
        GetUserResponseVO response = given()
                .contentType(JSON)
                .log().method().log().uri().log().body()
                .when()
                .header("Authorization", accessToken)
                .get("/auth/user")
                .then()
                .log().status().log().body()
                .statusCode(expectedCode)
                .body("success", equalTo(expectedSuccess))
                .extract().as(GetUserResponseVO.class);
        return response;
    }

    protected UpdateUserResponseVO updateUser(
            String accessToken,
            Object body) {
        return updateUser(accessToken, body, true, SC_OK);
    }

    protected UpdateUserResponseVO updateUser(
            String accessToken,
            Object body,
            boolean expectedSuccess,
            int expectedCode) {
        UpdateUserResponseVO response = given()
                .body(body)
                .contentType(JSON)
                .log().method().log().uri().log().body()
                .when()
                .header("Authorization", accessToken)
                .patch("/auth/user")
                .then()
                .log().status().log().body()
                .statusCode(expectedCode)
                .body("success", equalTo(expectedSuccess))
                .extract().as(UpdateUserResponseVO.class);
        return response;
    }

    protected IngredientsResponseVO getIngredients() {
        return given()
                .contentType(JSON)
                .log().method().log().uri().log().body()
                .when()
                .get("/ingredients")
                .then()
                .log().status()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .extract().as(IngredientsResponseVO.class);
    }

    protected CreateOrderResponseVO createOrder(String accessToken, List<String> ingredientIds) {
        return createOrder(accessToken, ingredientIds, true, SC_OK);
    }

    protected CreateOrderResponseVO createOrder(
            String accessToken,
            List<String> ingredientIds,
            boolean expectedSuccess,
            int expectedCode) {
        return given()
                .contentType(JSON)
                .log().method().log().uri().log().headers().log().body()
                .when()
                .header("Authorization", accessToken)
                .body(new Object() {
                    public List<?> ingredients = ingredientIds;
                })
                .post("/orders")
                .then()
                .log().status().log().body()
                .statusCode(expectedCode)
                .body("success", equalTo(expectedSuccess))
                .extract().as(CreateOrderResponseVO.class);
    }

    protected void createOrderWithUnknownIngredients(
            String accessToken,
            List<String> ingredientIds,
            int expectedCode) {
        given()
                .contentType(JSON)
                .log().method().log().uri().log().headers().log().body()
                .when()
                .header("Authorization", accessToken)
                .body(new Object() {
                    public List<?> ingredients = ingredientIds;
                })
                .post("/orders")
                .then()
                .log().status().log().body()
                .statusCode(expectedCode);
    }

    protected GetUserOrdersResponseVO getUserOrders(String accessToken) {
        return getUserOrders(accessToken, true, SC_OK);
    }

    protected GetUserOrdersResponseVO getUserOrders(
            String accessToken,
            boolean expectedSuccess,
            int expectedCode) {
        return given()
                .contentType(JSON)
                .log().method().log().uri().log().headers().log().body()
                .when()
                .header("Authorization", accessToken)
                .get("/orders")
                .then()
                .log().status().log().body()
                .statusCode(expectedCode)
                .body("success", equalTo(expectedSuccess))
                .extract().as(GetUserOrdersResponseVO.class);
    }
}
