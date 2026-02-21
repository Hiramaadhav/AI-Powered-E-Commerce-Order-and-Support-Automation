package com.project.ui.Pages;

import org.openqa.selenium.WebDriver;
import com.project.ui.Common.ElementActions;
import com.project.ui.Locators.Page_Locators;
import com.project.ui.utils.Log;

public class ContactPage {

    private static final Class<?> clazz = ContactPage.class;

    private ElementActions actions;

    public ContactPage(WebDriver driver) {
        this.actions = new ElementActions(driver, 40);
        Log.info(clazz, "ContactPage initialized");
    }

    public boolean isGetInTouchVisible() {
        Log.debug(clazz, "Checking 'Get In Touch' visibility");
        return actions.isDisplayed(Page_Locators.VerifyGetInTouch);
    }

    public void submitContactForm(String name, String email,
            String subject, String message,
            String filePath) {

        try {
            Log.info(clazz, "Submitting contact form");
            Log.debug(clazz, "Contact email: " + email);

            actions.enterText(Page_Locators.EnterName, name);
            actions.enterText(Page_Locators.EnterContactEmail, email);
            actions.enterText(Page_Locators.EnterSubject, subject);
            actions.enterText(Page_Locators.EnterMessage, message);
            Log.debug(clazz, "Uploading file: " + filePath);
            actions.enterText(Page_Locators.Uploadfile, filePath);
            actions.click(Page_Locators.ClickOnSubmit);
            actions.acceptAlertIfPresent();

        } catch (Exception e) {
            Log.error(clazz, "Error submitting contact form", e);
            throw e;
        }
    }

    public boolean isContactSuccessMessageVisible() {
        Log.debug(clazz, "Checking contact success message");
        return actions.isDisplayed(Page_Locators.VerifySuccessMessage);
    }
}
