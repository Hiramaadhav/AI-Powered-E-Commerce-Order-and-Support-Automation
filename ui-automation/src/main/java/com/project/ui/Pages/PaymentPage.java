package com.project.ui.Pages;

import org.openqa.selenium.WebDriver;
import com.project.ui.Common.ElementActions;
import com.project.ui.Locators.Page_Locators;
import com.project.ui.utils.Log;

public class PaymentPage {

    private static final Class<?> clazz = PaymentPage.class;

    private ElementActions actions;

    public PaymentPage(WebDriver driver) {
        this.actions = new ElementActions(driver, 60);
        Log.info(clazz, "PaymentPage initialized");
    }

    public void fillPaymentDetails(String cardName, String cardNumber,
                                   String cvc, String expMonth, String expYear) {

        Log.info(clazz, "Filling payment details");

        actions.enterText(Page_Locators.EnterNameOnCard, cardName);
        actions.enterText(Page_Locators.EnterCardNumber, cardNumber);
        actions.enterText(Page_Locators.EnterCVC, cvc);
        actions.enterText(Page_Locators.EnterExpirationMonth, expMonth);
        actions.enterText(Page_Locators.EnterExpirationYear, expYear);
    }

    public void clickPay() {
        Log.info(clazz, "Clicking Pay button");
        actions.click(Page_Locators.ClickOnPay);
    }

    public boolean isOrderPlaced() {
        Log.debug(clazz, "Checking order placement confirmation");
        return actions.isDisplayed(Page_Locators.VerifyOrderPlaced);
    }

    public void downloadInvoice() {
        Log.info(clazz, "Downloading invoice");
        actions.click(Page_Locators.DownloadInvoice);
    }
}
