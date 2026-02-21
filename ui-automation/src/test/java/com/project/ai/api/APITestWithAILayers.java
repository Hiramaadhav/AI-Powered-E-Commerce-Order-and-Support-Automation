package com.project.ai.api;

import com.project.ai.api.assertions.SmartAssertionEngine;
import com.project.ai.api.performance.PerformanceAnomalyDetector;
import com.project.api.Base.APIBase;
import com.project.api.Endpoints.APIEndpoints;
import com.project.api.utils.ResponseHandler;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import static io.qameta.allure.SeverityLevel.*;

/**
 * AI-Enhanced E-Commerce API Test Suite
 * 
 * This suite integrates AI layers with all API test cases:
 * 1. Smart assertion generation (auto-validate responses)
 * 2. Schema learning and drift detection
 * 3. Performance anomaly detection
 * 4. Baseline establishment and monitoring
 */
@Epic("AI Powered E-Commerce Order and Support Automation")
public class APITestWithAILayers extends APIBase {

    private static final String VALID_EMAIL = "test@example.com";
    private static final String VALID_PASSWORD = "password123";
    private static final String INVALID_EMAIL = "invalid@example.com";
    private static final String INVALID_PASSWORD = "invalid";
    private static final String USER_NAME = "Test User";
    private static final String FIRST_NAME = "Test";
    private static final String LAST_NAME = "User";
    private static final String TITLE = "Mr";
    private static final String BIRTH_DATE = "15";
    private static final String BIRTH_MONTH = "6";
    private static final String BIRTH_YEAR = "1990";
    private static final String COMPANY = "Test Company";
    private static final String ADDRESS1 = "123 Test Street";
    private static final String ADDRESS2 = "Apt 4B";
    private static final String COUNTRY = "United States";
    private static final String STATE = "California";
    private static final String CITY = "San Francisco";
    private static final String ZIPCODE = "94102";
    private static final String MOBILE_NUMBER = "1234567890";
    
    // ==================== PRODUCTS API TESTS ====================
    
    @Test(priority = 1)
    @Story("Get All Products List")
    @Description("Test API 1: Get All Products List - Should return 200 with products list")
    @Severity(CRITICAL)
    public void testGetAllProductsList() {
        logger.info("========== TEST 1: Get All Products List ==========");

        Response response = PerformanceAnomalyDetector.track(
            "getAllProducts",
            () -> performGET(APIEndpoints.GET_ALL_PRODUCTS)
        );

        logger.info("Verifying response status code...");
        SmartAssertionEngine.validate(response, "productsList")
            .assertStatusCode(200)
            .learnSchema()
            .assertAllFields()
            .detectAnomalies()
            .assertResponseTime(2000);

        String responseBody = ResponseHandler.getResponseAsString(response);
        Assert.assertTrue(responseBody.contains("products") || responseBody.contains("product"),
                "Response should contain products data");

        ResponseHandler.printResponseDetails(response);
        logger.info("✓ Test 1 PASSED: Get All Products List");
    }

    @Test(priority = 2)
    @Story("POST To All Products List")
    @Description("Test API 2: POST To All Products List - Should return 200")
    @Severity(CRITICAL)
    public void testPostToAllProductsList() {
        logger.info("========== TEST 2: POST To All Products List ==========");

        Response response = PerformanceAnomalyDetector.track(
            "postAllProducts",
            () -> performPOST(APIEndpoints.POST_ALL_PRODUCTS)
        );

        logger.info("Verifying response status code is 200...");
        SmartAssertionEngine.validate(response, "postProductsList")
            .assertStatusCode(200)
            .learnSchema()
            .detectAnomalies()
            .assertResponseTime(2000);

        ResponseHandler.printResponseDetails(response);
        logger.info("✓ Test 2 PASSED: POST To All Products List returns 200");
    }

    // ==================== BRANDS API TESTS ====================
    
    @Test(priority = 3)
    @Story("Get All Brands List")
    @Description("Test API 3: Get All Brands List - Should return 200 with brands list")
    @Severity(CRITICAL)
    public void testGetAllBrandsList() {
        logger.info("========== TEST 3: Get All Brands List ==========");

        Response response = PerformanceAnomalyDetector.track(
            "getAllBrands",
            () -> performGET(APIEndpoints.GET_ALL_BRANDS)
        );

        logger.info("Verifying response status code...");
        SmartAssertionEngine.validate(response, "brandsList")
            .assertStatusCode(200)
            .learnSchema()
            .assertAllFields()
            .detectAnomalies()
            .assertResponseTime(2000);

        String responseBody = ResponseHandler.getResponseAsString(response);
        Assert.assertTrue(responseBody.contains("brands") || responseBody.contains("brand"),
                "Response should contain brands data");

        ResponseHandler.printResponseDetails(response);
        logger.info("✓ Test 3 PASSED: Get All Brands List");
    }

    @Test(priority = 4)
    @Story("PUT To All Brands List")
    @Description("Test API 4: PUT To All Brands List - Should return 200")
    @Severity(CRITICAL)
    public void testPutToAllBrandsList() {
        logger.info("========== TEST 4: PUT To All Brands List ==========");

        Response response = PerformanceAnomalyDetector.track(
            "putAllBrands",
            () -> performPUT(APIEndpoints.PUT_ALL_BRANDS)
        );

        logger.info("Verifying response status code is 200...");
        SmartAssertionEngine.validate(response, "putBrandsList")
            .assertStatusCode(200)
            .learnSchema()
            .detectAnomalies()
            .assertResponseTime(2000);

        ResponseHandler.printResponseDetails(response);
        logger.info("✓ Test 4 PASSED: PUT To All Brands List returns 200");
    }

    // ==================== SEARCH PRODUCT API TESTS ====================
    
    @Test(priority = 5)
    @Story("Search Product With Parameter")
    @Description("Test API 5: POST To Search Product with search_product parameter - Should return 200")
    @Severity(CRITICAL)
    public void testSearchProductWithParameter() {
        logger.info("========== TEST 5: Search Product With Parameter ==========");

        Response response = PerformanceAnomalyDetector.track(
            "searchProduct",
            () -> performPOST(APIEndpoints.SEARCH_PRODUCT, "search_product", "top")
        );

        logger.info("Verifying response status code...");
        SmartAssertionEngine.validate(response, "searchProduct")
            .assertStatusCode(200)
            .learnSchema()
            .assertAllFields()
            .detectAnomalies()
            .assertResponseTime(1500);

        String responseBody = ResponseHandler.getResponseAsString(response);
        Assert.assertTrue(responseBody.contains("products") || responseBody.contains("product"),
                "Response should contain searched products data");

        ResponseHandler.printResponseDetails(response);
        logger.info("✓ Test 5 PASSED: Search Product With Parameter");
    }

    @Test(priority = 6)
    @Story("Search Product Without Parameter")
    @Description("Test API 6: POST To Search Product without search_product parameter - Should return 200")
    @Severity(CRITICAL)
    public void testSearchProductWithoutParameter() {
        logger.info("========== TEST 6: Search Product Without Parameter ==========");

        Response response = PerformanceAnomalyDetector.track(
            "searchProductNoParam",
            () -> performPOST(APIEndpoints.SEARCH_PRODUCT)
        );

        logger.info("Verifying response status code is 200...");
        SmartAssertionEngine.validate(response, "searchProductNoParam")
            .assertStatusCode(200)
            .learnSchema()
            .detectAnomalies()
            .assertResponseTime(1500);

        ResponseHandler.printResponseDetails(response);
        logger.info("✓ Test 6 PASSED: Search Product Without Parameter returns 200");
    }

    // ==================== LOGIN API TESTS ====================
    
    @Test(priority = 7)
    @Story("Verify Login With Valid Details")
    @Description("Test API 7: POST To Verify Login with valid details - Should return 200")
    @Severity(CRITICAL)
    public void testVerifyLoginWithValidDetails() {
        logger.info("========== TEST 7: Verify Login With Valid Details ==========");

        Response response = PerformanceAnomalyDetector.track(
            "verifyLogin",
            () -> performPOST(APIEndpoints.VERIFY_LOGIN,
                    "email", VALID_EMAIL,
                    "password", VALID_PASSWORD)
        );

        logger.info("Verifying response status code...");
        SmartAssertionEngine.validate(response, "userLogin")
            .assertStatusCode(200)
            .learnSchema()
            .assertAllFields()
            .detectAnomalies()
            .assertResponseTime(1000);

        ResponseHandler.printResponseDetails(response);
        logger.info("✓ Test 7 PASSED: Verify Login With Valid Details");
    }

    @Test(priority = 8)
    @Story("Verify Login Without Email Parameter")
    @Description("Test API 8: POST To Verify Login without email parameter - Should return 200")
    @Severity(CRITICAL)
    public void testVerifyLoginWithoutEmailParameter() {
        logger.info("========== TEST 8: Verify Login Without Email Parameter ==========");

        Response response = PerformanceAnomalyDetector.track(
            "verifyLoginNoEmail",
            () -> performPOST(APIEndpoints.VERIFY_LOGIN,
                    "password", VALID_PASSWORD)
        );

        logger.info("Verifying response status code is 200...");
        SmartAssertionEngine.validate(response, "verifyLoginNoEmail")
            .assertStatusCode(200)
            .learnSchema()
            .detectAnomalies()
            .assertResponseTime(1000);

        ResponseHandler.printResponseDetails(response);
        logger.info("✓ Test 8 PASSED: Verify Login Without Email Parameter returns 200");
    }

    @Test(priority = 9)
    @Story("DELETE Verify Login")
    @Description("Test API 9: DELETE To Verify Login - Should return 200")
    @Severity(CRITICAL)
    public void testDeleteVerifyLogin() {
        logger.info("========== TEST 9: DELETE Verify Login ==========");

        Response response = PerformanceAnomalyDetector.track(
            "deleteVerifyLogin",
            () -> performDELETE(APIEndpoints.VERIFY_LOGIN)
        );

        logger.info("Verifying response status code is 200...");
        SmartAssertionEngine.validate(response, "deleteVerifyLogin")
            .assertStatusCode(200)
            .learnSchema()
            .detectAnomalies()
            .assertResponseTime(1000);

        ResponseHandler.printResponseDetails(response);
        logger.info("✓ Test 9 PASSED: DELETE Verify Login returns 200");
    }

    @Test(priority = 10)
    @Story("Verify Login With Invalid Details")
    @Description("Test API 10: POST To Verify Login with invalid details - Should return 200")
    @Severity(CRITICAL)
    public void testVerifyLoginWithInvalidDetails() {
        logger.info("========== TEST 10: Verify Login With Invalid Details ==========");

        Response response = PerformanceAnomalyDetector.track(
            "loginInvalid",
            () -> performPOST(APIEndpoints.VERIFY_LOGIN,
                    "email", INVALID_EMAIL,
                    "password", INVALID_PASSWORD)
        );

        logger.info("Verifying response status code is 200...");
        SmartAssertionEngine.validate(response, "loginInvalid")
            .assertStatusCode(200)
            .learnSchema()
            .detectAnomalies()
            .assertResponseTime(1000);

        ResponseHandler.printResponseDetails(response);
        logger.info("✓ Test 10 PASSED: Verify Login With Invalid Details returns 200");
    }

    // ==================== USER ACCOUNT API TESTS ====================
    
    @Test(priority = 11)
    @Story("Create User Account")
    @Description("Test API 11: POST To Create/Register User Account - Should return 200")
    @Severity(CRITICAL)
    public void testCreateUserAccount() {
        logger.info("========== TEST 11: Create User Account ==========");

        String userEmail = "newuser" + System.currentTimeMillis() + "@example.com";
        
        Response response = PerformanceAnomalyDetector.track(
            "createAccount",
            () -> performPOST(APIEndpoints.CREATE_ACCOUNT,
                    "name", USER_NAME,
                    "email", userEmail,
                    "password", VALID_PASSWORD,
                    "title", TITLE,
                    "birth_date", BIRTH_DATE,
                    "birth_month", BIRTH_MONTH,
                    "birth_year", BIRTH_YEAR,
                    "firstname", FIRST_NAME,
                    "lastname", LAST_NAME,
                    "company", COMPANY,
                    "address1", ADDRESS1,
                    "address2", ADDRESS2,
                    "country", COUNTRY,
                    "state", STATE,
                    "city", CITY,
                    "zipcode", ZIPCODE,
                    "mobile_number", MOBILE_NUMBER)
        );

        logger.info("Verifying response status code...");
        SmartAssertionEngine.validate(response, "createAccount")
            .assertStatusCode(200)
            .learnSchema()
            .assertAllFields()
            .ignoreDynamicFields("user.id", "user.created_at", "user.token")
            .detectAnomalies()
            .assertResponseTime(2000);

        ResponseHandler.printResponseDetails(response);
        logger.info("✓ Test 11 PASSED: Create User Account returns 200");
    }

    @Test(priority = 12)
    @Story("Delete User Account")
    @Description("Test API 12: DELETE METHOD To Delete User Account - Should return 200")
    @Severity(CRITICAL)
    public void testDeleteUserAccount() {
        logger.info("========== TEST 12: Delete User Account ==========");

        String deleteEmail = "deleteuser" + System.currentTimeMillis() + "@example.com";
        
        // Create user first
        performPOST(APIEndpoints.CREATE_ACCOUNT,
                "name", USER_NAME,
                "email", deleteEmail,
                "password", VALID_PASSWORD,
                "title", TITLE,
                "birth_date", BIRTH_DATE,
                "birth_month", BIRTH_MONTH,
                "birth_year", BIRTH_YEAR,
                "firstname", FIRST_NAME,
                "lastname", LAST_NAME,
                "company", COMPANY,
                "address1", ADDRESS1,
                "address2", ADDRESS2,
                "country", COUNTRY,
                "state", STATE,
                "city", CITY,
                "zipcode", ZIPCODE,
                "mobile_number", MOBILE_NUMBER);

        Response response = PerformanceAnomalyDetector.track(
            "deleteAccount",
            () -> performDELETE(APIEndpoints.DELETE_ACCOUNT,
                    "email", deleteEmail,
                    "password", VALID_PASSWORD)
        );

        logger.info("Verifying response status code...");
        SmartAssertionEngine.validate(response, "deleteAccount")
            .assertStatusCode(200)
            .learnSchema()
            .assertAllFields()
            .detectAnomalies()
            .assertResponseTime(2000);

        String responseBody = ResponseHandler.getResponseAsString(response);
        Assert.assertTrue(responseBody.contains("Account deleted") || responseBody.contains("deleted"),
                "Response should contain 'Account deleted' message");

        ResponseHandler.printResponseDetails(response);
        logger.info("✓ Test 12 PASSED: Delete User Account returns 200");
    }

    @Test(priority = 13)
    @Story("Update User Account")
    @Description("Test API 13: PUT METHOD To Update User Account - Should return 200")
    @Severity(CRITICAL)
    public void testUpdateUserAccount() {
        logger.info("========== TEST 13: Update User Account ==========");

        String updateEmail = "updateuser" + System.currentTimeMillis() + "@example.com";
        
        // Create user first
        performPOST(APIEndpoints.CREATE_ACCOUNT,
                "name", USER_NAME,
                "email", updateEmail,
                "password", VALID_PASSWORD,
                "title", TITLE,
                "birth_date", BIRTH_DATE,
                "birth_month", BIRTH_MONTH,
                "birth_year", BIRTH_YEAR,
                "firstname", FIRST_NAME,
                "lastname", LAST_NAME,
                "company", COMPANY,
                "address1", ADDRESS1,
                "address2", ADDRESS2,
                "country", COUNTRY,
                "state", STATE,
                "city", CITY,
                "zipcode", ZIPCODE,
                "mobile_number", MOBILE_NUMBER);

        Response response = PerformanceAnomalyDetector.track(
            "updateAccount",
            () -> performPUT(APIEndpoints.UPDATE_ACCOUNT,
                    "name", "Updated User",
                    "email", updateEmail,
                    "password", VALID_PASSWORD,
                    "title", TITLE,
                    "birth_date", BIRTH_DATE,
                    "birth_month", BIRTH_MONTH,
                    "birth_year", BIRTH_YEAR,
                    "firstname", "Updated",
                    "lastname", LAST_NAME,
                    "company", COMPANY,
                    "address1", "Updated Address",
                    "address2", ADDRESS2,
                    "country", COUNTRY,
                    "state", STATE,
                    "city", CITY,
                    "zipcode", ZIPCODE,
                    "mobile_number", MOBILE_NUMBER)
        );

        logger.info("Verifying response status code...");
        SmartAssertionEngine.validate(response, "updateAccount")
            .assertStatusCode(200)
            .learnSchema()
            .assertAllFields()
            .detectAnomalies()
            .assertResponseTime(2000);

        String responseBody = ResponseHandler.getResponseAsString(response);
        Assert.assertTrue(responseBody.contains("User updated") || responseBody.contains("updated"),
                "Response should contain 'User updated' message");

        ResponseHandler.printResponseDetails(response);
        logger.info("✓ Test 13 PASSED: Update User Account returns 200");
    }

    @Test(priority = 14)
    @Story("Get User Detail By Email")
    @Description("Test API 14: GET user account detail by email - Should return 200")
    @Severity(CRITICAL)
    public void testGetUserDetailByEmail() {
        logger.info("========== TEST 14: Get User Detail By Email ==========");

        String detailEmail = "getdetail" + System.currentTimeMillis() + "@example.com";
        
        // Create user first
        performPOST(APIEndpoints.CREATE_ACCOUNT,
                "name", USER_NAME,
                "email", detailEmail,
                "password", VALID_PASSWORD,
                "title", TITLE,
                "birth_date", BIRTH_DATE,
                "birth_month", BIRTH_MONTH,
                "birth_year", BIRTH_YEAR,
                "firstname", FIRST_NAME,
                "lastname", LAST_NAME,
                "company", COMPANY,
                "address1", ADDRESS1,
                "address2", ADDRESS2,
                "country", COUNTRY,
                "state", STATE,
                "city", CITY,
                "zipcode", ZIPCODE,
                "mobile_number", MOBILE_NUMBER);

        Response response = PerformanceAnomalyDetector.track(
            "getUserDetail",
            () -> performGET(APIEndpoints.GET_USER_DETAIL_BY_EMAIL, "email", detailEmail)
        );

        logger.info("Verifying response status code...");
        SmartAssertionEngine.validate(response, "userAccount")
            .assertStatusCode(200)
            .learnSchema()
            .assertAllFields()
            .detectAnomalies()
            .assertResponseTime(1500);

        String responseBody = ResponseHandler.getResponseAsString(response);
        Assert.assertTrue(responseBody.contains("user") || responseBody.contains("email"),
                "Response should contain user details");

        ResponseHandler.printResponseDetails(response);
        logger.info("✓ Test 14 PASSED: Get User Detail By Email returns 200");
    }
    
    /**
     * Print comprehensive performance report after all tests
     */
    @AfterClass
    public void printAIPerformanceReport() {
        String separator = "================================================================================";
        System.out.println("\n" + separator);
        System.out.println("AI-POWERED API TESTING REPORT");
        System.out.println(separator);
        
        // Print performance statistics
        PerformanceAnomalyDetector.printPerformanceReport();
        
        System.out.println("\n[Summary]");
        System.out.println("✓ All 14 API endpoints tested with AI validation");
        System.out.println("✓ Response schemas learned and validated");
        System.out.println("✓ Performance anomalies detected and reported");
        System.out.println("✓ Baselines established for future comparisons");
        
        System.out.println("\n" + separator);
    }
}
