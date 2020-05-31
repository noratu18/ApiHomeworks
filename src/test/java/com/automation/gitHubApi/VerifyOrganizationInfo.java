package com.automation.gitHubApi;


import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class VerifyOrganizationInfo {

    @BeforeAll
    public static void setup(){
        baseURI = "https://api.github.com";

    }

    /*
    Verify organization information  1.Send a get request to /orgs/:org.
    Request includes :•Path param org with value cucumber
    2.Verify status code 200, content type application/json; charset=utf-8
    3.Verify value of the login field is cucumber
    4.Verify value of the name field is cucumber
    5.Verify value of the id field is 320565
     */

  @Test
    public void verifyOrganizationInfo(){

        Response response = given().accept(ContentType.JSON)
                .auth().basic("curl -u \"username\" ", "https://api.github.com")
                .and().queryParam("org","cucumber")
                .when().get("/orgs/:org").prettyPeek();

        response.then().assertThat().statusCode(200)
                .and().assertThat().contentType("application/json; charset=utf-8");

        response.then().body("login", is("cucumber"))
                .and().assertThat().body("name",equalTo("Cucumber"))
                .and().assertThat().body("id", is(320565));




    }
}
