package com.project.api.Endpoints;

/**
 * APIEndpoints class to define all API endpoints
 */
public class APIEndpoints {

    // Base URL
    public static final String BASE_URL = "https://automationexercise.com/api";

    // Product APIs
    public static final String GET_ALL_PRODUCTS = BASE_URL + "/productsList";
    public static final String POST_ALL_PRODUCTS = BASE_URL + "/productsList";

    // Brand APIs
    public static final String GET_ALL_BRANDS = BASE_URL + "/brandsList";
    public static final String PUT_ALL_BRANDS = BASE_URL + "/brandsList";

    // Search Product APIs
    public static final String SEARCH_PRODUCT = BASE_URL + "/searchProduct";

    // User Login APIs
    public static final String VERIFY_LOGIN = BASE_URL + "/verifyLogin";

    // User Account APIs
    public static final String CREATE_ACCOUNT = BASE_URL + "/createAccount";
    public static final String DELETE_ACCOUNT = BASE_URL + "/deleteAccount";
    public static final String UPDATE_ACCOUNT = BASE_URL + "/updateAccount";
    public static final String GET_USER_DETAIL_BY_EMAIL = BASE_URL + "/getUserDetailByEmail";
}
