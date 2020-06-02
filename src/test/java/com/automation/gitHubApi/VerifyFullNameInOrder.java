package com.automation.gitHubApi;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;

public class VerifyFullNameInOrder {

    /*
    Ascending order by full_name sort
    1.Send a get request to /orgs/:org/repos.
    Request includes :•Path param org with value cucumber•
    Query param sort with value full_name
    2.Verify that all repositories are listed in alphabetical order
    based on the value of the field name
     */

    @BeforeAll
    public static void setup() {
        baseURI = "https://api.github.com";
        authentication = basic("curl -u \"username\" ", "https://api.github.com");

    }

    @Test
    @DisplayName("Verify that full name sorted in ascending order")
    public void fullNameInAscendingOrder() {

        Response response = given().accept(ContentType.JSON)
                 //.pathParam("org", "cucumber")
                .queryParam("sort", "full_name")
                .when().get("/orgs/{:org}/repos", "cucumber");

        List<String> fullName = response.jsonPath().getList("full_name");
        System.out.println("fullName = " + fullName);

        List<String> ascendingOrder = new ArrayList<>();
        ascendingOrder.addAll(fullName);
        Collections.sort(ascendingOrder);

        response.then().assertThat().body("full_name", is(ascendingOrder));


    }

    /*
    Descending order by full_name sort
    1.Send a get request to /orgs/:org/repos.
    Request includes :•Path param org with value cucumber•
    Query param sort with value full_name•
    Query param direction with value desc
    2.Verify that all repositories are listed in reverser
    alphabetical order based on the value of the field name
     */

    @Test
    @DisplayName("Verify that full name sorted in descending order")
    public void fullNameInDescendingOrder(){

        Response response = given().accept(ContentType.JSON)
                //.pathParam("org", "cucumber")
                .queryParam("sort", "full_name")
                .queryParam("direction", "desc")
                .and().queryParam("per_page", 100)
                .when().get("/orgs/{:org}/repos", "cucumber");


        List<String> fullName = response.then().extract().jsonPath().getList("full_name");
        System.out.println("fullName = " + fullName); // prints in descending order


        response.then().assertThat().body("full_name", equalTo(fullName));



     //   assertEquals(fullName.stream().sorted().collect(Collectors.toList()) ,fullName);


    }

    /*
    Default sort
    1.Send a get request to /orgs/:org/repos.
    Request includes :•Path param org with value cucumber
    2.Verify that by default all repositories are listed
    in descending order based on the value of the field created_at

     */

    @Test
    @DisplayName("Verify that created_id by default in descending order")
    public void defaultSort(){
        Response response = given().accept(ContentType.JSON)
                .queryParam("per_page", 100)
                .when().get("/orgs/{:org}/repos", "cucumber");

        List<String> created = response.then().extract().jsonPath().getList("created_at");
        System.out.println("created = " + created);

        assertEquals(created.stream().sorted().collect(Collectors.toList()), created);
    }




}
