package com.automation.gitHubApi;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


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
    public void repositoryIdInfo1() {
        Response response = given().accept(ContentType.JSON)
                .auth().basic("curl -u \"username\" ", "https://api.github.com")
                .pathParam("org", "cucumber").when().get("/orgs/{org}");

        int idInfo = response.body().jsonPath().get("id");
        System.out.println("idInfo = " + idInfo);


        Response response1 = given()//.queryParam("org", "cucumber")
                            .when()
                            .get("/orgs/{:org}/repos", "cucumber");

        List<Object> ownerInfo = response1.jsonPath().getList("owner");
        System.out.println("ownerInfo = " + ownerInfo);

        response1.then().assertThat().body("owner.id", everyItem(equalTo(idInfo)));


    }



//    @Test
//    public void repositoryIdInfo() {
//
//        Response response =given().accept(ContentType.JSON)
//                .auth().basic("curl -u \"username\" ", "https://api.github.com")
//                .pathParam("org", "cucumber").when().get("/orgs/{org}");
//
//      int listOfID = response.jsonPath().get("id");
//        System.out.println("listOfID = " + listOfID);
//
////        List<Integer> expected = response.jsonPath().getList("id");
////        System.out.println("expected = " + expected);
//
//        Response response1 = given()//.queryParam("org", "cucumber")
//
//                .when().get("/orgs/{:org}/repos", "cucumber");
//
//        List<Object> ownerInfo = response1.jsonPath().get("owner");
//        System.out.println("ownerInfo = " +ownerInfo);
//
//        response1.then().assertThat().body("owner.id",is(listOfID));
//
//
//
//
//    }


}