package com.project.ui.utils;

import org.testng.annotations.DataProvider;

public class DataProviders {

    @DataProvider(name = "registerUserData")
    public static Object[][] registerUserData() {
        // Generate unique email using timestamp to avoid conflicts
        String uniqueEmail = "testuser" + System.currentTimeMillis() + "@automation.com";
        
        return new Object[][] {
                {
                        "TestUser",
                        uniqueEmail,
                        "Passssswwwword@120983",
                        "17",
                        "May",
                        "2004",
                        "John",
                        "Doe",
                        "Test Company",
                        "123 Test Street",
                        "Apt 456",
                        "India",
                        "Delhi",
                        "New Delhi",
                        "110001",
                        "9999999999"
                }
        };
    }

    @DataProvider(name = "loginValidUserData")
    public static Object[][] loginValidUserData() {
        return new Object[][] {
                {"pohosseweufei@yopmail.com", "pohosseweufei@yopmail.com"}
        };
    }

    @DataProvider(name = "loginInvalidUserData")
    public static Object[][] loginInvalidUserData() {
        return new Object[][] {
                {"hdhgvshxbds@gmail.com", "bhsdksjdnsh"}
        };
    }

    @DataProvider(name = "logoutUserData")
    public static Object[][] logoutUserData() {
        return new Object[][] {
                {"pohosseweufei@yopmail.com", "pohosseweufei@yopmail.com"}
        };
    }

    @DataProvider(name = "registerExistingEmailData")
    public static Object[][] registerExistingEmailData() {
        return new Object[][] {
                {"pohosseweufei@yopmail.com", "pohosseweufei@yopmail.com"}
        };
    }

    @DataProvider(name = "contactUsData")
    public static Object[][] contactUsData() {
        return new Object[][] {
                {
                        "Maadhav",
                        "pohosseweufei@yopmail.com",
                        "Wrong Product",
                        "The product is damaged and wrong product delivered",
                        "C:\\Users\\hp5cd\\Downloads\\Resume_Maadhav_Hira.pdf"
                }
        };
    }

    @DataProvider(name = "searchProductData")
    public static Object[][] searchProductData() {
        return new Object[][] {
                {"TShirt"}
        };
    }

    @DataProvider(name = "subscriptionHomeData")
    public static Object[][] subscriptionHomeData() {
        return new Object[][] {
                {"homesubscribeeee@example.com"}
        };
    }

    @DataProvider(name = "subscriptionCartData")
    public static Object[][] subscriptionCartData() {
        return new Object[][] {
                {"cartsubscribe@example.com"}
        };
    }

    @DataProvider(name = "productQuantityData")
    public static Object[][] productQuantityData() {
        return new Object[][] {
                {"5"}
        };
    }

    @DataProvider(name = "placeOrderAfterLoginData")
    public static Object[][] placeOrderAfterLoginData() {
        return new Object[][] {
                {
                        "fraufequoiffelleu@yopmail.com",
                        "fraufequoiffelleu@yopmail.com",
                        "Maadhav",
                        "829292928272",
                        "123",
                        "07",
                        "2027"
                }
        };
    }

    @DataProvider(name = "searchVerifyCartAfterLoginData")
    public static Object[][] searchVerifyCartAfterLoginData() {
        return new Object[][] {
                {"Shirt", "pohosseweufei@yopmail.com", "pohosseweufei@yopmail.com"}
        };
    }

    @DataProvider(name = "addReviewData")
    public static Object[][] addReviewData() {
        return new Object[][] {
                {"Maadhav", "pohosseweufei@yopmail.com", "Great product quality"}
        };
    }

    @DataProvider(name = "downloadInvoiceData")
    public static Object[][] downloadInvoiceData() {
        return new Object[][] {
                {
                        "mogoproujuffou@yopmail.com",
                        "mogoproujuffou@yopmail.com",
                        "Maadhav",
                        "829201928281",
                        "817",
                        "09",
                        "2027"
                }
        };
    }
}
