package com.project.ui.Common;

import java.time.Duration;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import com.project.ui.utils.Log;

public class ElementActions {

    WebDriver driver;
    WebDriverWait wait;

    public ElementActions(WebDriver driver, int timeoutInSeconds) {
        this.driver = driver;
        // Set implicit wait for better element detection
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
    }

    // ========== CORE FIND (Healnium heals here) ==========
    private WebElement find(By locator) {
        Log.debug(ElementActions.class, "Waiting for element: " + locator);
        long startTime = System.currentTimeMillis();
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            long waitTime = System.currentTimeMillis() - startTime;
            Log.debug(ElementActions.class, "Element found after " + waitTime + "ms: " + locator);
            return element;
        } catch (TimeoutException e) {
            long waitTime = System.currentTimeMillis() - startTime;
            Log.error(ElementActions.class, "Timeout waiting for element after " + waitTime + "ms: " + locator);

            // Log page source for debugging
            try {
                String pageSource = driver.getPageSource();
                Log.debug(ElementActions.class, "Page source length: " + pageSource.length() + " characters");
                if (pageSource.contains("search_product")) {
                    Log.debug(ElementActions.class, "search_product element EXISTS in DOM but may not be visible");
                } else {
                    Log.error(ElementActions.class, "search_product element NOT found in DOM");
                }
            } catch (Exception ex) {
                Log.error(ElementActions.class, "Could not get page source for debugging: " + ex.getMessage());
            }

            throw e;
        }
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
            // Check if alert is present - if so, don't remove ads as it might interfere
            // with alert handling
            try {
                WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofMillis(500));
                alertWait.until(ExpectedConditions.alertIsPresent());
                // Alert is present, so skip removing ads
                System.out.println("INFO: Alert detected, skipping removeAds to preserve alert");
                return;
            } catch (TimeoutException e) {
                // No alert, proceed with removing ads
            }

            ((JavascriptExecutor) driver).executeScript(
                    "document.querySelectorAll('iframe, ins, .adsbygoogle').forEach(e => e.remove());");
        } catch (Exception e) {
            // ignore
        }
    }

    // ========== ACTIONS ==========

    public void click(By locator) {
        clickWithRetry(locator, 3);
    }

    private void clickWithRetry(By locator, int maxRetries) {
        int attempts = 0;
        long startTime = System.currentTimeMillis();

        while (attempts < maxRetries) {
            try {
                removeAds();
                Log.info(ElementActions.class, "Attempt " + (attempts + 1) + ": Clicking element " + locator);

                WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
                String elementText = el.getText();
                Log.debug(ElementActions.class, "Found clickable element: " + elementText);

                // Method 1: Direct click
                try {
                    el.click();
                    Log.info(ElementActions.class, "Element clicked successfully using direct click");
                    slowDown();
                    verifyPageChange();
                    return;
                } catch (ElementClickInterceptedException e1) {
                    Log.debug(ElementActions.class, "Direct click intercepted, trying JavaScript click");

                    // Method 2: JavaScript click (more reliable)
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", el);
                    Thread.sleep(300);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
                    Log.info(ElementActions.class, "Element clicked successfully using JavaScript");
                    slowDown();
                    verifyPageChange();
                    return;
                }

            } catch (StaleElementReferenceException e) {
                attempts++;
                if (attempts >= maxRetries) {
                    long elapsedTime = System.currentTimeMillis() - startTime;
                    throw new TimeoutException("Failed to click element after " + maxRetries + " retries (total "
                            + elapsedTime + "ms) due to stale element", e);
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            } catch (TimeoutException e) {
                attempts++;
                long elapsedTime = System.currentTimeMillis() - startTime;
                Log.error(ElementActions.class,
                        "TimeoutException on attempt " + attempts + " after " + elapsedTime + "ms: " + locator);

                if (attempts >= maxRetries) {
                    Log.error(ElementActions.class, "Max retries exceeded. Element not found or not clickable");
                    throw e;
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            } catch (Exception e) {
                attempts++;
                long elapsedTime = System.currentTimeMillis() - startTime;
                Log.error(ElementActions.class, "Unexpected exception on attempt " + attempts + " after " + elapsedTime
                        + "ms: " + e.getClass().getSimpleName());

                if (attempts >= maxRetries) {
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    /**
     * Verify that a page change occurred after click
     */
    private void verifyPageChange() {
        try {
            String initialUrl = driver.getCurrentUrl();
            Thread.sleep(1000);
            String finalUrl = driver.getCurrentUrl();

            if (!initialUrl.equals(finalUrl)) {
                Log.info(ElementActions.class, "Page navigation confirmed: " + initialUrl + " -> " + finalUrl);
            } else {
                Log.debug(ElementActions.class, "Navigation check: URL unchanged (" + finalUrl + ")");
            }
        } catch (Exception e) {
            Log.debug(ElementActions.class, "Could not verify page change: " + e.getMessage());
        }
    }

    public void enterText(By locator, String text) {
        enterTextWithRetry(locator, text, 3);
    }

    private void enterTextWithRetry(By locator, String text, int maxRetries) {
        int attempts = 0;
        long startTime = System.currentTimeMillis();

        while (attempts < maxRetries) {
            try {
                removeAds();
                Log.info(ElementActions.class,
                        "Attempt " + (attempts + 1) + ": Entering text '" + text + "' into " + locator);
                WebElement el = find(locator);
                // Scroll into view before interacting
                ((JavascriptExecutor) driver)
                        .executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", el);
                slowDown();
                el.clear();
                slowDown();
                el.sendKeys(text);
                slowDown();
                long totalTime = System.currentTimeMillis() - startTime;
                Log.info(ElementActions.class, "Successfully entered text after " + totalTime + "ms");
                return;
            } catch (TimeoutException e) {
                attempts++;
                long elapsedTime = System.currentTimeMillis() - startTime;
                Log.error(ElementActions.class,
                        "TimeoutException on attempt " + attempts + " after " + elapsedTime + "ms: " + locator);

                if (attempts >= maxRetries) {
                    // Last attempt failed - provide detailed diagnostics
                    performPageDiagnostics();
                    throw e;
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            } catch (StaleElementReferenceException e) {
                attempts++;
                if (attempts >= maxRetries) {
                    long totalTime = System.currentTimeMillis() - startTime;
                    throw new TimeoutException("Failed to enter text after " + maxRetries + " retries (total "
                            + totalTime + "ms) due to stale element", e);
                }
                Log.debug(ElementActions.class, "Stale element on attempt " + attempts + ", retrying...");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    /**
     * Diagnose page loading issues
     */
    private void performPageDiagnostics() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;

            String readyState = (String) js.executeScript("return document.readyState");
            Log.error(ElementActions.class, "Page readyState: " + readyState);

            Long docLength = (Long) js.executeScript("return document.body.innerHTML.length");
            Log.error(ElementActions.class, "Page content length: " + docLength);

            Long elementCount = (Long) js.executeScript("return document.querySelectorAll('*').length");
            Log.error(ElementActions.class, "Total elements in DOM: " + elementCount);

            // Check if jQuery is loaded
            Boolean hasJQuery = (Boolean) js.executeScript("return typeof jQuery !== 'undefined'");
            Log.error(ElementActions.class, "jQuery loaded: " + hasJQuery);

            if (hasJQuery) {
                Long jqueryActive = (Long) js.executeScript("return jQuery.active || 0");
                Log.error(ElementActions.class, "jQuery active requests: " + jqueryActive);
            }

        } catch (Exception e) {
            Log.error(ElementActions.class, "Could not perform diagnostics: " + e.getMessage());
        }
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

    public boolean isDisplayedWithScroll(By locator) {
        try {
            removeAds();
            WebElement el = find(locator);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", el);
            slowDown();
            return el.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean waitForElementWithRetry(By locator, int maxRetries) {
        Log.debug(ElementActions.class, "Waiting for element with retries: " + locator);
        for (int i = 0; i < maxRetries; i++) {
            try {
                removeAds();
                WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", el);
                slowDown();
                // Give the element time to stabilize
                Thread.sleep(1000);
                if (el.isDisplayed()) {
                    Log.debug(ElementActions.class, "Element found on attempt " + (i + 1));
                    return true;
                }
            } catch (TimeoutException e) {
                if (i < maxRetries - 1) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        Log.debug(ElementActions.class, "Element not found after " + maxRetries + " retries");
        return false;
    }

    public void acceptAlertIfPresent() {
        try {
            Alert alert = driver.switchTo().alert();
            System.out.println("ALERT FOUND: " + alert.getText());
            alert.accept();
            System.out.println("Alert accepted successfully");
        } catch (NoAlertPresentException e) {
            System.out.println("No alert present, continuing execution");
        }
    }
}
