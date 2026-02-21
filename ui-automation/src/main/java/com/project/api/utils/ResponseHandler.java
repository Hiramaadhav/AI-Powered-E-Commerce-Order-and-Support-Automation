package com.project.api.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ResponseHandler class to handle API responses
 */
public class ResponseHandler {
    private static final Logger logger = LogManager.getLogger(ResponseHandler.class);
    private static final Gson gson = new Gson();

    /**
     * Get status code from response
     */
    public static int getStatusCode(Response response) {
        int statusCode = response.getStatusCode();
        logger.info("Response Status Code: " + statusCode);
        return statusCode;
    }

    /**
     * Get response string
     */
    public static String getResponseAsString(Response response) {
        String responseBody = response.getBody().asString();
        logger.info("Response Body: " + responseBody);
        return responseBody;
    }

    /**
     * Get response as JSON
     */
    public static JsonObject getResponseAsJSON(Response response) {
        return gson.fromJson(response.getBody().asString(), JsonObject.class);
    }

    /**
     * Get specific value from response
     */
    public static String getResponseValue(Response response, String key) {
        JsonObject jsonObject = getResponseAsJSON(response);
        String value = jsonObject.get(key).getAsString();
        logger.info("Response Key: " + key + ", Value: " + value);
        return value;
    }

    /**
     * Verify response status code
     */
    public static boolean verifyResponseStatusCode(Response response, int expectedStatusCode) {
        int actualStatusCode = getStatusCode(response);
        boolean isMatch = actualStatusCode == expectedStatusCode;
        logger.info("Expected Status Code: " + expectedStatusCode + ", Actual: " + actualStatusCode);
        return isMatch;
    }

    /**
     * Verify response message
     */
    public static boolean verifyResponseMessage(Response response, String expectedMessage) {
        String actualMessage = response.getBody().asString();
        boolean isMatch = actualMessage.contains(expectedMessage);
        logger.info("Expected Message: " + expectedMessage);
        logger.info("Actual Message contains expected: " + isMatch);
        return isMatch;
    }

    /**
     * Print response details
     */
    public static void printResponseDetails(Response response) {
        logger.info("============ RESPONSE DETAILS ============");
        logger.info("Status Code: " + response.getStatusCode());
        logger.info("Status Line: " + response.getStatusLine());
        logger.info("Content Type: " + response.getContentType());
        logger.info("Response Body: " + response.getBody().asString());
        logger.info("=========================================");
    }
}
