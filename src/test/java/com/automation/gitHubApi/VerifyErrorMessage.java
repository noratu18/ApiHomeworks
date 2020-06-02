package com.automation.gitHubApi;

import org.junit.jupiter.api.BeforeAll;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class VerifyErrorMessage {

    /*
    Verify error message
    1.Send a get request to /orgs/:org.
    Request includes :•Header Accept with value application/xml•
    Path param org with value cucumber
    2.Verify status code 415, content type application/json; charset=utf-8
    3.Verify response status line include message Unsupported Media Type
     */

    @BeforeAll
    public static void setup(){
        baseURI = "https://api.github.com";

    }

    @Test
    public  void verifyErrorMessage(){

        Response response = given().contentType(ContentType.JSON)
                .auth().basic("curl -u \"username\"", "https://api.github.com")
                .accept(ContentType.XML)
                .and().queryParam("org", "cucumber")
                .when().get("/orgs/:org").prettyPeek();

        // 2.Verify status code 415, content type application/json; charset=utf-8
        // 3.Verify response status line include message Unsupported Media Type
        response.then().assertThat().statusCode(415)
        .and().assertThat().contentType("application/json; charset=utf-8");
        response.then().assertThat().statusLine("HTTP/1.1 415 Unsupported Media Type");





    }
}
