package com.automation.harryPotterApi;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class HarryPotter {

    private String apiKey = "$2a$10$AWcXhrTWcLaT4w4wHjDvjOcZqpKb9mK0zBFjEwiu9jv5rEkDhgNgq";


    @BeforeAll
    public static void setup() {
        baseURI = "https://www.potterapi.com/v1/";

    }

    // $2a$10$AWcXhrTWcLaT4w4wHjDvjOcZqpKb9mK0zBFjEwiu9jv5rEkDhgNgq

    /*
    Verify sorting hat
    1.Send a get request to /sortingHat.
     Request includes :
    2.Verify status code 200, content type application/json; charset=utf-8
    3.Verify that response body contains one of the following houses:
    "Gryffindor", "Ravenclaw", "Slytherin", "Hufflepuff"
     */

    @Test
    @DisplayName("Verify sorting hat")
    public void test1() {
        List<String> houseNames = Arrays.asList("Gryffindor", "Ravenclaw", "Slytherin", "Hufflepuff");

        Response response = get("/sortingHat").prettyPeek();

        String responseAsString = response.as(String.class);
        response.then().assertThat().statusCode(200).contentType("application/json; charset=utf-8");

        assertTrue(houseNames.contains(responseAsString));
        //    assertTrue(houseNames.contains(response.body().asString()));


    }

    /*
    Verify bad key
    1.Send a get request to /characters.
     Request includes :•Header Accept with value application/json•Query param key with value invalid
     2.Verify status code 401,
     content type application/json; charset=utf-8
     3.Verify response status line include message Unauthorized
     4.Verify that response body says"error":"API Key Not Found"
     */
    @Test
    @DisplayName("Verify bad key")
    public void test2() {
        Response response = given().accept(ContentType.JSON)
                .queryParam("key", "invalid")
                .get("/characters").prettyPeek();
        response.then().assertThat().statusCode(401)
                .assertThat().contentType("application/json; charset=utf-8")
                .statusLine(containsString("Unauthorized"))
                .body("error", is("API Key Not Found"));
    }

    /*
    Verify no key
    1.Send a get request to /characters.
    Request includes :•Header Accept with value application/json
    2.Verify status code 409, content type application/json; charset=utf-8
    3.Verify response status line include message Conflict
    4.Verify that response body says"error":"Must pass API key for request"
     */

    @Test
    @DisplayName("Verify no key")
    public void test3() {
        Response response = given().accept(ContentType.JSON)
                .get("/characters").prettyPeek();
        response.then().assertThat().statusCode(409)
                .assertThat().contentType("application/json; charset=utf-8")
                .assertThat().statusLine(containsString("Conflict"))
                .body("error", is("Must pass API key for request"));
    }

    /*
    Verify number of characters
    1.Send a get request to /characters.
    Request includes :•Header Accept with value application/json•
    Query param key with value {{apiKey}}
    2.Verify status code 200, content type application/json; charset=utf-8
    3.Verify response contains 194 characters
     */
    @Test
    @DisplayName("Verify number of characters")
    public void test4() {
        Response response = given().accept(ContentType.JSON)
                .queryParam("key", apiKey)
                .get("/characters").prettyPeek();

        response.then().assertThat().statusCode(200)
                .assertThat().contentType("application/json; charset=utf-8")
                .assertThat().body("size()", is(194)); //actual is 195
    }

    /*
    Verify number of character id and house
    1.Send a get request to /characters.
    Request includes :•Header Accept with value application/json•
    Query param key with value {{apiKey}}
    2.Verify status code 200, content type application/json; charset=utf-8
    3.Verify all characters in the response have id field which is not empty
    4.Verify that value type of the field dumbledoresArmy is a boolean in all characters in the response
    5.Verify value of the house in all characters in the response is one of the following:
        "Gryffindor", "Ravenclaw", "Slytherin", "Hufflepuff"
     */
    @Test
    @DisplayName("Verify number of character id and house")
    public void test6() {
        Response response = given().accept(ContentType.JSON)
                .queryParam("key", apiKey)
                .get("/characters").prettyPeek();

        response.then().assertThat().statusCode(200)
                .assertThat().contentType("application/json; charset=utf-8")
                .body("_id", everyItem(is(not(empty()))))
                .body("dumbledoresArmy", everyItem(is(instanceOf(Boolean.class))))
                .body("house", everyItem(is(oneOf("Gryffindor", "Ravenclaw", "Slytherin", "Hufflepuff", null))));
    }

    /*
    Verify all character information
    1.Send a get request to /characters.
    Request includes :•Header Accept with value application/json•
    Query param key with value {{apiKey}}
    2.Verify status code 200, content type application/json; charset=utf-8
    3.Select name of any random character
    4.Send a get request to /characters.
     Request includes :•Header Accept with value application/json•
     Query param key with value {{apiKey}}•
     Query param name with value from step 3
     5.Verify that response contains the same character information from step 3.
      Compare all fields.
     */

    @Test
    @DisplayName("Verify all character information")
    public void test7() {
        Response response = given().accept(ContentType.JSON)
                .queryParam("key", apiKey)
                .get("/characters").prettyPeek();

        response.then().assertThat().statusCode(200)
                .assertThat().contentType("application/json; charset=utf-8");

        List<Character> characters = response.jsonPath().getList("", Character.class);
        System.out.println("characters = " + characters);

        Random random = new Random();
        int randomNum = random.nextInt(characters.size());

        String randomName = characters.get(randomNum).getName();

        // 2nd request
        Response response1 = given().accept(ContentType.JSON)
                .queryParam("key", apiKey)
                .queryParam("name", randomName)
                .get("/characters").prettyPeek();

        response1.then().body("[0].name",is(randomName) );
    }

    /*
    Verify name search
    1.Send a get request to /characters.
    Request includes :•Header Accept with value application/json•
    Query param key with value {{apiKey}}•
    Query param name with value Harry Potter
    2.Verify status code 200, content type application/json; charset=utf-8
    3.Verify name Harry Potter
    4.Send a get request to /characters.
    Request includes :•Header Accept with value application/json•
    Query param key with value {{apiKey}}•Query param name with value Marry Potter
    5.Verify status code 200, content type application/json; charset=utf-8
    6.Verify response body is empty
     */
    @Test
    @DisplayName("Verify name search")
    public void test8() {
        Response response = given().accept(ContentType.JSON)
                .queryParam("key", apiKey)
                .queryParam("name", "Harry Potter")
                .get("/characters").prettyPeek();

        response.then().assertThat().statusCode(200)
                .assertThat().contentType("application/json; charset=utf-8")
                .assertThat().body("[0].name", is("Harry Potter"));
        //2nd request
        Response response1 = given().accept(ContentType.JSON)
                .queryParam("key", apiKey)
                .queryParam("name", "Marry Potter")
                .get("/characters").prettyPeek();

        response1.then().assertThat().statusCode(200)
                .assertThat().contentType("application/json; charset=utf-8")
                .body("[0]", is(emptyOrNullString()));
    }

    /*
    Verify house members
    1.Send a get request to /houses.
    Request includes :•Header Accept with value application/json•
    Query param key with value {{apiKey}}
    2.Verify status code 200, content type application/json; charset=utf-8
    3.Capture the id of the Gryffindor house
    4.Capture the ids of the all members of the Gryffindor house
    5.Send a get request to /houses/:id.
    Request includes :•Header Accept with value application/json•
    Query param key with value {{apiKey}}
    •Path param id with value from step 3
    6.Verify that response contains the  same memberids as the step 4
     */
    @Test
    @DisplayName("Verify house members")
    public void test9() {
        Response response = given().accept(ContentType.JSON)
                .queryParam("key", apiKey)
                .get("/houses").prettyPeek();
        response.then().assertThat().statusCode(200)
                .assertThat().contentType("application/json; charset=utf-8");
        
        String griffindorId = response.jsonPath().getString("find{it.name == 'Gryffindor'}._id");
        System.out.println("griffindorId = " + griffindorId);
        
        List<String> members=response.jsonPath().getList("find{it.name == 'Gryffindor'}.members");
        System.out.println("members = " + members);

        //2nd request
        Response response1 = given().accept(ContentType.JSON)
                .queryParam("key", apiKey)
                .pathParam("id", griffindorId)
                .get("/houses/{id}").prettyPeek();

        List<String> membersId = response1.jsonPath().getList("[0].members._id");
        System.out.println("membersId = " + membersId);

        assertEquals(members, membersId);
    }

    /*
    Verify house members again
    1.Send a get request to /houses/:id.
    Request includes :•Header Accept with value application/json
    •Query param key with value {{apiKey}}
     •Path param id with value 5a05e2b252f721a3cf2ea33f2.
     Capture the ids of all members
     3.Send a get request to /characters.
     Request includes :•Header Accept with value application/json
     •Query param key with value {{apiKey}}
     •Query param house with value Gryffindor
     4.Verify that response contains the same member ids from step 2
     */
    @Test
    @DisplayName("Verify house members again")
    public void test10() {
        Response response = given().accept(ContentType.JSON)
                .queryParam("key", apiKey)
                .pathParam("id", "5a05e2b252f721a3cf2ea33f2")
                .get("/houses/{id}").prettyPeek();

        List<House> members = response.jsonPath().getList("[0].members._id", House.class);

        Response response1 = given().accept(ContentType.JSON)
                .queryParam("key", apiKey)
                .queryParam("house", "Gryffindor")
                .get("/characters").prettyPeek();

        response1.then().assertThat();
    }


    /*
    Verify house with most members
    1.Send a get request to /houses.
    Request includes :•Header Accept with value application/json
    •Query param key with value {{apiKey}}
    2.Verify status code 200, content type application/json; charset=utf-8
    3.Verify that Gryffindor house has the most members
     */
    @Test
    @DisplayName("Verify house with most members")
    public void test11() {
        Response response = given().accept(ContentType.JSON)
                .queryParam("key", apiKey)
                .get("/houses").prettyPeek();
        response.then().assertThat().statusCode(200)
                .assertThat().contentType("application/json; charset=utf-8")
                .body("max{it.members.size()}.name", is("Gryffindor"));

        List<House> houseLst = response.jsonPath().getList("", House.class);
        System.out.println("houseLst = " + houseLst);

        String houseWithMostMembers = "";
        int max = 0;
        for(House each : houseLst){
            int sizeOfMemebrs = each.getMembers().size();
            if(sizeOfMemebrs > max){
                max = sizeOfMemebrs;
                houseWithMostMembers = each.getName();
            }
        }

        assertEquals("Gryffindor", houseWithMostMembers);


    }



}