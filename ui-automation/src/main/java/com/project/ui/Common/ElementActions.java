package com.project.ui.Common;

import java.time.Duration;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

public class ElementActions {

    WebDriver driver;
    WebDriverWait wait;

    public ElementActions(WebDriver driver, int timeoutInSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
    }

    // ========== CORE FIND (Healnium heals here) ==========
    private WebElement find(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // Small controlled delay (human-like speed)
    private void slowDown() {
        try {
            Thread.sleep(400); // 0.4 second only
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void removeAds() {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "document.querySelectorAll('iframe, ins, .adsbygoogle').forEach(e => e.remove());");
        } catch (Exception e) {
            // ignore
        }
    }

    // ========== ACTIONS ==========

    public void click(By locator) {
        removeAds();
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
        try {
            el.click(); // Healnium works here
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", el);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
        slowDown();
    }

    public void enterText(By locator, String text) {
        removeAds();
        WebElement el = find(locator); 
        el.clear();
        slowDown();
        el.sendKeys(text);
        slowDown();
    }

    public void selectByText(By locator, String text) {
        removeAds();
        WebElement el = find(locator); 
        new Select(el).selectByVisibleText(text);
        slowDown();
    }

    // ========== VERIFY ==========

    public boolean isDisplayed(By locator) {
        try {
            removeAds();
            return find(locator).isDisplayed(); 
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void acceptAlertIfPresent() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            System.out.println("ALERT FOUND: " + alert.getText());
            alert.accept();
        } catch (TimeoutException e) {
            System.out.println("INFO: No alert present, continuing execution");
        }
    }
}
