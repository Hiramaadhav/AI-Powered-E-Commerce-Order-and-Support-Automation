package com.project.ui.Pages;

import org.openqa.selenium.WebDriver;
import com.project.ui.Common.ElementActions;
import com.project.ui.Locators.Page_Locators;
import com.project.ui.utils.Log;

public class CategoryPage {

    private static final Class<?> clazz = CategoryPage.class;

    private ElementActions actions;

    public CategoryPage(WebDriver driver) {
        this.actions = new ElementActions(driver, 40);
        Log.info(clazz, "CategoryPage initialized");
    }

    public void clickWomenCategory() {
        try {
            Log.info(clazz, "Clicking Women category");
            actions.click(Page_Locators.ClickOnWomen);
        } catch (Exception e) {
            Log.error(clazz, "Error clicking Women category", e);
            throw e;
        }
    }

    public void clickDressSubcategory() {
        try {
            Log.info(clazz, "Clicking Dress subcategory");
            actions.click(Page_Locators.ClickOnDress);
        } catch (Exception e) {
            Log.error(clazz, "Error clicking Dress subcategory", e);
            throw e;
        }
    }

    public boolean isWomenDressProductsVisible() {
        Log.debug(clazz, "Checking Women Dress products visibility");
        return actions.isDisplayed(Page_Locators.VerifyWomenTopProducts);
    }

    public void clickMenCategory() {
        try {
            Log.info(clazz, "Clicking Men category");
            actions.click(Page_Locators.ClickOnMen);
        } catch (Exception e) {
            Log.error(clazz, "Error clicking Men category", e);
            throw e;
        }
    }

    public void clickTshirtsSubcategory() {
        try {
            Log.info(clazz, "Clicking Tshirts subcategory");
            actions.click(Page_Locators.ClickOnTshirts);
        } catch (Exception e) {
            Log.error(clazz, "Error clicking Tshirts subcategory", e);
            throw e;
        }
    }

    public boolean isMenTshirtProductsVisible() {
        Log.debug(clazz, "Checking Men Tshirts products visibility");
        return actions.isDisplayed(Page_Locators.VerifyMenTopProducts);
    }
}
