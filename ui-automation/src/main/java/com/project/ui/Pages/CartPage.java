package com.project.ui.Pages;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import com.project.ui.Common.ElementActions;
import com.project.ui.Locators.Page_Locators;
import com.project.ui.utils.Log;

public class CartPage {

    private static final Class<?> clazz = CartPage.class;

    private ElementActions actions;

    public CartPage(WebDriver driver) {
        this.actions = new ElementActions(driver, 40);
        Log.info(clazz, "CartPage initialized");
    }

    public void viewCart() {
        Log.info(clazz, "Opening cart");
        try {
            actions.click(Page_Locators.ClickOnViewCart);
        } catch (TimeoutException e) {
            Log.info(clazz, "View Cart link not clickable, using cart icon");
            actions.click(Page_Locators.ClickOnCart);
        }
    }

    public void removeProduct() {
        Log.info(clazz, "Removing product from cart");
        actions.click(Page_Locators.RemoveProduct);
    }

    public boolean isCartPageVisible() {
        Log.debug(clazz, "Checking cart page visibility");
        return actions.isDisplayed(Page_Locators.VerifyCartPage);
    }

    public boolean isSubscriptionVisible() {
        Log.debug(clazz, "Checking Subscription section visibility on cart page");
        return actions.isDisplayed(Page_Locators.VerifySubscription);
    }

    public void subscribe(String email) {
        Log.info(clazz, "Subscribing from cart page");
        actions.enterText(Page_Locators.EnterSubscribeEmail, email);
        actions.click(Page_Locators.ClickOnArrowButton);
    }

    public boolean isSubscriptionSuccessVisible() {
        Log.debug(clazz, "Checking cart page subscription success message");
        return actions.isDisplayed(Page_Locators.VerifySuccessfullSubscription);
    }

    public boolean isQuantityVisible() {
        Log.debug(clazz, "Checking product quantity in cart");
        return actions.isDisplayed(Page_Locators.VerifyQuantity);
    }

    public void clickProceedToCheckout() {
        Log.info(clazz, "Proceeding to checkout");
        actions.click(Page_Locators.ClickOnProceedToCheckout);
    }

    public boolean isRegisterLoginVisible() {
        Log.debug(clazz, "Checking Register/Login link visibility");
        return actions.isDisplayed(Page_Locators.ClickOnRegisterLogin);
    }

    public void clickRegisterLogin() {
        Log.info(clazz, "Clicking Register/Login link");
        actions.click(Page_Locators.ClickOnRegisterLogin);
    }
}
