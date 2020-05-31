package com.automation.gitHubApi;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.*;


public class RepoOwnerInfo {

    /*
    Repository owner information
    .Send a get request to /orgs/:org.
    Request includes :•Path param org with value cucumber
    2.Grab the value of the field id
    3.Send a get request to /orgs/:org/repos.
    Request includes :•Path param org with value cucumber
    4.Verify that value of the id inside the owner object in every response is equal to value from step 2
     */

    @BeforeAll
    public static void setup() {
        baseURI = "https://api.github.com";

    }

    @Test
    public void repositoryIdInfo() {

        Response response = given().accept(ContentType.JSON)
                .auth().basic("curl -u \"username\" ", "https://api.github.com")
                .given() //.queryParam("org", "cucumber")
                .when().get("/orgs/{:org}/repos", "cucumber");

        List<String> expected = response.jsonPath().getList("id");
        System.out.println("expected = " + expected);

        Response response1 = given()//.queryParam("org", "cucumber")

                .when().get("/orgs/{:org}/repos", "cucumber");

        List<Object> ownerInfo = response1.jsonPath().getList("owner");
        System.out.println("ownerInfo = " +ownerInfo);

        response1.then().assertThat().body("items.owner", everyItem(equalTo(expected)) );




    }
}