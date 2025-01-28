import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;

import static org.hamcrest.Matchers.*;

public class ReqresTests {

    @BeforeAll
    static void setuo() {
        baseURI = "https://reqres.in";
        basePath = "/api";
    }

    @Test
    void createUserTest() {
        String userData = "{\"name\": \"Timur\", \"job\": \"QA\"}";
        given()
                .body(userData)
                .contentType(JSON)
                .log().all()

            .when()
                .post("/users")

            .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", is("Timur"))
                .body("job", is("QA"));
    }

    @Test
    void updateUserNameTest() {
        String userData = "{\"name\": \"Timur\", \"job\": \"QA\"}";
        String userUpdateData = "{\"name\": \"Timur\", \"job\": \"QA Lead\"}";
        Response response =
            given()
                    .body(userData)
                    .contentType(JSON)
                    .log().all()

                    .when()
                    .post("/users")

                    .then()
                    .log().status()
                    .log().body()
                    .statusCode(201)
                    .body("name", is("Timur"))
                    .body("job", is("QA"))
                    .extract().response();
        String userId = response.path("id");

        given()
                .body(userUpdateData)
                .contentType(JSON)
                .log().all()

            .when()
                .patch("/users/" + userId)

            .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("name", is("Timur"))
                .body("job", is("QA Lead"))
                .extract().response();
    }

    @Test
    void checkUserTest() {
        given()
                .log().all()
                .when()
                    .get("/users/2")
                .then()
                .statusCode(200)
                .log().all()
                .body("data.first_name", is("Janet"))
                .body("data.last_name", is("Weaver"));
    }

    @Test
    void DeleteUserTest() {

        given()
                .when()
                .delete("/users/2")
                .then()
                .statusCode(204);
    }

    @Test
    void CountUserInListTest() {

        given()
                .when()
                .get("/users?page=2")
                .then()
                .body("per_page", is(6));
    }
}
