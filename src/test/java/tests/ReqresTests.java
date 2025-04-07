package tests;

import io.restassured.response.Response;
import models.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReqresTests {

    @BeforeAll
    static void setuo() {
        baseURI = "https://reqres.in";
        basePath = "/api";
    }

    @Test
    void createUserTest() {

        UserBodyModel userData = new UserBodyModel();
        userData.setName("Timur");
        userData.setJob("QA");

        CreateUserModel responce = given()
                .body(userData)
                .contentType(JSON)
                .log().all()

            .when()
                .post("/users")

            .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .extract().as(CreateUserModel.class);

        assertEquals("Timur", responce.getName());
        assertEquals("QA", responce.getJob());
    }

    @Test
    void updateUserNameTest() {

        UserBodyModel userData = new UserBodyModel();
        userData.setName("Timur");
        userData.setJob("QA");

        UserBodyModel userUpdateData = new UserBodyModel();
        userUpdateData.setName("Timur");
        userUpdateData.setJob("QA Lead");

        CreateUserModel response =
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
                    .extract().as(CreateUserModel.class);

        assertEquals("Timur", response.getName());
        assertEquals("QA", response.getJob());
        String userId = response.getId();

        UpdateUserModel responce = given()
                .body(userUpdateData)
                .contentType(JSON)
                .log().all()

            .when()
                .patch("/users/" + userId)

            .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().as(UpdateUserModel.class);

        assertEquals("Timur", responce.getName());
        assertEquals("QA Lead", responce.getJob());
    }

    @Test
    void checkUserTest() {
        CheckUserModel responce = given()
                .log().all()
                .when()
                    .get("/users/2")
                .then()
                .statusCode(200)
                .log().all()
                .extract().as(CheckUserModel.class);

        assertEquals("Janet", responce.getData().getFirst_name());
        assertEquals("Weaver", responce.getData().getLast_name());
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

        UserListModel responce = given()
                .when()
                .get("/users?page=2")
                .then()
                .log().all()
                .extract().as(UserListModel.class);

        assertEquals("6", responce.getPer_page());
    }
}
