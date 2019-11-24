package org.vistula.restassured.information;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.junit.Test;
import org.vistula.restassured.RestAssuredTest;
import org.vistula.restassured.pet.Information;

import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;

public class ZadanieDomowe {

    @Test
    public void shouldCreateNewItem() {
        JSONObject requestParams = new JSONObject();
        String name = RandomStringUtils.randomAlphabetic(10);
        String nationality = RandomStringUtils.randomAlphabetic(10);
        int salary = ThreadLocalRandom.current().nextInt(20,Integer.MAX_VALUE);
        requestParams.put("name", name);
        requestParams.put("nationality", nationality);
        requestParams.put("salary", salary);

        Information information = given().header("Content-Type", "application/json")
                .body(requestParams.toString())
                .post("/information")
                .then()
                .log().all()
                .statusCode(201)
                .extract().body().as(Information.class);

        long id = information.getId();

        assertThat(id).isGreaterThan(0);
        assertThat(information.getName()).isEqualTo(name);
        assertThat(information.getNationality()).isEqualTo(nationality);
        assertThat(information.getSalary()).isEqualTo(salary);

        given().delete("/information/"+id)
                .then()
                .log().all()
                .statusCode(204);
    }

}