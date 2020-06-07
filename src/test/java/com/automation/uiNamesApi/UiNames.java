package com.automation.uiNamesApi;

import static io.restassured.RestAssured.*;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;

public class UiNames {


    @BeforeAll
    public static void setup(){

        baseURI = "https://cybertek-ui-names.herokuapp.com/api/";
    }

     /*
    TEST CASES
    No params test
    1.Send a get request without providing any parameters
    2.Verify status code 200, content type application/json; charset=utf-8
    3.Verify that name, surname, gender, region fields have value
     */

    @Test
    @DisplayName("No params test")
    public void test1(){
        Response response = given()
                .accept(ContentType.JSON)
                .get().prettyPeek();

        response.then()
                        .assertThat().statusCode(200)
                        .contentType("application/json; charset=utf-8")
                .and()
                        .body("name", not(empty()))
                        .body("surname", not(empty()))
                        .body("gender", is(notNullValue()))
                        .body("region", not(empty()));
    }

    /*
    Gender test
    1.Create a request by providing query parameter: gender, male or female
    2.Verify status code 200, content type application/json; charset=utf-8
    3.Verify that value of gender field is same from step 1
     */

    @Test
    @DisplayName("Gender test")
    public void test2(){

         given()
                .queryParam("gender", "female").
             //   .queryParam("gender", "male").
          when()
                .get().prettyPeek().
          then()
                .assertThat().statusCode(200)
                .contentType("application/json; charset=utf-8").
                        and()
                .assertThat().body("gender", is("female"));

    }

    /*
    2 params test
    1.Create a request by providing query parameters: a valid region and gender
    NOTE: Available region values are given in the documentation
    2.Verify status code 200, content type application/json; charset=utf-8
    3.Verify that value of gender field is same from step 1
    4.Verify that value of region field is same from step 1
     */

    @Test
    @DisplayName("2 params test")
    public void test3(){

        String gender = "male";
        String region = "Germany";

        Response response = given()
                .queryParam("region", region)
                .queryParam("gender", gender)
        .get().prettyPeek();

        response
                .then().assertThat().statusCode(200)
                .contentType("application/json; charset=utf-8").
         and()
                .assertThat().body("region", is(region))
                             .body("gender", is(gender));
    }

    /*
    Invalid gender test
    1.Create a request by providing query parameter: invalid gender
    2.Verify status code 400 and status line contains Bad Request
    3.Verify that value of error field is Invalid gender
     */
    @Test
    @DisplayName("Invalid gender test")
    public void test4(){
        Response response = given()
                .queryParam("gender", "invalid")
                .get().prettyPeek();
        response.
                then().assertThat().statusCode(400)
                .statusLine(containsString("Bad Request"))
                .body("error", is("Invalid gender"));
    }

    /*
    Invalid region test
    1.Create a request by providing query parameter: invalid region
    2.Verify status code 400 and status line contains Bad Request
    3.Verify that value of error field is Regionorlanguagenotfound
     */
    @Test
    @DisplayName("Invalid region test")
    public void test5(){
        Response response = given()
                .queryParam("region", "invalid")
                .get().prettyPeek();
        response.
                then().assertThat().statusCode(400)
                .statusLine(containsString("Bad Request"))
                .body("error", is("Region or language not found"));
    }

    /*
    Amount and regions test
    1.Create request by providing query parameters: a valid region and amount(must be bigger than 1)
    2.Verify status code 200, content type application/json; charset=utf-8
    3.Verify that all objects have different name+surname combination
     */
    @Test
    @DisplayName("Amount and regions test")
    public void test6() {
        Response response = given()
                .queryParam("region", "Kyrgyz Republic")
                .queryParam("amount", 18)
                .get().prettyPeek();


        // our custom class UserInfo
        List<UserInfo> usersLst = response.jsonPath().getList("",UserInfo.class);
        System.out.println(usersLst);
        Set<String> fullNames = new HashSet<>();


        for (UserInfo user : usersLst) {
            String fullName = user.getName() + " " + user.getSurname();
            fullNames.add(fullName);
        }

        response.
                then().assertThat().statusCode(200)
                .contentType("application/json; charset=utf-8")
                .body("size()", is(fullNames.size()));
        
    }

    /*
    3 params test
    1.Create a request by providing query parameters: a valid region, gender and amount (must be biggerthan 1)
    2.Verify status code 200, content type application/json; charset=utf-8
    3.Verify that all objects the response have the same region and gender passed in step 1
     */

    @Test
    @DisplayName("3 params test")
    public void test7(){

        Response response = given().queryParam("region", "Kyrgyz Republic")
                .queryParam("gender", "female")
                .queryParam( "amount",18)
                .when().get().prettyPeek();

        response.then().statusCode(200).contentType("application/json; charset=utf-8")
                .and().body("size()",is (18))
                .body("gender",everyItem(is("female")))
                .body("region",everyItem(is("Kyrgyz Republic")));

    }


    /*
    Amount count test
    1.Create a request by providing query parameter: amount (must be bigger than 1)
    2.Verify status code 200, content type application/json; charset=utf-8
    3.Verify that number of objects returned in the response is same as the amount passed in step 1
     */

    @Test
    @DisplayName("Amount count test")
    public void test8() {

        Response response = given()
                        .queryParams("amount",88)
                        .when()
                        .get().prettyPeek();

        response.then().assertThat().statusCode(200)
                .and().contentType("application/json; charset=utf-8")
                .body("size()",is(88));
    }











}
