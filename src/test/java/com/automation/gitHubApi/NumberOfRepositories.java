package com.automation.gitHubApi;

import org.junit.jupiter.api.BeforeAll;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import static io.restassured.RestAssured.*;

public class NumberOfRepositories {

    /*
    Number of repositories
    1.Send a get request to /orgs/:org.
    Request includes :•Path param org with value cucumber
    2.Grab the value of the field public_repos
    3.Send a get request to /orgs/:org/repos.
    Request includes :•Path param org with value cucumber
    4.Verify that number of objects in the response  is equal to value from step 2
     */

    @BeforeAll
    public static void setup(){
        baseURI = "https://api.github.com";

    }


    @Test
    public void numberOfRepos(){

//        Map<String, String> params = new HashMap<>();
//        params.put("org", "cucumber");
//        params.put("field", "public_repos");

        Response response = given().accept(ContentType.JSON)
                .auth().basic("curl -u \"username\" ", "https://api.github.com")
                .and().queryParam("org", "cucumber")
                .when().get("/orgs/:org");


       int expected = response.jsonPath().getInt("public_repos");
       System.out.println("expected = " + expected);

       // 3.Send a get request to /orgs/:org/repos.
       // Request includes :•Path param org with value cucumber
        Response response1 = given().queryParam("org", "cucumber")
                //in their API default number of repos is 30, we can specify how many
                // repos we wanna see in one page --> limit is 100
                .and().queryParam("per_page", 100)
                .when().get("/orgs/:org/repos");

        List<Object> lstOfRepos = response1.jsonPath().getList("");
        System.out.println("lstOfRepos = " + lstOfRepos);

            assertEquals(expected, lstOfRepos.size());

    }





    }
