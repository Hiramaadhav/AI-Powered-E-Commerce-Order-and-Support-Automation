package com.project.ui.Pages;

import org.openqa.selenium.WebDriver;
import com.project.ui.Common.ElementActions;
import com.project.ui.Locators.Page_Locators;
import com.project.ui.utils.Log;

public class BrandPage {

    private static final Class<?> clazz = BrandPage.class;

    private ElementActions actions;

    public BrandPage(WebDriver driver) {
        this.actions = new ElementActions(driver, 40);
        Log.info(clazz, "BrandPage initialized");
    }

    public boolean isBrandsSectionVisible() {
        Log.debug(clazz, "Checking Brands section visibility");
        return actions.isDisplayed(Page_Locators.VerifyBrands);
    }

    public void clickHMBrand() {
        try {
            Log.info(clazz, "Clicking H&M brand");
            actions.click(Page_Locators.ClickOnHM);
        } catch (Exception e) {
            Log.error(clazz, "Error while clicking H&M brand", e);
            throw e;
        }
    }

    public boolean isHMProductsVisible() {
        Log.debug(clazz, "Checking H&M products visibility");
        return actions.isDisplayed(Page_Locators.VerifyBrandNameHM);
    }

    public void clickPoloBrand() {
        try {
            Log.info(clazz, "Clicking Polo brand");
            actions.click(Page_Locators.ClickOnPolo);
        } catch (Exception e) {
            Log.error(clazz, "Error while clicking Polo brand", e);
            throw e;
        }
    }

    public boolean isPoloProductsVisible() {
        Log.debug(clazz, "Checking Polo products visibility");
        return actions.isDisplayed(Page_Locators.VerifyBrandNamePolo);
    }
}
