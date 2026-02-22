package com.project.ai.ui;

import com.project.ai.ui.visual.VisualAIValidator;
import com.project.ai.ui.waits.SmartWaitManager;
import com.project.ui.Base.BaseTest;
import com.project.ui.Pages.*;
import com.project.ui.utils.DataProviders;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import io.qameta.allure.*;
import static io.qameta.allure.SeverityLevel.*;

/**
 * AI-Powered E-Commerce UI Test Suite
 * 
 * This test suite integrates AI capabilities with all business test cases:
 * 1. Visual AI validation with Applitools (cross-browser/device testing)
 * 2. Open-source visual testing with OpenCV (local baseline comparison)
 * 3. Smart wait management (eliminates flaky tests, learns optimal wait times)
 * 4. All actual business test cases with AI enhancements
 */
@Epic("AI Powered E-Commerce Order and Support Automation")
public class UITestWithAILayers extends BaseTest {
    
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
    
    @BeforeClass
    public void setUpAI() {
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
        smartWait = new SmartWaitManager(driver, 30);
    }
    
    @AfterClass
    public void closeAI() {
        // Close visual AI session if active
        try {
            visualAI.endTest();
        } catch (Exception e) {
            // Already closed or not started
        }
    }
    
    @Feature("User Management")
    @Story("Register User")
    @Severity(CRITICAL)
    @Description("Verify that a new user can register successfully and delete account")
    @Test(priority = 0, dataProvider = "registerUserData", dataProviderClass = DataProviders.class)
    public void TC01_RegisterUser(
            String signupName, String signupEmail, String accountInfoPassword,
            String birthDay, String birthMonth, String birthYear,
            String firstName, String lastName, String company,
            String address1, String address2, String country,
            String state, String city, String zipcode, String mobile) {

        visualAI.startTest(driver, "TC01 - Register User", "E-Commerce App");
        smartWait.waitForPageReady();

        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");
        homePage.clickSignupLogin();
        visualAI.checkWindow("Signup/Login Page");

        Assert.assertTrue(authPage.isNewUserSignupVisible(), "New user signup section not visible");
        authPage.enterSignupNameEmail(signupName, signupEmail);
        authPage.clickSignupButton();

        Assert.assertTrue(authPage.isEnterAccountInfoVisible(), "Account info section not visible");
        authPage.fillAccountInfo(accountInfoPassword, birthDay, birthMonth, birthYear);
        authPage.selectNewsletter();
        authPage.selectSpecialOffers();

        authPage.fillAddressDetails(firstName, lastName, company,
                address1, address2, country, state, city, zipcode, mobile);

        authPage.clickCreateAccount();
        visualAI.checkWindow("Account Created Confirmation");
        Assert.assertTrue(authPage.isAccountCreated(), "Account created message not visible");
        authPage.clickContinue();
        Assert.assertTrue(authPage.isUserLoggedIn(), "User is not logged in after account creation");

        authPage.deleteAccount();
        visualAI.checkWindow("Account Deleted Confirmation");
        Assert.assertTrue(authPage.isAccountDeleted(), "Account deleted message not visible");
        authPage.clickContinue();
        
        visualAI.endTest();
    }

    @Feature("User Management")
    @Story("Login with Valid Credentials")
    @Severity(BLOCKER)
    @Description("Verify user can login with valid credentials")
    @Test(priority = 1, dataProvider = "loginValidUserData", dataProviderClass = DataProviders.class)
    public void TC02_LoginValidUser(String email, String password) {

        visualAI.startTest(driver, "TC02 - Login Valid User", "E-Commerce App");
        smartWait.waitForPageReady();

        homePage.clickSignupLogin();
        visualAI.checkWindow("Login Form Page");
        Assert.assertTrue(authPage.isLoginFormVisible(), "Login form not visible");
        authPage.login(email, password);
        Assert.assertTrue(authPage.isUserLoggedIn(), "User is not logged in");
        visualAI.checkWindow("User Logged In Successfully");
        
        visualAI.endTest();
    }

    @Feature("User Management")
    @Story("Login with Invalid Credentials")
    @Severity(NORMAL)
    @Description("Verify error message appears when invalid credentials are used")
    @Test(priority = 2, dataProvider = "loginInvalidUserData", dataProviderClass = DataProviders.class)
    public void TC03_LoginInvalidUser(String email, String password) {

        visualAI.startTest(driver, "TC03 - Login Invalid User", "E-Commerce App");
        smartWait.waitForPageReady();

        homePage.clickSignupLogin();
        visualAI.checkWindow("Login Form Page");
        Assert.assertTrue(authPage.isLoginFormVisible(), "Login form not visible");
        authPage.login(email, password);
        visualAI.checkWindow("Invalid Credentials Error");
        Assert.assertTrue(authPage.isWrongCredentialMessageDisplayed(), "Wrong credentials warning not visible");
        
        visualAI.endTest();
    }

    @Feature("User Management")
    @Story("Logout User")
    @Severity(NORMAL)
    @Description("Verify user can logout successfully")
    @Test(priority = 3, dataProvider = "logoutUserData", dataProviderClass = DataProviders.class)
    public void TC04_LogoutUser(String email, String password) {

        visualAI.startTest(driver, "TC04 - Logout User", "E-Commerce App");
        smartWait.waitForPageReady();

        homePage.clickSignupLogin();
        authPage.login(email, password);
        Assert.assertTrue(authPage.isUserLoggedIn(), "User is not logged in");
        visualAI.checkWindow("User Logged In");

        authPage.clickLogout();
        visualAI.checkWindow("After Logout");
        Assert.assertTrue(authPage.isLoginFormVisible(), "Login form not visible after logout");
        
        visualAI.endTest();
    }

    @Feature("User Management")
    @Story("Register Existing Email")
    @Severity(MINOR)
    @Description("Verify warning is shown when registering with existing email")
    @Test(priority = 4, dataProvider = "registerExistingEmailData", dataProviderClass = DataProviders.class)
    public void TC05_RegisterExistingEmail(String name, String email) {

        visualAI.startTest(driver, "TC05 - Register Existing Email", "E-Commerce App");
        smartWait.waitForPageReady();

        homePage.clickHome();
        homePage.clickSignupLogin();
        Assert.assertTrue(authPage.isNewUserSignupVisible(), "New user signup section not visible");
        visualAI.checkWindow("Signup Section");

        authPage.enterSignupNameEmail(name, email);
        authPage.clickSignupButton();
        visualAI.checkWindow("Existing User Warning");
        Assert.assertTrue(authPage.isExistingUserSignupVisible(), "Existing user warning not visible");
        
        visualAI.endTest();
    }

    @Feature("Support")
    @Story("Contact Us Form")
    @Severity(NORMAL)
    @Description("Verify user can submit contact us form")
    @Test(priority = 5, dataProvider = "contactUsData", dataProviderClass = DataProviders.class)
    public void TC06_ContactUsForm(
            String name, String email,
            String subject, String message,
            String filePath) {

        visualAI.startTest(driver, "TC06 - Contact Us Form", "E-Commerce App");
        smartWait.waitForPageReady();

        homePage.clickHome();
        homePage.clickContactUs();
        visualAI.checkWindow("Contact Form Page");
        Assert.assertTrue(contactPage.isGetInTouchVisible(), "Contact form not visible");

        contactPage.submitContactForm(name, email, subject, message, filePath);
        visualAI.checkWindow("Contact Success");
        Assert.assertTrue(contactPage.isContactSuccessMessageVisible(), "Contact success message not visible");
        homePage.clickHome();
        
        visualAI.endTest();
    }

    @Feature("Navigation")
    @Story("Verify Test Cases Page")
    @Severity(MINOR)
    @Description("Verify Test Cases page is accessible")
    @Test(priority = 6)
    public void TC07_VerifyTestCasesPage() {
        
        visualAI.startTest(driver, "TC07 - Verify Test Cases Page", "E-Commerce App");
        smartWait.waitForPageReady();
        
        homePage.clickTestCases();
        visualAI.checkWindow("Test Cases Page");
        visualAI.endTest();
    }

    @Feature("Product")
    @Story("Verify Product Details")
    @Severity(CRITICAL)
    @Description("Verify product details page displays correct information")
    @Test(priority = 7)
    public void TC08_VerifyProductDetails() {

        visualAI.startTest(driver, "TC08 - Verify Product Details", "E-Commerce App");
        smartWait.waitForPageReady();

        homePage.clickProductPage();
        visualAI.checkWindow("Products Listing Page");
        Assert.assertTrue(productPage.isProductsPageVisible(), "Products page not visible");
        productPage.clickViewProduct();
        visualAI.validateEcommerceCheckpoint("Product Details Page");
        Assert.assertTrue(productPage.isProductInfoVisible(), "Product information not visible");
        
        visualAI.endTest();
    }

    @Feature("Product")
    @Story("Search Product")
    @Severity(CRITICAL)
    @Description("Verify search functionality works properly")
    @Test(priority = 8, dataProvider = "searchProductData", dataProviderClass = DataProviders.class)
    public void TC09_SearchProduct(String searchTerm) {

        visualAI.startTest(driver, "TC09 - Search Product", "E-Commerce App");
        smartWait.waitForPageReady();

        homePage.clickProductPage();
        Assert.assertTrue(productPage.isProductsPageVisible(), "Products page not visible");
        productPage.searchProduct(searchTerm);
        visualAI.checkWindow("Search Results");
        Assert.assertTrue(productPage.isSearchedProductsVisible(), "Searched products not visible");
        
        visualAI.endTest();
    }

    @Feature("Subscription")
    @Story("Subscribe from Home Page")
    @Severity(NORMAL)
    @Description("Verify user can subscribe from home page")
    @Test(priority = 9, dataProvider = "subscriptionHomeData", dataProviderClass = DataProviders.class)
    public void TC10_SubscriptionHome(String email) {

        visualAI.startTest(driver, "TC10 - Subscription Home", "E-Commerce App");
        smartWait.waitForPageReady();

        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");
        Assert.assertTrue(homePage.isSubscriptionVisible(), "Subscription section not visible");
        homePage.subscribe(email);
        visualAI.checkWindow("Subscription Success");
        visualAI.endTest();
    }

    @Feature("Subscription")
    @Story("Subscribe from Cart Page")
    @Severity(NORMAL)
    @Description("Verify user can subscribe from cart page")
    @Test(priority = 10, dataProvider = "subscriptionCartData", dataProviderClass = DataProviders.class)
    public void TC11_SubscriptionCart(String email) {

        visualAI.startTest(driver, "TC11 - Subscription Cart", "E-Commerce App");
        smartWait.waitForPageReady();

        homePage.clickCart();
        smartWait.waitForPageReady();
        Assert.assertTrue(cartPage.isSubscriptionVisible(), "Cart subscription section not visible");
        cartPage.subscribe(email);
        visualAI.checkWindow("Cart Subscription Success");
        Assert.assertTrue(cartPage.isSubscriptionSuccessVisible(), "Cart subscription success not visible");
        
        visualAI.endTest();
    }

    @Feature("Cart")
    @Story("Add Multiple Products to Cart")
    @Severity(CRITICAL)
    @Description("Verify user can add multiple products to cart")
    @Test(priority = 11)
    public void TC12_AddProductsToCart() {

        visualAI.startTest(driver, "TC12 - Add Multiple Products to Cart", "E-Commerce App");
        smartWait.waitForPageReady();

        homePage.clickProductPage();
        smartWait.waitForPageReady();
        visualAI.checkWindow("Products Page");
        productPage.addFirstProductToCart();
        productPage.clickContinueShopping();
        productPage.addSecondProductToCart();
        cartPage.viewCart();
        visualAI.validateEcommerceCheckpoint("Cart Page with Multiple Products");
        Assert.assertTrue(cartPage.isCartPageVisible(), "Cart page not visible");
        
        visualAI.endTest();
    }

    @Feature("Cart")
    @Story("Verify Product Quantity in Cart")
    @Severity(CRITICAL)
    @Description("Verify custom quantity is reflected correctly in cart")
    @Test(priority = 12, dataProvider = "productQuantityData", dataProviderClass = DataProviders.class)
    public void TC13_VerifyProductQuantity(String quantity) {

        visualAI.startTest(driver, "TC13 - Verify Product Quantity", "E-Commerce App");
        smartWait.waitForPageReady();

        homePage.clickProductPage();
        smartWait.waitForPageReady();
        productPage.clickViewProduct();
        visualAI.checkWindow("Product Details");
        Assert.assertTrue(productPage.isProductInfoVisible(), "Product information not visible");
        productPage.setQuantity(quantity);
        productPage.addToCartFromDetails();
        cartPage.viewCart();
        smartWait.waitForPageReady();
        visualAI.checkWindow("Cart with Quantity");
        Assert.assertTrue(cartPage.isQuantityVisible(), "Product quantity not visible in cart");
        
        visualAI.endTest();
    }

    @Feature("Checkout")
    @Story("Place Order After Login")
    @Severity(BLOCKER)
    @Description("Verify user can place order after login")
    @Test(priority = 13, dataProvider = "placeOrderAfterLoginData", dataProviderClass = DataProviders.class)
    public void TC14_PlaceOrderAfterLogin(
            String email, String password,
            String nameOnCard, String cardNumber,
            String cvc, String expiryMonth,
            String expiryYear) {

        visualAI.startTest(driver, "TC14 - Place Order After Login", "E-Commerce App");
        smartWait.waitForPageReady();

        homePage.clickProductPage();
        smartWait.waitForPageReady();
        productPage.addFirstProductToCart();
        cartPage.viewCart();
        smartWait.waitForPageReady();
        visualAI.checkWindow("Cart Page");

        cartPage.clickProceedToCheckout();
        if (cartPage.isRegisterLoginVisible()) {
            cartPage.clickRegisterLogin();
        }

        homePage.clickSignupLogin();
        authPage.login(email, password);
        Assert.assertTrue(authPage.isUserLoggedIn(), "User is not logged in");
        visualAI.checkWindow("User Logged In");

        homePage.clickCart();
        smartWait.waitForPageReady();
        cartPage.clickProceedToCheckout();
        smartWait.waitForPageReady();
        visualAI.checkWindow("Checkout Summary");
        checkoutPage.clickPlaceOrder();

        smartWait.waitForPageReady();
        visualAI.checkWindow("Payment Form");
        paymentPage.fillPaymentDetails(nameOnCard, cardNumber,
                cvc, expiryMonth, expiryYear);
        paymentPage.clickPay();

        visualAI.checkWindow("Order Confirmation");
        Assert.assertTrue(paymentPage.isOrderPlaced(), "Order placed message not visible");
        
        visualAI.endTest();
    }

    @Feature("Cart")
    @Story("Remove Product from Cart")
    @Severity(NORMAL)
    @Description("Verify user can remove product from cart")
    @Test(priority = 14)
    public void TC15_RemoveProduct() {

        visualAI.startTest(driver, "TC15 - Remove Product from Cart", "E-Commerce App");
        smartWait.waitForPageReady();

        homePage.clickProductPage();
        smartWait.waitForPageReady();
        productPage.addFirstProductToCart();
        cartPage.viewCart();
        smartWait.waitForPageReady();
        visualAI.checkWindow("Cart Before Removal");
        Assert.assertTrue(cartPage.isCartPageVisible(), "Cart page not visible");
        cartPage.removeProduct();
        visualAI.checkWindow("Cart After Removal");
        
        visualAI.endTest();
    }

    @Feature("Category")
    @Story("Browse Products by Category")
    @Severity(NORMAL)
    @Description("Verify products are displayed when selecting categories")
    @Test(priority = 15)
    public void TC16_CategoryProducts() {

        visualAI.startTest(driver, "TC16 - Category Products", "E-Commerce App");
        smartWait.waitForPageReady();

        Assert.assertTrue(homePage.isHomePageVisible(), "Home page is not visible");
        categoryPage.clickWomenCategory();
        categoryPage.clickDressSubcategory();
        smartWait.waitForPageReady();
        visualAI.checkWindow("Women Dress Products");
        Assert.assertTrue(categoryPage.isWomenDressProductsVisible(), "Women dress products not visible");

        categoryPage.clickMenCategory();
        categoryPage.clickTshirtsSubcategory();
        smartWait.waitForPageReady();
        visualAI.checkWindow("Men T-shirt Products");
        Assert.assertTrue(categoryPage.isMenTshirtProductsVisible(), "Men tshirts products not visible");
        
        visualAI.endTest();
    }

    @Feature("Brand")
    @Story("Browse Products by Brand")
    @Severity(NORMAL)
    @Description("Verify products are displayed when selecting brands")
    @Test(priority = 16)
    public void TC17_BrandProducts() {

        visualAI.startTest(driver, "TC17 - Brand Products", "E-Commerce App");
        smartWait.waitForPageReady();

        homePage.clickProductPage();
        smartWait.waitForPageReady();
        Assert.assertTrue(productPage.isProductsPageVisible(), "Products page not visible");
        Assert.assertTrue(brandPage.isBrandsSectionVisible(), "Brands section not visible");

        brandPage.clickHMBrand();
        smartWait.waitForPageReady();
        visualAI.checkWindow("H&M Products");
        Assert.assertTrue(brandPage.isHMProductsVisible(), "H&M products not visible");

        brandPage.clickPoloBrand();
        smartWait.waitForPageReady();
        visualAI.checkWindow("Polo Products");
        Assert.assertTrue(brandPage.isPoloProductsVisible(), "Polo products not visible");
        
        visualAI.endTest();
    }

    @Feature("Cart")
    @Story("Search & Verify Cart After Login")
    @Severity(CRITICAL)
    @Description("Verify searched products remain in cart after login")
    @Test(priority = 17, dataProvider = "searchVerifyCartAfterLoginData", dataProviderClass = DataProviders.class)
    public void TC18_SearchVerifyCartAfterLogin(String searchTerm, String email, String password) {

        visualAI.startTest(driver, "TC18 - Search & Verify Cart After Login", "E-Commerce App");
        smartWait.waitForPageReady();

        homePage.clickProductPage();
        smartWait.waitForPageReady();
        productPage.searchProduct(searchTerm);
        visualAI.checkWindow("Search Results");
        Assert.assertTrue(productPage.isSearchedProductsVisible(), "Searched products not visible");

        productPage.addFirstProductToCart();
        cartPage.viewCart();
        smartWait.waitForPageReady();
        visualAI.checkWindow("Cart with Searched Product");

        homePage.clickSignupLogin();
        authPage.login(email, password);
        Assert.assertTrue(authPage.isUserLoggedIn(), "User is not logged in");
        visualAI.checkWindow("User Logged In");

        homePage.clickCart();
        smartWait.waitForPageReady();
        authPage.clickLogout();
        
        visualAI.endTest();
    }

    @Feature("Product")
    @Story("Add Product Review")
    @Severity(NORMAL)
    @Description("Verify user can submit product review")
    @Test(priority = 18, dataProvider = "addReviewData", dataProviderClass = DataProviders.class)
    public void TC19_AddReview(String name, String email, String review) {

        visualAI.startTest(driver, "TC19 - Add Product Review", "E-Commerce App");
        smartWait.waitForPageReady();

        homePage.clickProductPage();
        smartWait.waitForPageReady();
        productPage.clickViewProduct();
        smartWait.waitForPageReady();
        visualAI.checkWindow("Product Details with Review Section");
        Assert.assertTrue(productPage.isProductInfoVisible(), "Product information not visible");
        Assert.assertTrue(productPage.isWriteReviewVisible(), "Review section not visible");

        productPage.submitReview(name, email, review);
        visualAI.checkWindow("Review Submitted");
        Assert.assertTrue(productPage.isReviewThankYouVisible(), "Review thank you message not visible");
        
        visualAI.endTest();
    }

    @Feature("Checkout")
    @Story("Download Invoice")
    @Severity(CRITICAL)
    @Description("Verify user can download invoice after order placement")
    @Test(priority = 19, dataProvider = "downloadInvoiceData", dataProviderClass = DataProviders.class)
    public void TC20_DownloadInvoice(
            String email, String password,
            String nameOnCard, String cardNumber,
            String cvc, String expiryMonth,
            String expiryYear) {

        visualAI.startTest(driver, "TC20 - Download Invoice", "E-Commerce App");
        smartWait.waitForPageReady();

        homePage.clickProductPage();
        smartWait.waitForPageReady();
        productPage.addFirstProductToCart();
        cartPage.viewCart();
        smartWait.waitForPageReady();

        homePage.clickCart();
        cartPage.clickProceedToCheckout();

        if (cartPage.isRegisterLoginVisible()) {
            cartPage.clickRegisterLogin();
        }

        homePage.clickSignupLogin();
        authPage.login(email, password);
        Assert.assertTrue(authPage.isUserLoggedIn(), "User is not logged in");

        homePage.clickCart();
        smartWait.waitForPageReady();
        cartPage.clickProceedToCheckout();
        smartWait.waitForPageReady();
        visualAI.checkWindow("Checkout Summary");

        checkoutPage.clickPlaceOrder();
        smartWait.waitForPageReady();
        visualAI.checkWindow("Payment Form");
        paymentPage.fillPaymentDetails(nameOnCard, cardNumber,
                cvc, expiryMonth, expiryYear);
        paymentPage.clickPay();

        visualAI.checkWindow("Order Confirmation");
        Assert.assertTrue(paymentPage.isOrderPlaced(), "Order placed message not visible");
        paymentPage.downloadInvoice();
        
        visualAI.endTest();
    }
}
