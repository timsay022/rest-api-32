package tests;

import models.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static spec.MainSpec.*;

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

        CreateUserModel responce = step("Создать пользователя", ()-> given(mainRequestSpec)
                .body(userData)
                .when()
                    .post("/users")

                .then()
                    .spec(createdResponseSpec)
                    .extract().as(CreateUserModel.class));

        step("Проверить данные пользователя:", ()-> {
            assertEquals("Timur", responce.getName());
            assertEquals("QA", responce.getJob());
                });
    }

    @Test
    void updateUserNameTest() {

        UserBodyModel userData = new UserBodyModel();
        userData.setName("Timur");
        userData.setJob("QA");

        UserBodyModel userUpdateData = new UserBodyModel();
        userUpdateData.setName("Timur");
        userUpdateData.setJob("QA Lead");

        CreateUserModel response = step("Создать пользвателя", ()-> given(mainRequestSpec)
                .body(userData)
                .when()
                    .post("/users")

                .then()
                    .spec(createdResponseSpec)
                    .extract().as(CreateUserModel.class));

        step("Проверить данные пользователя", ()-> {
            assertEquals("Timur", response.getName());
            assertEquals("QA", response.getJob());
        });


        String userId = step("Получить Id пользователя", response::getId);

        UpdateUserModel responce = step("Изменить данные пользвателя", ()-> given(mainRequestSpec)
                .body(userUpdateData)

            .when()
                .patch("/users/" + userId)

            .then()
                .spec(mainResponseSpec)
                .extract().as(UpdateUserModel.class)
        );

        step("Проверить измененные данные пользователя", ()-> {
            assertEquals("Timur", responce.getName());
            assertEquals("QA Lead", responce.getJob());
        });
    }

    @Test
    void checkUserTest() {
        CheckUserModel responce = step("Найти пользователя с id = 2", ()-> given(mainRequestSpec)

                .when()
                    .get("/users/2")

                .then()
                    .spec(mainResponseSpec)
                    .extract().as(CheckUserModel.class));

        step("Проверить имя и фамилию пользователя с id = 2", ()-> {
            assertEquals("Janet", responce.getData().getFirstName());
            assertEquals("Weaver", responce.getData().getLastName());
        });
    }

    @Test
    void DeleteUserTest() {

        step("Удалить пользователя с id = 2", ()-> given(mainRequestSpec)

                .when()
                    .delete("/users/2")

                .then()
                    .spec(noContentResponseSpec));
    }

    @Test
    void CountUserInListTest() {

        UserListModel responce = step("Открыть страницу с пользователями", ()-> given(mainRequestSpec)

                .when()
                    .get("/users?page=2")

                .then()
                    .spec(mainResponseSpec)
                    .extract().as(UserListModel.class));

        step("Проверить количество пользователей", ()->
                assertEquals("6", responce.getPerPage()));
    }
}
