package com.automation.gitHubApi;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class RepositoryInfo {

    /*
    Repository id information
    1.Send a get request to /orgs/:org/repos.
     Request includes :•Path param org with value cucumber
     2.Verify that id field is unique in every in every object in the response
     3.Verify that node_id field is unique in every in every object in the response
     */

    @BeforeAll
    public static void setup(){
        baseURI = "https://api.github.com";

    }

    @Test
    public void repositoryIdInfo(){

        Response response = given().accept(ContentType.JSON)
                .auth().basic("curl -u \"username\" ", "https://api.github.com")
                .given().pathParam("org", "cucumber")
                .when().get("/orgs/:org/repos");

        //2.Verify that id field is unique in every in every object in the response

        List<Map<String, Integer>> actual = response.jsonPath().getList("id");
        System.out.println("actual = " + actual);
       // for(Integer each : getId){

        response.then().assertThat().body("items.id", everyItem(not(equalTo(actual))));


        //3.Verify that node_id field is unique in every in every object in the response

        List<Map<String, Object>> actual1 = response.jsonPath().getList("node_id");
        System.out.println("actual1 = " + actual1);

        response.then().assertThat().body("items.node_id", everyItem(not(equalTo(actual1))));




    }


}
