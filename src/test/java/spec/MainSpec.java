package spec;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.*;
import static io.restassured.http.ContentType.JSON;

public class MainSpec {

    public static RequestSpecification mainRequestSpec = with()
            .filter(new AllureRestAssured())
            .contentType(JSON)
            .log().all();

    public static ResponseSpecification mainResponseSpec = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .log(ALL)
            .build();

    public static ResponseSpecification createdResponseSpec = new ResponseSpecBuilder()
            .expectStatusCode(201)
            .log(ALL)
            .build();

    public static ResponseSpecification noContentResponseSpec = new ResponseSpecBuilder()
            .expectStatusCode(204)
            .log(ALL)
            .build();

}
