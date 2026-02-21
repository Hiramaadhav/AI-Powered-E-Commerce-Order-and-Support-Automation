package com.project.ui.Pages;

import org.openqa.selenium.WebDriver;
import com.project.ui.Common.ElementActions;
import com.project.ui.Locators.Page_Locators;
import com.project.ui.utils.Log;

public class CheckoutPage {

    private static final Class<?> clazz = CheckoutPage.class;

    private ElementActions actions;

    public CheckoutPage(WebDriver driver) {
        this.actions = new ElementActions(driver, 40);
        Log.info(clazz, "CheckoutPage initialized");
    }

    public void clickPlaceOrder() {
        Log.info(clazz, "Clicking Place Order");
        actions.click(Page_Locators.ClickOnPlaceOrder);
    }
}
