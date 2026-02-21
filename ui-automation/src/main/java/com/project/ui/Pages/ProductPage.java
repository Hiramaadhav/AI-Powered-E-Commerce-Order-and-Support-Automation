package com.project.ui.Pages;

import org.openqa.selenium.WebDriver;
import com.project.ui.Common.ElementActions;
import com.project.ui.Locators.Page_Locators;
import com.project.ui.utils.Log;

public class ProductPage {

    private static final Class<?> clazz = ProductPage.class;

    private ElementActions actions;

    public ProductPage(WebDriver driver) {
        this.actions = new ElementActions(driver, 40);
        Log.info(clazz, "ProductPage initialized");
    }

    public void searchProduct(String productName) {
        Log.info(clazz, "Searching product: " + productName);
        actions.enterText(Page_Locators.SearchProduct, productName);
        actions.click(Page_Locators.ClickOnSearchButton);
    }

    public boolean isSearchedProductsVisible() {
        Log.debug(clazz, "Checking searched products section");
        return actions.isDisplayed(Page_Locators.VerifySearchedProductIsVisible);
    }

    public void addFirstProductToCart() {
        Log.info(clazz, "Adding first product to cart");
        actions.click(Page_Locators.AddFirstProductToCart);
    }

    public void clickContinueShopping() {
        Log.info(clazz, "Continuing shopping");
        actions.click(Page_Locators.ClickOnContinueShopping);
    }

    public void addSecondProductToCart() {
        Log.info(clazz, "Adding second product to cart");
        actions.click(Page_Locators.AddSecondProductToCart);
    }

    public void clickViewProduct() {
        Log.info(clazz, "Viewing product details");
        actions.click(Page_Locators.ClickOnViewProduct);
    }

    public boolean isProductsPageVisible() {
        Log.debug(clazz, "Checking products page visibility");
        return actions.isDisplayed(Page_Locators.VerifyAllProductVisible)
                && actions.isDisplayed(Page_Locators.VerifyProductListIsVisible);
    }

    public boolean isProductInfoVisible() {
        Log.debug(clazz, "Checking product information section");
        return actions.isDisplayed(Page_Locators.VerifyProductInfo);
    }

    public void setQuantity(String quantity) {
        Log.info(clazz, "Setting product quantity: " + quantity);
        actions.enterText(Page_Locators.EnterQuantity, quantity);
    }

    public void addToCartFromDetails() {
        Log.info(clazz, "Adding product to cart from details page");
        actions.click(Page_Locators.ClickOnAddToCart);
    }

    public boolean isWriteReviewVisible() {
        Log.debug(clazz, "Checking review section visibility");
        return actions.isDisplayed(Page_Locators.VerifyWriteYourReviewVisible);
    }

    public void submitReview(String name, String email, String reviewText) {
        Log.info(clazz, "Submitting product review");
        actions.enterText(Page_Locators.EnterReviewName, name);
        actions.enterText(Page_Locators.EnterReviewEmail, email);
        actions.enterText(Page_Locators.EnterReviewText, reviewText);
        actions.click(Page_Locators.SubmitReview);
    }

    public boolean isReviewThankYouVisible() {
        Log.debug(clazz, "Checking review thank you message");
        return actions.isDisplayed(Page_Locators.VerifyThankYouForYourReview);
    }
}
