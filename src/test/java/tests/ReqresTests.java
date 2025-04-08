package tests;

import models.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.*;

import static org.assertj.core.api.Assertions.assertThat;
import static spec.MainSpec.*;

public class ReqresTests {

    @BeforeAll
    static void setUp() {
        baseURI = "https://reqres.in";
        basePath = "/api";
    }

    @Test
    void createUserTest() {

        UserBodyModel userData = new UserBodyModel();
        userData.setName("Timur");
        userData.setJob("QA");

        CreateUserModel response = step("Создать пользователя", ()-> given(mainRequestSpec)
                .body(userData)
                .when()
                    .post("/users")

                .then()
                    .spec(createdResponseSpec)
                    .extract().as(CreateUserModel.class));

        step("Проверить данные пользователя:", ()-> {
            assertThat(response.getName()).isEqualTo(userData.getName());
            assertThat(response.getJob()).isEqualTo(userData.getJob());
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

        CreateUserModel firstResponse = step("Создать пользвателя", ()-> given(mainRequestSpec)
                .body(userData)
                .when()
                    .post("/users")

                .then()
                    .spec(createdResponseSpec)
                    .extract().as(CreateUserModel.class));

        step("Проверить данные пользователя", ()-> {
            assertThat(firstResponse.getName()).isEqualTo(userData.getName());
            assertThat(firstResponse.getJob()).isEqualTo(userData.getJob());
        });


        String userId = step("Получить Id пользователя", firstResponse::getId);

        UpdateUserModel secondResponse = step("Изменить данные пользвателя", ()-> given(mainRequestSpec)
                .body(userUpdateData)

            .when()
                .patch("/users/" + userId)

            .then()
                .spec(mainResponseSpec)
                .extract().as(UpdateUserModel.class)
        );

        step("Проверить измененные данные пользователя", ()-> {
            assertThat(secondResponse.getName()).isEqualTo(userUpdateData.getName());
            assertThat(secondResponse.getJob()).isEqualTo(userUpdateData.getJob());
        });
    }

    @Test
    void checkUserTest() {
        CheckUserModel response = step("Найти пользователя с id = 2", ()-> given(mainRequestSpec)

                .when()
                    .get("/users/2")

                .then()
                    .spec(mainResponseSpec)
                    .extract().as(CheckUserModel.class));

        step("Проверить имя и фамилию пользователя с id = 2", ()-> {
            assertThat(response.getData().getFirstName()).isEqualTo("Janet");
            assertThat(response.getData().getLastName()).isEqualTo("Weaver");
        });
    }

    @Test
    void deleteUserTest() {

        step("Удалить пользователя с id = 2", ()-> given(mainRequestSpec)

                .when()
                    .delete("/users/2")

                .then()
                    .spec(noContentResponseSpec));
    }

    @Test
    void countUserInListTest() {

        UserListModel response = step("Открыть страницу с пользователями", ()-> given(mainRequestSpec)

                .when()
                    .queryParam("page", "2")
                    .get("/users")

                .then()
                    .spec(mainResponseSpec)
                    .extract().as(UserListModel.class));

        step("Проверить количество пользователей", ()->
                assertThat(response.getPerPage()).isEqualTo("6"));
    }
}
