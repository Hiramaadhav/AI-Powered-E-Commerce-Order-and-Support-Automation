package com.project.ui.utils;

import org.testng.annotations.DataProvider;

public class DataProviders {

    @DataProvider(name = "registerUserData")
    public static Object[][] registerUserData() {
        return new Object[][] {
                {
                        "sdhsghbncj",
                        "nebngmnjuthbbnhncbyutyeojsnhjc@gmail.com",
                        "ihugybciutfebnj@6739",
                        "17",
                        "May",
                        "2004",
                        "sdhsj",
                        "bhcjdkc",
                        "cdcdcdjd",
                        "cnhdsbgc cdhcbdsc",
                        "xcmdshc jcjnd",
                        "India",
                        "Delhi",
                        "xnjsbhc",
                        "771772",
                        "999928782891"
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
                {"hiramaadhav@gmail.com", "Maadhav@1608"}
        };
    }

    @DataProvider(name = "registerExistingEmailData")
    public static Object[][] registerExistingEmailData() {
        return new Object[][] {
                {"Maadhav", "maadhavhira@gmail.com"}
        };
    }

    @DataProvider(name = "contactUsData")
    public static Object[][] contactUsData() {
        return new Object[][] {
                {
                        "Maadhav",
                        "hiramaadhav@gmail.com",
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
                {"home-subscribeeee@example.com"}
        };
    }

    @DataProvider(name = "subscriptionCartData")
    public static Object[][] subscriptionCartData() {
        return new Object[][] {
                {"cart-subscribe@example.com"}
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
                {"Shirt", "hiramaadhav@gmail.com", "Maadhav@1608"}
        };
    }

    @DataProvider(name = "addReviewData")
    public static Object[][] addReviewData() {
        return new Object[][] {
                {"Maadhav", "hiramaadhav@gmail.com", "Great product quality"}
        };
    }

    @DataProvider(name = "downloadInvoiceData")
    public static Object[][] downloadInvoiceData() {
        return new Object[][] {
                {
                        "mogoproujuffou@yopmail.com",
                        "Mmogoproujuffou@yopmail.com",
                        "Maadhav",
                        "829201928281",
                        "817",
                        "09",
                        "2027"
                }
        };
    }
}
