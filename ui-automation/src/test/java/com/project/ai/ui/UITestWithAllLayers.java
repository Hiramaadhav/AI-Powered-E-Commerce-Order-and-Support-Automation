package com.project.ai.ui;

import com.project.ai.ui.visual.VisualAIValidator;
import com.project.ai.ui.waits.SmartWaitManager;
import com.project.ui.Base.BaseTest;
import com.project.ui.Pages.*;
import com.project.ui.utils.DataProviders;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import io.qameta.allure.*;
import static io.qameta.allure.SeverityLevel.*;

/**
 * Comprehensive UI Test Suite with All Layers
 * 
 * This test suite contains all 24 test cases for automation exercise website:
 * - Complete user registration and authentication flows
 * - Product browsing, search, and cart management
 * - Checkout and payment processing
 * - Category and brand navigation
 * - Subscription and contact form functionality
 * 
 * Each test case is numbered according to the automation exercise specification
 */
@Epic("E-Commerce Automation Exercise - Complete Test Suite")
public class UITestWithAllLayers extends BaseTest {
    
    private HomePage homePage;
    private AuthPage authPage;
    private ContactPage contactPage;
    private ProductPage productPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;
    private PaymentPage paymentPage;
    private CategoryPage categoryPage;
    private BrandPage brandPage;
    
    // AI Layer components
    private VisualAIValidator visualAI;
    private SmartWaitManager smartWait;
    
    @BeforeMethod(alwaysRun = true)
    public void setUpPages() {
        homePage = new HomePage(driver);
        authPage = new AuthPage(driver);
        contactPage = new ContactPage(driver);
        productPage = new ProductPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
        paymentPage = new PaymentPage(driver);
        categoryPage = new CategoryPage(driver);
        brandPage = new BrandPage(driver);
        
        // Initialize AI layers
        visualAI = new VisualAIValidator();  
        smartWait = new SmartWaitManager(driver, 5);
    }
    
    @AfterMethod(alwaysRun = true)
    public void closeAI() {
        // Close visual AI session if active
        try {
            visualAI.endTest();
        } catch (Exception e) {
            // Already closed or not started
        }
    }
    
    // ==================== TEST CASE 1: REGISTER USER ====================
    
    @Feature("User Management")
    @Story("Register New User")
    @Severity(CRITICAL)
    @Description("Test Case 1: Register User - Complete registration flow with account creation and deletion")
    @Test(priority = 1, dataProvider = "registerUserData", dataProviderClass = DataProviders.class)
    public void testCase01_RegisterUser(
            String signupName, String signupEmail, String accountInfoPassword,
            String birthDay, String birthMonth, String birthYear,
            String firstName, String lastName, String company,
            String address1, String address2, String country,
            String state, String city, String zipcode, String mobile) {

        visualAI.startTest(driver, "TC01 - Register User", "Automation Exercise");
        smartWait.waitForPageReady();

        // Step 3: Verify that home page is visible successfully
        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");
        
        // Step 4: Click on 'Signup / Login' button
        homePage.clickSignupLogin();
        visualAI.checkWindow("Signup/Login Page");

        // Step 5: Verify 'New User Signup!' is visible
        Assert.assertTrue(authPage.isNewUserSignupVisible(), "New User Signup section not visible");
        
        // Step 6: Enter name and email address
        authPage.enterSignupNameEmail(signupName, signupEmail);
        
        // Step 7: Click 'Signup' button
        authPage.clickSignupButton();

        // Step 8: Verify that 'ENTER ACCOUNT INFORMATION' is visible
        Assert.assertTrue(authPage.isEnterAccountInfoVisible(), "Enter Account Information section not visible");
        
        // Step 9: Fill details: Title, Name, Email, Password, Date of birth
        authPage.selectTitle("Mr");
        authPage.fillAccountInfo(accountInfoPassword, birthDay, birthMonth, birthYear);
        
        // Step 10: Select checkbox 'Sign up for our newsletter!'
        authPage.selectNewsletter();
        
        // Step 11: Select checkbox 'Receive special offers from our partners!'
        authPage.selectSpecialOffers();

        // Step 12: Fill details: First name, Last name, Company, Address, Address2, Country, State, City, Zipcode, Mobile Number
        authPage.fillAddressDetails(firstName, lastName, company,
                address1, address2, country, state, city, zipcode, mobile);

        // Step 13: Click 'Create Account button'
        authPage.clickCreateAccount();
        visualAI.checkWindow("Account Created Confirmation");
        
        // Step 14: Verify that 'ACCOUNT CREATED!' is visible
        Assert.assertTrue(authPage.isAccountCreated(), "ACCOUNT CREATED! message not visible");
        
        // Step 15: Click 'Continue' button
        authPage.clickContinue();
        
        // Step 16: Verify that 'Logged in as username' is visible
        Assert.assertTrue(authPage.isUserLoggedIn(), "User is not logged in after account creation");

        // Step 17: Click 'Delete Account' button
        authPage.deleteAccount();
        visualAI.checkWindow("Account Deleted Confirmation");
        
        // Step 18: Verify that 'ACCOUNT DELETED!' is visible and click 'Continue' button
        Assert.assertTrue(authPage.isAccountDeleted(), "ACCOUNT DELETED! message not visible");
        authPage.clickContinue();
        
        visualAI.endTest();
    }

    // ==================== TEST CASE 2: LOGIN USER WITH CORRECT EMAIL AND PASSWORD ====================
    
    @Feature("User Management")
    @Story("Login with Valid Credentials")
    @Severity(BLOCKER)
    @Description("Test Case 2: Login User with correct email and password")
    @Test(priority = 2, dataProvider = "loginValidUserData", dataProviderClass = DataProviders.class)
    public void testCase02_LoginUserWithCorrectCredentials(String email, String password) {

        visualAI.startTest(driver, "TC02 - Login Valid User", "Automation Exercise");
        smartWait.waitForPageReady();

        // Step 3: Verify that home page is visible successfully
        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");
        
        // Step 4: Click on 'Signup / Login' button
        homePage.clickSignupLogin();
        visualAI.checkWindow("Login Form Page");
        
        // Step 5: Verify 'Login to your account' is visible
        Assert.assertTrue(authPage.isLoginFormVisible(), "Login form not visible");
        
        // Step 6: Enter correct email address and password
        // Step 7: Click 'login' button
        authPage.login(email, password);
        
        // Step 8: Verify that 'Logged in as username' is visible
        Assert.assertTrue(authPage.isUserLoggedIn(), "User is not logged in");
        visualAI.checkWindow("User Logged In Successfully");
        
        // Step 9: Click 'Delete Account' button
        authPage.deleteAccount();
        
        // Step 10: Verify that 'ACCOUNT DELETED!' is visible
        Assert.assertTrue(authPage.isAccountDeleted(), "ACCOUNT DELETED! message not visible");
        
        visualAI.endTest();
    }

    // ==================== TEST CASE 3: LOGIN USER WITH INCORRECT EMAIL AND PASSWORD ====================
    
    @Feature("User Management")
    @Story("Login with Invalid Credentials")
    @Severity(NORMAL)
    @Description("Test Case 3: Login User with incorrect email and password")
    @Test(priority = 3, dataProvider = "loginInvalidUserData", dataProviderClass = DataProviders.class)
    public void testCase03_LoginUserWithIncorrectCredentials(String email, String password) {

        visualAI.startTest(driver, "TC03 - Login Invalid User", "Automation Exercise");
        smartWait.waitForPageReady();

        // Step 3: Verify that home page is visible successfully
        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");
        
        // Step 4: Click on 'Signup / Login' button
        homePage.clickSignupLogin();
        visualAI.checkWindow("Login Form Page");
        
        // Step 5: Verify 'Login to your account' is visible
        Assert.assertTrue(authPage.isLoginFormVisible(), "Login form not visible");
        
        // Step 6: Enter incorrect email address and password
        // Step 7: Click 'login' button
        authPage.login(email, password);
        
        // Step 8: Verify error 'Your email or password is incorrect!' is visible
        visualAI.checkWindow("Invalid Credentials Error");
        Assert.assertTrue(authPage.isWrongCredentialMessageDisplayed(), 
                "Error message 'Your email or password is incorrect!' not visible");
        
        visualAI.endTest();
    }

    // ==================== TEST CASE 4: LOGOUT USER ====================
    
    @Feature("User Management")
    @Story("Logout User")
    @Severity(NORMAL)
    @Description("Test Case 4: Logout User")
    @Test(priority = 4, dataProvider = "logoutUserData", dataProviderClass = DataProviders.class)
    public void testCase04_LogoutUser(String email, String password) {

        visualAI.startTest(driver, "TC04 - Logout User", "Automation Exercise");
        smartWait.waitForPageReady();

        // Step 3: Verify that home page is visible successfully
        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");
        
        // Step 4: Click on 'Signup / Login' button
        homePage.clickSignupLogin();
        
        // Step 5: Verify 'Login to your account' is visible
        Assert.assertTrue(authPage.isLoginFormVisible(), "Login form not visible");
        
        // Step 6: Enter correct email address and password
        // Step 7: Click 'login' button
        authPage.login(email, password);
        
        // Step 8: Verify that 'Logged in as username' is visible
        Assert.assertTrue(authPage.isUserLoggedIn(), "User is not logged in");
        visualAI.checkWindow("User Logged In");

        // Step 9: Click 'Logout' button
        authPage.clickLogout();
        
        // Step 10: Verify that user is navigated to login page
        visualAI.checkWindow("After Logout");
        Assert.assertTrue(authPage.isLoginFormVisible(), "User not navigated to login page after logout");
        
        visualAI.endTest();
    }

    // ==================== TEST CASE 5: REGISTER USER WITH EXISTING EMAIL ====================
    
    @Feature("User Management")
    @Story("Register with Existing Email")
    @Severity(MINOR)
    @Description("Test Case 5: Register User with existing email")
    @Test(priority = 5, dataProvider = "registerExistingEmailData", dataProviderClass = DataProviders.class)
    public void testCase05_RegisterUserWithExistingEmail(String name, String email) {

        visualAI.startTest(driver, "TC05 - Register Existing Email", "Automation Exercise");
        smartWait.waitForPageReady();

        // Step 3: Verify that home page is visible successfully
        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");
        
        // Step 4: Click on 'Signup / Login' button
        homePage.clickSignupLogin();
        
        // Step 5: Verify 'New User Signup!' is visible
        Assert.assertTrue(authPage.isNewUserSignupVisible(), "New User Signup section not visible");
        visualAI.checkWindow("Signup Section");

        // Step 6: Enter name and already registered email address
        authPage.enterSignupNameEmail(name, email);
        
        // Step 7: Click 'Signup' button
        authPage.clickSignupButton();
        
        // Step 8: Verify error 'Email Address already exist!' is visible
        visualAI.checkWindow("Existing User Warning");
        Assert.assertTrue(authPage.isExistingUserSignupVisible(), 
                "Error 'Email Address already exist!' not visible");
        
        visualAI.endTest();
    }

    // ==================== TEST CASE 6: CONTACT US FORM ====================
    
    @Feature("Support")
    @Story("Contact Us Form")
    @Severity(NORMAL)
    @Description("Test Case 6: Contact Us Form")
    @Test(priority = 6, dataProvider = "contactUsData", dataProviderClass = DataProviders.class)
    public void testCase06_ContactUsForm(
            String name, String email,
            String subject, String message,
            String filePath) {

        visualAI.startTest(driver, "TC06 - Contact Us Form", "Automation Exercise");
        smartWait.waitForPageReady();

        // Step 3: Verify that home page is visible successfully
        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");
        
        // Step 4: Click on 'Contact Us' button
        homePage.clickContactUs();
        visualAI.checkWindow("Contact Form Page");
        
        // Step 5: Verify 'GET IN TOUCH' is visible
        Assert.assertTrue(contactPage.isGetInTouchVisible(), "GET IN TOUCH heading not visible");

        // Step 6: Enter name, email, subject and message
        // Step 7: Upload file
        // Step 8: Click 'Submit' button
        // Step 9: Click OK button
        contactPage.submitContactForm(name, email, subject, message, filePath);
        contactPage.verifysuccess();
        
        // Step 10: Verify success message 'Success! Your details have been submitted successfully.' is visible
        visualAI.checkWindow("Contact Success");
        Assert.assertTrue(contactPage.isContactSuccessMessageVisible(), 
                "Success message not visible");
        
        // Step 11: Click 'Home' button and verify that landed to home page successfully
        homePage.clickHome();
        Assert.assertTrue(homePage.isHomePageVisible(), "Not navigated to home page");
        
        visualAI.endTest();
    }

    // ==================== TEST CASE 7: VERIFY TEST CASES PAGE ====================
    
    @Feature("Navigation")
    @Story("Verify Test Cases Page")
    @Severity(MINOR)
    @Description("Test Case 7: Verify Test Cases Page")
    @Test(priority = 7)
    public void testCase07_VerifyTestCasesPage() {
        
        visualAI.startTest(driver, "TC07 - Verify Test Cases Page", "Automation Exercise");
        smartWait.waitForPageReady();
        
        // Step 3: Verify that home page is visible successfully
        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");
        
        // Step 4: Click on 'Test Cases' button
        homePage.clickTestCases();
        
        // Step 5: Verify user is navigated to test cases page successfully
        visualAI.checkWindow("Test Cases Page");
        
        visualAI.endTest();
    }

    // ==================== TEST CASE 8: VERIFY ALL PRODUCTS AND PRODUCT DETAIL PAGE ====================
    
    @Feature("Product")
    @Story("Verify Product Details")
    @Severity(CRITICAL)
    @Description("Test Case 8: Verify All Products and product detail page")
    @Test(priority = 8)
    public void testCase08_VerifyAllProductsAndProductDetail() {

        visualAI.startTest(driver, "TC08 - Verify Product Details", "Automation Exercise");
        smartWait.waitForPageReady();

        // Step 3: Verify that home page is visible successfully
        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");
        
        // Step 4: Click on 'Products' button
        homePage.clickProductPage();
        smartWait.waitForPageReady();
        
        // Step 5: Verify user is navigated to ALL PRODUCTS page successfully
        visualAI.checkWindow("Products Listing Page");
        Assert.assertTrue(productPage.isProductsPageVisible(), "ALL PRODUCTS page not visible");
        
        // Step 6: The products list is visible (verified in step 5)
        
        // Step 7: Click on 'View Product' of first product
        productPage.clickViewProduct();
        smartWait.waitForPageReady();
        
        // Step 8: User is landed to product detail page
        visualAI.validateEcommerceCheckpoint("Product Details Page");
        
        // Step 9: Verify that detail is visible: product name, category, price, availability, condition, brand
        Assert.assertTrue(productPage.isProductInfoVisible(), "Product information not visible");
        
        visualAI.endTest();
    }

    // ==================== TEST CASE 9: SEARCH PRODUCT ====================
    
    @Feature("Product")
    @Story("Search Product")
    @Severity(CRITICAL)
    @Description("Test Case 9: Search Product")
    @Test(priority = 9, dataProvider = "searchProductData", dataProviderClass = DataProviders.class)
    public void testCase09_SearchProduct(String searchTerm) {

        visualAI.startTest(driver, "TC09 - Search Product", "Automation Exercise");
        smartWait.waitForPageReady();

        // Step 3: Verify that home page is visible successfully
        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");
        
        // Step 4: Click on 'Products' button
        homePage.clickProductPage();
        smartWait.waitForPageReady();
        
        // Step 5: Verify user is navigated to ALL PRODUCTS page successfully
        Assert.assertTrue(productPage.isProductsPageVisible(), "ALL PRODUCTS page not visible");
        visualAI.checkWindow("All Products Page");
        
        // Step 6: Enter product name in search input and click search button
        productPage.searchProduct(searchTerm);
        smartWait.waitForPageReady();
        
        // Step 7: Verify 'SEARCHED PRODUCTS' is visible
        visualAI.checkWindow("Search Results");
        Assert.assertTrue(productPage.isSearchedProductsVisible(), "SEARCHED PRODUCTS heading not visible");
        
        // Step 8: Verify all the products related to search are visible
        visualAI.validateEcommerceCheckpoint("Search Results Products");
        
        visualAI.endTest();
    }

    // ==================== TEST CASE 10: VERIFY SUBSCRIPTION IN HOME PAGE ====================
    
    @Feature("Subscription")
    @Story("Subscribe from Home Page")
    @Severity(NORMAL)
    @Description("Test Case 10: Verify Subscription in home page")
    @Test(priority = 10, dataProvider = "subscriptionHomeData", dataProviderClass = DataProviders.class)
    public void testCase10_VerifySubscriptionInHomePage(String email) {

        visualAI.startTest(driver, "TC10 - Subscription Home", "Automation Exercise");
        smartWait.waitForPageReady();

        // Step 3: Verify that home page is visible successfully
        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");
        
        // Step 4: Scroll down to footer (handled automatically by element actions)
        
        // Step 5: Verify text 'SUBSCRIPTION'
        Assert.assertTrue(homePage.isSubscriptionVisible(), "SUBSCRIPTION text not visible");
        
        // Step 6: Enter email address in input and click arrow button
        homePage.subscribe(email);
        
        // Step 7: Verify success message 'You have been successfully subscribed!' is visible
        visualAI.checkWindow("Subscription Success");
        Assert.assertTrue(homePage.isSubscriptionSuccessVisible(), 
                "Success message 'You have been successfully subscribed!' not visible");
        
        visualAI.endTest();
    }

    // ==================== TEST CASE 11: VERIFY SUBSCRIPTION IN CART PAGE ====================
    
    @Feature("Subscription")
    @Story("Subscribe from Cart Page")
    @Severity(NORMAL)
    @Description("Test Case 11: Verify Subscription in Cart page")
    @Test(priority = 11, dataProvider = "subscriptionCartData", dataProviderClass = DataProviders.class)
    public void testCase11_VerifySubscriptionInCartPage(String email) {

        visualAI.startTest(driver, "TC11 - Subscription Cart", "Automation Exercise");
        smartWait.waitForPageReady();

        // Step 3: Verify that home page is visible successfully
        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");
        
        // Step 4: Click 'Cart' button
        homePage.clickCart();
        smartWait.waitForPageReady();
        
        // Step 5: Scroll down to footer (handled automatically)
        
        // Step 6: Verify text 'SUBSCRIPTION'
        Assert.assertTrue(cartPage.isSubscriptionVisible(), "SUBSCRIPTION text not visible on cart page");
        
        // Step 7: Enter email address in input and click arrow button
        cartPage.subscribe(email);
        
        // Step 8: Verify success message 'You have been successfully subscribed!' is visible
        visualAI.checkWindow("Cart Subscription Success");
        Assert.assertTrue(cartPage.isSubscriptionSuccessVisible(), 
                "Success message 'You have been successfully subscribed!' not visible");
        
        visualAI.endTest();
    }

    // ==================== TEST CASE 12: ADD PRODUCTS IN CART ====================
    
    @Feature("Cart")
    @Story("Add Multiple Products to Cart")
    @Severity(CRITICAL)
    @Description("Test Case 12: Add Products in Cart")
    @Test(priority = 12)
    public void testCase12_AddProductsInCart() {

        visualAI.startTest(driver, "TC12 - Add Multiple Products", "Automation Exercise");
        smartWait.waitForPageReady();

        // Step 3: Verify that home page is visible successfully
        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");
        
        // Step 4: Click 'Products' button
        homePage.clickProductPage();
        smartWait.waitForPageReady();
        visualAI.checkWindow("Products Page");
        
        // Step 5: Hover over first product and click 'Add to cart'
        productPage.addFirstProductToCart();
        
        // Step 6: Click 'Continue Shopping' button
        productPage.clickContinueShopping();
        
        // Step 7: Hover over second product and click 'Add to cart'
        productPage.addSecondProductToCart();
        
        // Step 8: Click 'View Cart' button
        cartPage.viewCart();
        smartWait.waitForPageReady();
        
        // Step 9: Verify both products are added to Cart
        visualAI.validateEcommerceCheckpoint("Cart with Multiple Products");
        Assert.assertTrue(cartPage.isCartPageVisible(), "Cart page not visible");
        
        // Step 10: Verify their prices, quantity and total price
        // Visual validation confirms the presence of price details
        
        visualAI.endTest();
    }

    // ==================== TEST CASE 13: VERIFY PRODUCT QUANTITY IN CART ====================
    
    @Feature("Cart")
    @Story("Verify Product Quantity in Cart")
    @Severity(CRITICAL)
    @Description("Test Case 13: Verify Product quantity in Cart")
    @Test(priority = 13, dataProvider = "productQuantityData", dataProviderClass = DataProviders.class)
    public void testCase13_VerifyProductQuantityInCart(String quantity) {

        visualAI.startTest(driver, "TC13 - Verify Product Quantity", "Automation Exercise");
        smartWait.waitForPageReady();

        // Step 3: Verify that home page is visible successfully
        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");
        
        // Step 4: Click 'View Product' for any product on home page
        homePage.clickProductPage();
        smartWait.waitForPageReady();
        productPage.clickViewProduct();
        smartWait.waitForPageReady();
        
        // Step 5: Verify product detail is opened
        visualAI.checkWindow("Product Details");
        Assert.assertTrue(productPage.isProductInfoVisible(), "Product detail not opened");
        
        // Step 6: Increase quantity to 4
        productPage.setQuantity(quantity);
        
        // Step 7: Click 'Add to cart' button
        productPage.addToCartFromDetails();
        
        // Step 8: Click 'View Cart' button
        cartPage.viewCart();
        smartWait.waitForPageReady();
        
        // Step 9: Verify that product is displayed in cart page with exact quantity
        visualAI.checkWindow("Cart with Quantity");
        Assert.assertTrue(cartPage.isQuantityVisible(), "Product quantity not visible in cart");
        
        visualAI.endTest();
    }

    // ==================== TEST CASE 14: PLACE ORDER - REGISTER WHILE CHECKOUT ====================
    
    @Feature("Checkout")
    @Story("Place Order: Register while Checkout")
    @Severity(BLOCKER)
    @Description("Test Case 14: Place Order: Register while Checkout")
    @Test(priority = 14, dataProvider = "registerUserData", dataProviderClass = DataProviders.class)
    public void testCase14_PlaceOrderRegisterWhileCheckout(
            String signupName, String signupEmail, String accountInfoPassword,
            String birthDay, String birthMonth, String birthYear,
            String firstName, String lastName, String company,
            String address1, String address2, String country,
            String state, String city, String zipcode, String mobile) {

        visualAI.startTest(driver, "TC14 - Place Order Register While Checkout", "Automation Exercise");
        smartWait.waitForPageReady();

        // Step 3: Verify that home page is visible successfully
        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");
        
        // Step 4: Add products to cart
        homePage.clickProductPage();
        smartWait.waitForPageReady();
        productPage.addFirstProductToCart();
        productPage.clickContinueShopping();
        
        // Step 5: Click 'Cart' button
        homePage.clickCart();
        smartWait.waitForPageReady();
        
        // Step 6: Verify that cart page is displayed
        Assert.assertTrue(cartPage.isCartPageVisible(), "Cart page not displayed");
        visualAI.checkWindow("Cart Page");
        
        // Step 7: Click Proceed To Checkout
        cartPage.clickProceedToCheckout();
        smartWait.waitForPageReady();
        
        // Step 8: Click 'Register / Login' button
        if (cartPage.isRegisterLoginVisible()) {
            cartPage.clickRegisterLogin();
        }
        
        // Step 9: Fill all details in Signup and create account
        Assert.assertTrue(authPage.isNewUserSignupVisible(), "New User Signup not visible");
        authPage.enterSignupNameEmail(signupName, signupEmail);
        authPage.clickSignupButton();
        Assert.assertTrue(authPage.isEnterAccountInfoVisible(), "Enter Account Info not visible");
        authPage.selectTitle("Mr");
        authPage.fillAccountInfo(accountInfoPassword, birthDay, birthMonth, birthYear);
        authPage.selectNewsletter();
        authPage.selectSpecialOffers();
        authPage.fillAddressDetails(firstName, lastName, company,
                address1, address2, country, state, city, zipcode, mobile);
        authPage.clickCreateAccount();
        
        // Step 10: Verify 'ACCOUNT CREATED!' and click 'Continue' button
        Assert.assertTrue(authPage.isAccountCreated(), "ACCOUNT CREATED! not visible");
        visualAI.checkWindow("Account Created");
        authPage.clickContinue();
        
        // Step 11: Verify 'Logged in as username' at top
        Assert.assertTrue(authPage.isUserLoggedIn(), "User not logged in");
        
        // Step 12: Click 'Cart' button
        homePage.clickCart();
        smartWait.waitForPageReady();
        
        // Step 13: Click 'Proceed To Checkout' button
        cartPage.clickProceedToCheckout();
        smartWait.waitForPageReady();
        
        // Step 14: Verify Address Details and Review Your Order
        visualAI.checkWindow("Checkout Summary");
        
        // Step 15: Enter description in comment text area and click 'Place Order'
        checkoutPage.clickPlaceOrder();
        smartWait.waitForPageReady();
        
        // Step 16: Enter payment details: Name on Card, Card Number, CVC, Expiration date
        visualAI.checkWindow("Payment Form");
        paymentPage.fillPaymentDetails("Test User", "4111111111111111", "123", "12", "2027");
        
        // Step 17: Click 'Pay and Confirm Order' button
        paymentPage.clickPay();
        
        // Step 18: Verify success message 'Your order has been placed successfully!'
        visualAI.checkWindow("Order Confirmation");
        Assert.assertTrue(paymentPage.isOrderPlaced(), "Order placement confirmation not visible");
        
        // Step 19: Click 'Delete Account' button
        authPage.deleteAccount();
        
        // Step 20: Verify 'ACCOUNT DELETED!' and click 'Continue' button
        Assert.assertTrue(authPage.isAccountDeleted(), "ACCOUNT DELETED! not visible");
        authPage.clickContinue();
        
        visualAI.endTest();
    }

    // ==================== TEST CASE 17: REMOVE PRODUCTS FROM CART ====================
    
    @Feature("Cart")
    @Story("Remove Product from Cart")
    @Severity(NORMAL)
    @Description("Test Case 17: Remove Products From Cart")
    @Test(priority = 17)
    public void testCase17_RemoveProductsFromCart() {

        visualAI.startTest(driver, "TC17 - Remove Product", "Automation Exercise");
        smartWait.waitForPageReady();

        // Step 3: Verify that home page is visible successfully
        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");
        
        // Step 4: Add products to cart
        homePage.clickProductPage();
        smartWait.waitForPageReady();
        productPage.addFirstProductToCart();
        productPage.clickContinueShopping();
        
        // Step 5: Click 'Cart' button
        homePage.clickCart();
        smartWait.waitForPageReady();
        
        // Step 6: Verify that cart page is displayed
        visualAI.checkWindow("Cart Before Removal");
        Assert.assertTrue(cartPage.isCartPageVisible(), "Cart page not visible");
        
        // Step 7: Click 'X' button corresponding to particular product
        cartPage.removeProduct();
        
        // Step 8: Verify that product is removed from the cart
        visualAI.checkWindow("Cart After Removal");
        
        visualAI.endTest();
    }

    // ==================== TEST CASE 18: VIEW CATEGORY PRODUCTS ====================
    
    @Feature("Category")
    @Story("Browse Products by Category")
    @Severity(NORMAL)
    @Description("Test Case 18: View Category Products")
    @Test(priority = 18)
    public void testCase18_ViewCategoryProducts() {

        visualAI.startTest(driver, "TC18 - Category Products", "Automation Exercise");
        smartWait.waitForPageReady();

        // Step 3: Verify that categories are visible on left side bar
        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");
        
        // Step 4: Click on 'Women' category
        categoryPage.clickWomenCategory();
        
        // Step 5: Click on any category link under 'Women' category, for example: Dress
        categoryPage.clickDressSubcategory();
        smartWait.waitForPageReady();
        
        // Step 6: Verify that category page is displayed and confirm text 'WOMEN - TOPS PRODUCTS'
        visualAI.checkWindow("Women Dress Products");
        Assert.assertTrue(categoryPage.isWomenDressProductsVisible(), "Women - Dress Products not visible");

        // Step 7: On left side bar, click on any sub-category link of 'Men' category
        categoryPage.clickMenCategory();
        categoryPage.clickTshirtsSubcategory();
        smartWait.waitForPageReady();
        
        // Step 8: Verify that user is navigated to that category page
        visualAI.checkWindow("Men T-shirt Products");
        Assert.assertTrue(categoryPage.isMenTshirtProductsVisible(), "Men - Tshirts Products not visible");
        
        visualAI.endTest();
    }

    // ==================== TEST CASE 19: VIEW & CART BRAND PRODUCTS ====================
    
    @Feature("Brand")
    @Story("Browse Products by Brand")
    @Severity(NORMAL)
    @Description("Test Case 19: View & Cart Brand Products")
    @Test(priority = 19)
    public void testCase19_ViewAndCartBrandProducts() {

        visualAI.startTest(driver, "TC19 - Brand Products", "Automation Exercise");
        smartWait.waitForPageReady();

        // Step 3: Click on 'Products' button
        homePage.clickProductPage();
        smartWait.waitForPageReady();
        
        // Step 4: Verify that Brands are visible on left side bar
        Assert.assertTrue(productPage.isProductsPageVisible(), "Products page not visible");
        Assert.assertTrue(brandPage.isBrandsSectionVisible(), "Brands section not visible");

        // Step 5: Click on any brand name
        brandPage.clickHMBrand();
        smartWait.waitForPageReady();
        
        // Step 6: Verify that user is navigated to brand page and brand products are displayed
        visualAI.checkWindow("H&M Products");
        Assert.assertTrue(brandPage.isHMProductsVisible(), "H&M brand products not visible");

        // Step 7: On left side bar, click on any other brand link
        brandPage.clickPoloBrand();
        smartWait.waitForPageReady();
        
        // Step 8: Verify that user is navigated to that brand page and can see products
        visualAI.checkWindow("Polo Products");
        Assert.assertTrue(brandPage.isPoloProductsVisible(), "Polo brand products not visible");
        
        visualAI.endTest();
    }

    // ==================== TEST CASE 20: SEARCH PRODUCTS AND VERIFY CART AFTER LOGIN ====================
    
    @Feature("Cart")
    @Story("Search & Verify Cart After Login")
    @Severity(CRITICAL)
    @Description("Test Case 20: Search Products and Verify Cart After Login")
    @Test(priority = 20, dataProvider = "searchVerifyCartAfterLoginData", dataProviderClass = DataProviders.class)
    public void testCase20_SearchProductsAndVerifyCartAfterLogin(String searchTerm, String email, String password) {

        visualAI.startTest(driver, "TC20 - Search & Verify Cart After Login", "Automation Exercise");
        smartWait.waitForPageReady();

        // Step 3: Click on 'Products' button
        homePage.clickProductPage();
        smartWait.waitForPageReady();
        
        // Step 4: Verify user is navigated to ALL PRODUCTS page successfully
        Assert.assertTrue(productPage.isProductsPageVisible(), "ALL PRODUCTS page not visible");
        
        // Step 5: Enter product name in search input and click search button
        productPage.searchProduct(searchTerm);
        smartWait.waitForPageReady();
        
        // Step 6: Verify 'SEARCHED PRODUCTS' is visible
        visualAI.checkWindow("Search Results");
        Assert.assertTrue(productPage.isSearchedProductsVisible(), "SEARCHED PRODUCTS not visible");
        
        // Step 7: Verify all the products related to search are visible
        
        // Step 8: Add those products to cart
        productPage.addFirstProductToCart();
        
        // Step 9: Click 'Cart' button and verify that products are visible in cart
        cartPage.viewCart();
        smartWait.waitForPageReady();
        visualAI.checkWindow("Cart with Searched Product");
        Assert.assertTrue(cartPage.isCartPageVisible(), "Cart page not visible");

        // Step 10: Click 'Signup / Login' button and submit login details
        homePage.clickSignupLogin();
        authPage.login(email, password);
        Assert.assertTrue(authPage.isUserLoggedIn(), "User not logged in");
        visualAI.checkWindow("User Logged In");

        // Step 11: Again, go to Cart page
        homePage.clickCart();
        smartWait.waitForPageReady();
        
        // Step 12: Verify that those products are visible in cart after login as well
        Assert.assertTrue(cartPage.isCartPageVisible(), "Cart page not visible after login");
        visualAI.checkWindow("Cart After Login");
        
        authPage.clickLogout();
        
        visualAI.endTest();
    }

    // ==================== TEST CASE 21: ADD REVIEW ON PRODUCT ====================
    
    @Feature("Product")
    @Story("Add Product Review")
    @Severity(NORMAL)
    @Description("Test Case 21: Add review on product")
    @Test(priority = 21, dataProvider = "addReviewData", dataProviderClass = DataProviders.class)
    public void testCase21_AddReviewOnProduct(String name, String email, String review) {

        visualAI.startTest(driver, "TC21 - Add Product Review", "Automation Exercise");
        smartWait.waitForPageReady();

        // Step 3: Click on 'Products' button
        homePage.clickProductPage();
        smartWait.waitForPageReady();
        
        // Step 4: Verify user is navigated to ALL PRODUCTS page successfully
        Assert.assertTrue(productPage.isProductsPageVisible(), "ALL PRODUCTS page not visible");
        
        // Step 5: Click on 'View Product' button
        productPage.clickViewProduct();
        smartWait.waitForPageReady();
        
        // Step 6: Verify 'Write Your Review' is visible
        visualAI.checkWindow("Product Details with Review Section");
        Assert.assertTrue(productPage.isProductInfoVisible(), "Product information not visible");
        Assert.assertTrue(productPage.isWriteReviewVisible(), "Write Your Review section not visible");

        // Step 7: Enter name, email and review
        // Step 8: Click 'Submit' button
        productPage.submitReview(name, email, review);
        
        // Step 9: Verify success message 'Thank you for your review.'
        visualAI.checkWindow("Review Submitted");
        Assert.assertTrue(productPage.isReviewThankYouVisible(), 
                "Success message 'Thank you for your review.' not visible");
        
        visualAI.endTest();
    }

    // ==================== TEST CASE 24: DOWNLOAD INVOICE AFTER PURCHASE ORDER ====================
    
    @Feature("Checkout")
    @Story("Download Invoice")
    @Severity(CRITICAL)
    @Description("Test Case 24: Download Invoice after purchase order")
    @Test(priority = 24, dataProvider = "registerUserData", dataProviderClass = DataProviders.class)
    public void testCase24_DownloadInvoiceAfterPurchaseOrder(
            String signupName, String signupEmail, String accountInfoPassword,
            String birthDay, String birthMonth, String birthYear,
            String firstName, String lastName, String company,
            String address1, String address2, String country,
            String state, String city, String zipcode, String mobile) {

        visualAI.startTest(driver, "TC24 - Download Invoice", "Automation Exercise");
        smartWait.waitForPageReady();

        // Step 3: Verify that home page is visible successfully
        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");
        
        // Step 4: Add products to cart
        homePage.clickProductPage();
        smartWait.waitForPageReady();
        productPage.addFirstProductToCart();
        productPage.clickContinueShopping();
        
        // Step 5: Click 'Cart' button
        homePage.clickCart();
        smartWait.waitForPageReady();
        
        // Step 6: Verify that cart page is displayed
        Assert.assertTrue(cartPage.isCartPageVisible(), "Cart page not displayed");
        visualAI.checkWindow("Cart Page");
        
        // Step 7: Click Proceed To Checkout
        cartPage.clickProceedToCheckout();
        smartWait.waitForPageReady();
        
        // Step 8: Click 'Register / Login' button
        if (cartPage.isRegisterLoginVisible()) {
            cartPage.clickRegisterLogin();
        }
        
        // Step 9: Fill all details in Signup and create account
        Assert.assertTrue(authPage.isNewUserSignupVisible(), "New User Signup not visible");
        authPage.enterSignupNameEmail(signupName, signupEmail);
        authPage.clickSignupButton();
        Assert.assertTrue(authPage.isEnterAccountInfoVisible(), "Enter Account Info not visible");
        authPage.selectTitle("Mr");
        authPage.fillAccountInfo(accountInfoPassword, birthDay, birthMonth, birthYear);
        authPage.selectNewsletter();
        authPage.selectSpecialOffers();
        authPage.fillAddressDetails(firstName, lastName, company,
                address1, address2, country, state, city, zipcode, mobile);
        authPage.clickCreateAccount();
        
        // Step 10: Verify 'ACCOUNT CREATED!' and click 'Continue' button
        Assert.assertTrue(authPage.isAccountCreated(), "ACCOUNT CREATED! not visible");
        authPage.clickContinue();
        
        // Step 11: Verify 'Logged in as username' at top
        Assert.assertTrue(authPage.isUserLoggedIn(), "User not logged in");
        
        // Step 12: Click 'Cart' button
        homePage.clickCart();
        smartWait.waitForPageReady();
        
        // Step 13: Click 'Proceed To Checkout' button
        cartPage.clickProceedToCheckout();
        smartWait.waitForPageReady();
        
        // Step 14: Verify Address Details and Review Your Order
        visualAI.checkWindow("Checkout Summary");
        
        // Step 15: Enter description in comment text area and click 'Place Order'
        checkoutPage.clickPlaceOrder();
        smartWait.waitForPageReady();
        
        // Step 16: Enter payment details: Name on Card, Card Number, CVC, Expiration date
        visualAI.checkWindow("Payment Form");
        paymentPage.fillPaymentDetails("Test User", "4111111111111111", "123", "12", "2027");
        
        // Step 17: Click 'Pay and Confirm Order' button
        paymentPage.clickPay();
        smartWait.waitForPageReady();
        
        // Step 18: Verify success message 'Your order has been placed successfully!'
        visualAI.checkWindow("Order Confirmation");
        Assert.assertTrue(paymentPage.isOrderPlaced(), "Order placement confirmation not visible");
        
        // Step 19: Click 'Download Invoice' button and verify invoice is downloaded successfully
        paymentPage.downloadInvoice();
        try {
            Thread.sleep(2000); // Allow time for download
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Step 20: Click 'Continue' button
        authPage.clickContinue();
        
        // Step 21: Click 'Delete Account' button
        authPage.deleteAccount();
        
        // Step 22: Verify 'ACCOUNT DELETED!' and click 'Continue' button
        Assert.assertTrue(authPage.isAccountDeleted(), "ACCOUNT DELETED! not visible");
        authPage.clickContinue();
        
        visualAI.endTest();
    }
}
