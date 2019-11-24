package org.vistula.restassured.pet;

import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.junit.Test;
import org.vistula.restassured.RestAssuredTest;

import java.util.concurrent.ThreadLocalRandom;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class PetPostGetScenarioTest extends RestAssuredTest {

    @Test
    public void shouldCreateGetKillPet() {
        JSONObject requestParams = new JSONObject();
        int value = ThreadLocalRandom.current().nextInt(20,Integer.MAX_VALUE);
        requestParams.put("id", value);
        String name = RandomStringUtils.randomAlphabetic(10);
        requestParams.put("name", name);

        createNewPet(requestParams);
        getPet(value, name);
        killPet(value);
    }

    private void killPet(int value) {
        given().delete("/pet/"+value)
                .then()
                .log().all()
                .statusCode(204);
    }

    private void getPet(int value, String name) {
        given().get("/pet/"+ value)
                .then()
                .log().all()
                .statusCode(200)
                .body("id", is(value))
                .body("name", equalTo(name));
    }

    private void createNewPet(JSONObject requestParams) {
        given().header("Content-Type", "application/json")
                .body(requestParams.toString())
                .post("/pet")
                .then()
                .log().all()
                .statusCode(201);
    }

}
