package com.project.ui.Pages;

import org.openqa.selenium.WebDriver;
import com.project.ui.Common.ElementActions;
import com.project.ui.Locators.Page_Locators;
import com.project.ui.utils.Log;

public class ProductPage {

    private static final Class<?> clazz = ProductPage.class;

    private ElementActions actions;

    public ProductPage(WebDriver driver) {
        this.actions = new ElementActions(driver, 60);
        Log.info(clazz, "ProductPage initialized");
    }

    public void searchProduct(String productName) {
        Log.info(clazz, "Searching product: " + productName);
        // Wait for search input to be clickable and visible
        try {
            Thread.sleep(2000); // Allow page to fully load
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        // Retry mechanism for entering search text
        for (int i = 0; i < 3; i++) {
            try {
                actions.enterText(Page_Locators.SearchProduct, productName);
                actions.click(Page_Locators.ClickOnSearchButton);
                Log.info(clazz, "Search submitted successfully on attempt " + (i + 1));
                break;
            } catch (Exception e) {
                Log.info(clazz, "Attempt " + (i + 1) + " to search failed, retrying...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public boolean isSearchedProductsVisible() {
        Log.debug(clazz, "Checking searched products section");
        try {
            Thread.sleep(500); // Allow page to render results
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
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
        // Accept any alerts that may appear
        actions.acceptAlertIfPresent();
        // Wait for the thank you message to appear after review submission
        Log.info(clazz, "Waiting for review confirmation...");
        try {
            Thread.sleep(2000); // Allow time for thank you message to appear
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    public boolean isReviewThankYouVisible() {
        Log.debug(clazz, "Checking review thank you message");
        // The thank you message appears briefly, so wait for 7 seconds
        try {
            Thread.sleep(7000); // Wait 7 seconds for message to be visible
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        return actions.waitForElementWithRetry(Page_Locators.VerifyThankYouForYourReview, 10);
    }
}
