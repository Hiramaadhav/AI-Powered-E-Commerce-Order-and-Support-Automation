package com.project.ui.Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import com.project.ui.Common.ElementActions;
import com.project.ui.Locators.Page_Locators;
import com.project.ui.utils.Log;

public class HomePage {

    private static final Class<?> clazz = HomePage.class;

    private ElementActions actions;

    public HomePage(WebDriver driver) {
        this.actions = new ElementActions(driver, 40);
        Log.info(clazz, "HomePage initialized");
    }

    public void clickHome() {
        Log.info(clazz, "Clicking Home button");
        actions.click(Page_Locators.ClickOnHome);
    }

    public boolean isHomePageVisible() {
        Log.debug(clazz, "Checking Home page visibility");
        return actions.isDisplayed(Page_Locators.VerifyHomePage);
    }

    public void clickSignupLogin() {
        Log.info(clazz, "Clicking Signup/Login");
        actions.click(Page_Locators.ClickOnSignupLogin);
    }

    public void clickSignup() {
        Log.info(clazz, "Clicking Signup");
        actions.click(By.xpath("//a[contains(text(),'Signup')]"));
    }

    public void clickProductPage() {
        Log.info(clazz, "Opening Product page");
        actions.click(Page_Locators.ClickOnProductPage);
    }

    public void clickContactUs() {
        Log.info(clazz, "Opening Contact Us page");
        actions.click(Page_Locators.ClickOnContactUs);
    }

    public void clickCart() {
        Log.info(clazz, "Opening Cart page");
        actions.click(Page_Locators.ClickOnCart);
    }

    public void clickTestCases() {
        Log.info(clazz, "Opening Test Cases page");
        actions.click(Page_Locators.ClickOnTestCase);
    }

    public boolean isSubscriptionVisible() {
        Log.debug(clazz, "Checking Subscription section visibility");
        return actions.isDisplayed(Page_Locators.VerifySubscription);
    }

    public void subscribe(String email) {
        Log.info(clazz, "Subscribing with email");
        actions.enterText(Page_Locators.EnterSubscribeEmail, email);
        actions.click(Page_Locators.ClickOnArrowButton);
    }

    public boolean isSubscriptionSuccessVisible() {
        Log.debug(clazz, "Checking subscription success message");
        return actions.isDisplayed(Page_Locators.VerifySuccessfullSubscription);
    }
}
