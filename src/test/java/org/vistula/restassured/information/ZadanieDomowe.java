package org.vistula.restassured.information;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.junit.Test;
import org.vistula.restassured.RestAssuredTest;
import org.vistula.restassured.pet.Information;

import java.util.concurrent.ThreadLocalRandom;

import static io.restassured.RestAssured.get;
import static org.assertj.core.api.Assertions.assertThat;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;

public class ZadanieDomowe extends RestAssuredTest{

    @Test
    public void putTest() {
        JSONObject requestParams = randomParams();
        Information object = createObject(requestParams);
        long id = object.getId();
        JSONObject requestNewParams = randomParams();
        putNewParams(requestNewParams, id);
        deleteObject(id);
    }

    @Test
    public void patchTestOne() {
        JSONObject requestParams = randomParams();
        Information object = createObject(requestParams);
        long id = object.getId();
        String oldName = object.getName();
        int oldSalary = object.getSalary();
        JSONObject requestNewParams = oneRandomParam();
        patchOneParam(requestNewParams, id, oldName, oldSalary);
        deleteObject(id);
    }

    @Test
    public void patchTestTwo() {
        JSONObject requestParams = randomParams();
        Information object = createObject(requestParams);
        long id = object.getId();
        String oldNationality = object.getNationality();
        JSONObject requestNewParams = twoRandomParams();
        patchTwoParams(requestNewParams, id, oldNationality);
        deleteObject(id);
    }


    private JSONObject randomParams() {
        JSONObject requestParams = new JSONObject();
        String name = RandomStringUtils.randomAlphabetic(10);
        String nationality = RandomStringUtils.randomAlphabetic(10);
        int salary = ThreadLocalRandom.current().nextInt(20,Integer.MAX_VALUE);
        requestParams.put("name", name);
        requestParams.put("nationality", nationality);
        requestParams.put("salary", salary);
        return requestParams;
    }

    private Information createObject(JSONObject requestParams) {
        return given().header("Content-Type", "application/json")
                .body(requestParams.toString())
                .post("/information")
                .then()
                .log().all()
                .statusCode(201)
                .extract().body().as(Information.class);
    }

    private void putNewParams(JSONObject requestNewParams, long id) {

        Information newInformation = given().header("Content-Type", "application/json")
                .body(requestNewParams.toString())
                .put("/information/"+id)
                .then()
                .log().all()
                .statusCode(200)
                .extract().body().as(Information.class);

        assertThat(id).isEqualTo(id);
        assertThat(newInformation.getName()).isEqualTo(requestNewParams.getString("name"));
        assertThat(newInformation.getNationality()).isEqualTo(requestNewParams.getString("nationality"));
        assertThat(newInformation.getSalary()).isEqualTo(requestNewParams.getInt("salary"));
    }

    private void deleteObject(long id) {
        given().delete("/information/"+id)
                .then()
                .log().all()
                .statusCode(204);
    }

    private JSONObject oneRandomParam() {
        JSONObject requestParams = new JSONObject();
        String nationality = RandomStringUtils.randomAlphabetic(10);
        requestParams.put("nationality", nationality);
        return requestParams;
    }

    private JSONObject twoRandomParams() {
        JSONObject requestParams = new JSONObject();
        String name = RandomStringUtils.randomAlphabetic(10);
        int salary = ThreadLocalRandom.current().nextInt(20,Integer.MAX_VALUE);
        requestParams.put("name", name);
        requestParams.put("salary", salary);
        return requestParams;
    }

    private void patchOneParam(JSONObject requestNewParams, long id, String oldName, int oldSalary) {

        Information newInformation = given().header("Content-Type", "application/json")
                .body(requestNewParams.toString())
                .patch("/information/" + id)
                .then()
                .log().all()
                .statusCode(200)
                .extract().body().as(Information.class);

        assertThat(id).isEqualTo(id);
        assertThat(newInformation.getName()).isEqualTo(oldName);
        assertThat(newInformation.getNationality()).isEqualTo(requestNewParams.getString("nationality"));
        assertThat(newInformation.getSalary()).isEqualTo(oldSalary);
    }

    private void patchTwoParams(JSONObject requestNewParams, long id, String oldNationality) {

        Information newInformation = given().header("Content-Type", "application/json")
                .body(requestNewParams.toString())
                .patch("/information/" + id)
                .then()
                .log().all()
                .statusCode(200)
                .extract().body().as(Information.class);

        assertThat(id).isEqualTo(id);
        assertThat(newInformation.getName()).isEqualTo(requestNewParams.getString("name"));
        assertThat(newInformation.getNationality()).isEqualTo(oldNationality);
        assertThat(newInformation.getSalary()).isEqualTo(requestNewParams.getInt("salary"));
    }
}
