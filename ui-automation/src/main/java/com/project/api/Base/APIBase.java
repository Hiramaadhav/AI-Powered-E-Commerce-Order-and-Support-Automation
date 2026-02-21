package com.project.api.Base;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;

import static io.restassured.RestAssured.given;

/**
 * Base class for all API tests
 */
public class APIBase {
    protected static final Logger logger = LogManager.getLogger(APIBase.class);
    protected RequestSpecification requestSpec;

    /**
     * Initialize the request specification before each test class
     */
    @BeforeClass
    public void setupAPIBase() {
        logger.info("Setting up API Base configuration");
        RestAssured.useRelaxedHTTPSValidation();
        requestSpec = given()
                .contentType("application/x-www-form-urlencoded")
                .accept("application/json")
                .log().all();
    }

    /**
     * Perform GET request
     */
    protected Response performGET(String endpoint) {
        logger.info("Performing GET request to: " + endpoint);
        return requestSpec.when()
                .get(endpoint)
                .then()
                .log().all()
                .extract().response();
    }

    /**
     * Perform GET request with query parameters
     */
    protected Response performGET(String endpoint, Object... params) {
        logger.info("Performing GET request to: " + endpoint + " with params");
        RequestSpecification spec = given()
                .contentType("application/x-www-form-urlencoded")
                .accept("application/json")
                .log().all();

        for (int i = 0; i < params.length; i += 2) {
            spec.queryParam(params[i].toString(), params[i + 1]);
        }

        return spec.when()
                .get(endpoint)
                .then()
                .log().all()
                .extract().response();
    }

    /**
     * Perform POST request with form data
     */
    protected Response performPOST(String endpoint, Object... params) {
        logger.info("Performing POST request to: " + endpoint);
        RequestSpecification spec = given()
                .contentType("application/x-www-form-urlencoded")
                .accept("application/json")
                .log().all();

        for (int i = 0; i < params.length; i += 2) {
            spec.formParam(params[i].toString(), params[i + 1]);
        }

        return spec.when()
                .post(endpoint)
                .then()
                .log().all()
                .extract().response();
    }

    /**
     * Perform POST request with body
     */
    protected Response performPOSTWithBody(String endpoint, Object body) {
        logger.info("Performing POST request with body to: " + endpoint);
        return given()
                .contentType("application/json")
                .accept("application/json")
                .body(body)
                .log().all()
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .extract().response();
    }

    /**
     * Perform PUT request with form data
     */
    protected Response performPUT(String endpoint, Object... params) {
        logger.info("Performing PUT request to: " + endpoint);
        RequestSpecification spec = given()
                .contentType("application/x-www-form-urlencoded")
                .accept("application/json")
                .log().all();

        for (int i = 0; i < params.length; i += 2) {
            spec.formParam(params[i].toString(), params[i + 1]);
        }

        return spec.when()
                .put(endpoint)
                .then()
                .log().all()
                .extract().response();
    }

    /**
     * Perform PUT request with body
     */
    protected Response performPUTWithBody(String endpoint, Object body) {
        logger.info("Performing PUT request with body to: " + endpoint);
        return given()
                .contentType("application/json")
                .accept("application/json")
                .body(body)
                .log().all()
                .when()
                .put(endpoint)
                .then()
                .log().all()
                .extract().response();
    }

    /**
     * Perform DELETE request
     */
    protected Response performDELETE(String endpoint) {
        logger.info("Performing DELETE request to: " + endpoint);
        return given()
                .contentType("application/x-www-form-urlencoded")
                .accept("application/json")
                .log().all()
                .when()
                .delete(endpoint)
                .then()
                .log().all()
                .extract().response();
    }

    /**
     * Perform DELETE request with form data
     */
    protected Response performDELETE(String endpoint, Object... params) {
        logger.info("Performing DELETE request to: " + endpoint);
        RequestSpecification spec = given()
                .contentType("application/x-www-form-urlencoded")
                .accept("application/json")
                .log().all();

        for (int i = 0; i < params.length; i += 2) {
            spec.formParam(params[i].toString(), params[i + 1]);
        }

        return spec.when()
                .delete(endpoint)
                .then()
                .log().all()
                .extract().response();
    }
}
