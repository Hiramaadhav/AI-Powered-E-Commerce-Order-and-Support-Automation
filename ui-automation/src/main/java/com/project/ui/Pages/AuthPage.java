package com.project.ui.Pages;

import org.openqa.selenium.WebDriver;
import com.project.ui.Common.ElementActions;
import com.project.ui.Locators.Page_Locators;
import com.project.ui.utils.Log;

public class AuthPage {

    private static final Class<?> clazz = AuthPage.class;

    private ElementActions actions;

    public AuthPage(WebDriver driver) {
        this.actions = new ElementActions(driver, 60);
        Log.info(clazz, "AuthPage initialized");
    }

    public void enterSignupNameEmail(String name, String email) {
        Log.info(clazz, "Entering signup details");
        actions.enterText(Page_Locators.EnterName, name);
        actions.enterText(Page_Locators.EnterEmail, email);
    }

    public void clickSignupButton() {
        Log.info(clazz, "Clicking Signup button");
        actions.click(Page_Locators.ClickOnSignupButton);
    }

    public boolean isNewUserSignupVisible() {
        Log.debug(clazz, "Checking New User Signup section");
        return actions.isDisplayed(Page_Locators.VerifyNewUserSignup);
    }

    public boolean isEnterAccountInfoVisible() {
        Log.debug(clazz, "Checking Enter Account Information section");
        return actions.isDisplayed(Page_Locators.VerifyEnterAccountInformation);
    }

    public void selectTitle(String title) {
        Log.info(clazz, "Selecting title: " + title);
        if (title != null && title.equalsIgnoreCase("Mrs")) {
            actions.click(Page_Locators.SelectTitleMrs);
        } else {
            actions.click(Page_Locators.SelectTitleMr);
        }
    }

    public void fillAccountInfo(String password, String day, String month, String year) {
        Log.info(clazz, "Filling account information");
        actions.enterText(Page_Locators.SetPassword, password);
        actions.selectByText(Page_Locators.SelectDay, day);
        actions.selectByText(Page_Locators.SelectMonth, month);
        actions.selectByText(Page_Locators.SelectYear, year);
    }

    public void selectNewsletter() {
        Log.info(clazz, "Selecting newsletter signup");
        actions.click(Page_Locators.SignupNewsletter);
    }

    public void selectSpecialOffers() {
        Log.info(clazz, "Selecting special offers");
        actions.click(Page_Locators.SignupSpecialOffers);
    }

    public void fillAddressDetails(String firstName, String lastName, String company,
                                   String address1, String address2, String country,
                                   String state, String city, String zipcode, String mobile) {
        Log.info(clazz, "Filling address details");
        actions.enterText(Page_Locators.EnterFirstName, firstName);
        actions.enterText(Page_Locators.EnterLastName, lastName);
        actions.enterText(Page_Locators.EnterCompanyName, company);
        actions.enterText(Page_Locators.EnterAddress1, address1);
        actions.enterText(Page_Locators.EnterAddress2, address2);
        actions.selectByText(Page_Locators.SelectCountry, country);
        actions.enterText(Page_Locators.EnterState, state);
        actions.enterText(Page_Locators.EnterCity, city);
        actions.enterText(Page_Locators.EnterZipcode, zipcode);
        actions.enterText(Page_Locators.EnterMobileNumber, mobile);
    }

    public void clickCreateAccount() {
        Log.info(clazz, "Clicking Create Account button");
        actions.click(Page_Locators.ClickOnCreateAccountButton);
    }

    public void clickContinue() {
        Log.info(clazz, "Clicking Continue button");
        actions.click(Page_Locators.ClickOnContinue);
    }

    public boolean isAccountCreated() {
        Log.debug(clazz, "Checking account created confirmation");
        return actions.isDisplayed(Page_Locators.VerifyAccountCreated);
    }

    public boolean isExistingUserSignupVisible() {
        Log.debug(clazz, "Checking existing user signup warning");
        return actions.isDisplayed(Page_Locators.VerifyExistingUserSignup);
    }

    public void enterLoginEmail(String email) {
        Log.info(clazz, "Entering login email");
        actions.enterText(Page_Locators.EnterLoginEmail, email);
    }

    public void enterLoginPassword(String password) {
        Log.debug(clazz, "Entering login password");
        actions.enterText(Page_Locators.EnterLoginPassword, password);
    }

    public void clickLogin() {
        Log.info(clazz, "Clicking Login button");
        actions.click(Page_Locators.ClickOnLogin);
    }

    public boolean isLoginFormVisible() {
        Log.debug(clazz, "Checking login form visibility");
        return actions.isDisplayed(Page_Locators.VerifyLoginToYourAccount);
    }

    public void login(String email, String password) {
        Log.info(clazz, "Logging in user");
        enterLoginEmail(email);
        enterLoginPassword(password);
        clickLogin();
    }

    public boolean isUserLoggedIn() {
        Log.debug(clazz, "Checking if user is logged in");
        return actions.isDisplayed(Page_Locators.VerifyLoggedInAsUsername);
    }

    public boolean isWrongCredentialMessageDisplayed() {
        Log.debug(clazz, "Checking wrong credential message");
        return actions.isDisplayed(Page_Locators.VerifyWrongCredentials);
    }

    public void clickLogout() {
        Log.info(clazz, "Clicking Logout button");
        actions.click(Page_Locators.ClickOnLogout);
    }

    public void deleteAccount() {
        Log.info(clazz, "Deleting account");
        actions.click(Page_Locators.DeleteAccount);
    }

    public boolean isAccountDeleted() {
        Log.debug(clazz, "Checking account deletion confirmation");
        return actions.isDisplayed(Page_Locators.VerifyAccountDeleted);
    }
}
