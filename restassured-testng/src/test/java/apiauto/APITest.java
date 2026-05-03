package apiauto;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;


public class APITest {

    String baseURL = "https://api.rizqifauzan.com";

    //Helper Method For Register
    private String registerUser(String email, String password) {

        JSONObject body = new JSONObject();
        body.put("nama", "John Doe");
        body.put("email", email);
        body.put("password", password);

        RestAssured
                .given()
                .baseUri(baseURL)
                .header("Content-Type", "application/json")
                .body(body.toString())
                .post("/api/auth/register")
                .then()
                .assertThat().statusCode(201);

        return email;
    }

    @Epic("Authentication")
    @Feature("Register API")
    @Description("Create a user account")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void registerUserSuccessfully() {

        // Dynamic email
        String email = "user_" + System.currentTimeMillis() + "@mail.com";
        String password = "Password123";

        // Request body
        JSONObject body = new JSONObject();
        body.put("nama", "John Doe");
        body.put("email", email);
        body.put("password", password);

        // Send request
        Response response =
                RestAssured
                        .given()
                        .baseUri(baseURL)
                        .header("Content-Type", "application/json")
                        .body(body.toString())
                        .post("/api/auth/register");

        // Assertions
        response.then()
                .log().all()
                .assertThat().statusCode(201)
                .assertThat().body("success", equalTo(true))
                .assertThat().body("message", equalTo("Registrasi berhasil"))
                .assertThat().body("data.id", notNullValue())
                .assertThat().body("data.email", equalTo(email));
    }

    @Epic("Authentication")
    @Feature("Login API")
    @Description("Verify if the created user can login")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void loginSuccessfully() {

        // Dynamic email
        String email = "user_" + System.currentTimeMillis() + "@mail.com";
        String password = "Password123";
        registerUser(email, password);

        // Login
        JSONObject loginBody = new JSONObject();
        loginBody.put("email", email);
        loginBody.put("password", password);

        Response response =
                RestAssured
                        .given()
                        .baseUri(baseURL)
                        .header("Content-Type", "application/json")
                        .body(loginBody.toString())
                        .post("/api/auth/login");

        // Assertions
        String token = response.jsonPath().getString("data.token");

        response.then()
                .log().all()
                .assertThat().statusCode(200)
                .assertThat().body("success", equalTo(true))
                .assertThat().body("message", equalTo("Login berhasil"))
                .assertThat().body("data.user.email", equalTo(email))
                .assertThat().body("data.token", notNullValue());

        assert token != null;
        assert token.split("\\.").length == 3;
    }

    @Epic("Authentication")
    @Feature("Login API")
    @Description("Verify if the created user can't login with the wrong password")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void loginWithWrongPassword() {

        String email = "user_" + System.currentTimeMillis() + "@mail.com";
        String password = "Password123";
        String wrongPassword = "WrongPassword123";
        registerUser(email, password);


        // Login
        JSONObject loginBody = new JSONObject();
        loginBody.put("email", email);
        loginBody.put("password", wrongPassword);

        Response response =
                RestAssured
                        .given()
                        .baseUri(baseURL)
                        .header("Content-Type", "application/json")
                        .body(loginBody.toString())
                        .post("/api/auth/login");


        // Assertions
        response.then()
                .log().all()
                .assertThat().statusCode(anyOf(is(400), is(401))) // flexible check
                .assertThat().body("success", equalTo(false))
                .assertThat().body("error", equalTo("Email atau password salah"));
    }

    @Epic("Authentication")
    @Feature("Register API")
    @Description("Verify if user can create 2 account with the same password & different email")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void registerMultipleUsers() {

        String password = "Password123";

        // Generate 2 unique emails
        String email1 = "user_" + System.currentTimeMillis() + "_1@mail.com";
        String email2 = "user_" + System.currentTimeMillis() + "_2@mail.com";

        // Register first user
        registerUser(email1, password);

        // Register second user
        registerUser(email2, password);

        // Validation
        assert !email1.equals(email2);
    }

    @Epic("Authentication")
    @Feature("Login API")
    @Description("Check the current user information")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void getCurrentUserProfile() {

        String email = "user_" + System.currentTimeMillis() + "@mail.com";
        String password = "Password123";
        registerUser(email, password);

        // Login
        JSONObject loginBody = new JSONObject();
        loginBody.put("email", email);
        loginBody.put("password", password);

        Response loginResponse =
                RestAssured
                        .given()
                        .baseUri(baseURL)
                        .header("Content-Type", "application/json")
                        .body(loginBody.toString())
                        .post("/api/auth/login");

        // Assertions
        String token = loginResponse.jsonPath().getString("data.token");

        // Checking Me
        Response getResponse =
                RestAssured
                        .given()
                        .baseUri(baseURL)
                        .header("Authorization", "Bearer " + token)
                        .get("/api/auth/me");


        // Assertions
        getResponse.then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("data.email", equalTo(email))
                .body("data.nama", equalTo("John Doe"))
                .body("data.id", notNullValue());
    }
}
