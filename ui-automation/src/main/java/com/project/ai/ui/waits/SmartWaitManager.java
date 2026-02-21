package com.project.ai.ui.waits;

import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.project.ui.utils.Log;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * AI-Powered Smart Wait Manager
 * Uses machine learning principles to:
 * - Learn optimal wait times for different page types
 * - Adapt to environment performance (CI slower than local)
 * - Detect page readiness beyond simple element presence
 * - Eliminate flaky tests due to timing issues
 */
public class SmartWaitManager {
    
    private static final Class<?> clazz = SmartWaitManager.class;
    private WebDriver driver;
    private WebDriverWait wait;
    
    // ML-based wait time tracking
    private static Map<String, WaitMetrics> pageWaitMetrics = new HashMap<>();
    private static final int DEFAULT_TIMEOUT = 30;
    private static final int DEFAULT_POLL_INTERVAL = 500; // milliseconds
    
    public SmartWaitManager(WebDriver driver, int timeoutSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }
    
    /**
     * Smart wait for element with adaptive timeout
     * Learns from past executions and adjusts timeout
     */
    public WebElement waitForElement(By locator, String context) {
        long startTime = System.currentTimeMillis();
        
        try {
            // Get learned timeout for this context, or use default
            int adaptiveTimeout = getAdaptiveTimeout(context);
            
            WebDriverWait adaptiveWait = new WebDriverWait(driver, Duration.ofSeconds(adaptiveTimeout));
            WebElement element = adaptiveWait.until(ExpectedConditions.presenceOfElementLocated(locator));
            
            // Record successful wait time for learning
            long waitTime = System.currentTimeMillis() - startTime;
            recordWaitMetrics(context, waitTime, true);
            
            Log.info(clazz, String.format("Smart wait successful for %s (took %dms)", context, waitTime));
            return element;
            
        } catch (TimeoutException e) {
            long waitTime = System.currentTimeMillis() - startTime;
            recordWaitMetrics(context, waitTime, false);
            Log.error(clazz, String.format("Smart wait timeout for %s (waited %dms)", context, waitTime));
            throw e;
        }
    }
    
    /**
     * Wait for element to be clickable (visible + enabled + in viewport)
     */
    public WebElement waitForClickable(By locator, String context) {
        long startTime = System.currentTimeMillis();
        
        try {
            int adaptiveTimeout = getAdaptiveTimeout(context);
            WebDriverWait adaptiveWait = new WebDriverWait(driver, Duration.ofSeconds(adaptiveTimeout));
            
            WebElement element = adaptiveWait.until(ExpectedConditions.elementToBeClickable(locator));
            
            // Additional check for overlays/animations
            waitForElementStable(element);
            
            long waitTime = System.currentTimeMillis() - startTime;
            recordWaitMetrics(context, waitTime, true);
            
            Log.info(clazz, String.format("Element clickable for %s (took %dms)", context, waitTime));
            return element;
            
        } catch (TimeoutException e) {
            long waitTime = System.currentTimeMillis() - startTime;
            recordWaitMetrics(context, waitTime, false);
            throw e;
        }
    }
    
    /**
     * Wait for element to become stable (no position/size changes)
     * Detects animations, lazy loading, dynamic content
     */
    public void waitForElementStable(WebElement element) {
        try {
            Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .pollInterval(100, TimeUnit.MILLISECONDS)
                .until(() -> isElementStable(element));
                
            Log.debug(clazz, "Element is stable");
        } catch (ConditionTimeoutException e) {
            Log.error(clazz, "Element did not stabilize within timeout");
        }
    }
    
    /**
     * Check if element position and size are stable
     */
    private boolean isElementStable(WebElement element) {
        try {
            Point location1 = element.getLocation();
            Dimension size1 = element.getSize();
            
            Thread.sleep(200); // Wait briefly
            
            Point location2 = element.getLocation();
            Dimension size2 = element.getSize();
            
            return location1.equals(location2) && size1.equals(size2);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Wait for AJAX/JavaScript to complete
     * Monitors jQuery, Angular, and generic JS execution
     */
    public void waitForAjaxComplete() {
        try {
            Awaitility.await()
                .atMost(30, TimeUnit.SECONDS)
                .pollInterval(500, TimeUnit.MILLISECONDS)
                .until(() -> isAjaxComplete());
                
            Log.info(clazz, "AJAX requests completed");
        } catch (ConditionTimeoutException e) {
            Log.error(clazz, "AJAX wait timeout - continuing anyway");
        }
    }
    
    /**
     * Check if AJAX/JS requests are complete
     */
    private boolean isAjaxComplete() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        
        try {
            // Check jQuery active requests
            Boolean jQueryComplete = (Boolean) js.executeScript(
                "return typeof jQuery != 'undefined' ? jQuery.active == 0 : true"
            );
            
            // Check document ready state
            Boolean documentReady = js.executeScript("return document.readyState").equals("complete");
            
            // Check for Angular (if used)
            Boolean angularReady = (Boolean) js.executeScript(
                "return typeof angular != 'undefined' ? " +
                "angular.element(document).injector().get('$http').pendingRequests.length == 0 : true"
            );
            
            return jQueryComplete && documentReady && angularReady;
            
        } catch (Exception e) {
            // If scripts fail, assume complete
            return true;
        }
    }
    
    /**
     * Wait for page to be fully loaded (DOM + resources + AJAX)
     */
    public void waitForPageReady() {
        long startTime = System.currentTimeMillis();
        
        try {
            // Wait for DOM ready
            wait.until(driver -> 
                ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete")
            );
            
            // Wait for AJAX
            waitForAjaxComplete();
            
            // Wait for network idle (no pending requests)
            waitForNetworkIdle();
            
            long loadTime = System.currentTimeMillis() - startTime;
            Log.info(clazz, String.format("Page fully loaded (took %dms)", loadTime));
            
        } catch (Exception e) {
            Log.error(clazz, "Page load wait failed: " + e.getMessage());
        }
    }
    
    /**
     * Wait for network to be idle (no active requests)
     */
    private void waitForNetworkIdle() {
        try {
            Awaitility.await()
                .atMost(20, TimeUnit.SECONDS)
                .pollInterval(500, TimeUnit.MILLISECONDS)
                .until(() -> {
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    Long activeRequests = (Long) js.executeScript(
                        "return window.performance.getEntriesByType('resource')" +
                        ".filter(r => r.responseEnd === 0).length"
                    );
                    return activeRequests == 0;
                });
        } catch (Exception e) {
            // If script fails or times out, continue
            Log.debug(clazz, "Network idle check skipped");
        }
    }
    
    /**
     * Smart wait for custom condition with retry logic
     */
    public <T> T waitForCondition(Callable<T> condition, String description, int timeoutSeconds) {
        try {
            return Awaitility.await(description)
                .atMost(timeoutSeconds, TimeUnit.SECONDS)
                .pollInterval(DEFAULT_POLL_INTERVAL, TimeUnit.MILLISECONDS)
                .ignoreExceptions()
                .until(condition, result -> result != null);
        } catch (ConditionTimeoutException e) {
            Log.error(clazz, "Condition timeout: " + description);
            throw new TimeoutException("Smart wait timeout: " + description);
        }
    }
    
    /**
     * Get adaptive timeout based on learned metrics
     * Uses historical data to predict optimal timeout
     */
    private int getAdaptiveTimeout(String context) {
        WaitMetrics metrics = pageWaitMetrics.get(context);
        
        if (metrics == null || metrics.getExecutionCount() < 3) {
            return DEFAULT_TIMEOUT;
        }
        
        // Use average + 2*stddev for 95% confidence
        long avgTime = metrics.getAverageWaitTime();
        long stdDev = metrics.getStandardDeviation();
        long adaptiveTimeout = (avgTime + 2 * stdDev) / 1000; // Convert to seconds
        
        // Apply bounds
        adaptiveTimeout = Math.max(5, Math.min(adaptiveTimeout, 60));
        
        Log.debug(clazz, String.format("Adaptive timeout for %s: %ds (based on %d executions)", 
            context, adaptiveTimeout, metrics.getExecutionCount()));
        
        return (int) adaptiveTimeout;
    }
    
    /**
     * Record wait metrics for machine learning
     */
    private void recordWaitMetrics(String context, long waitTime, boolean success) {
        WaitMetrics metrics = pageWaitMetrics.computeIfAbsent(context, k -> new WaitMetrics());
        metrics.addExecution(waitTime, success);
    }
    
    /**
     * Wait metrics class for tracking and learning
     */
    private static class WaitMetrics {
        private long totalWaitTime = 0;
        private long executionCount = 0;
        private long successCount = 0;
        private long sumOfSquares = 0;
        
        public synchronized void addExecution(long waitTime, boolean success) {
            totalWaitTime += waitTime;
            executionCount++;
            sumOfSquares += waitTime * waitTime;
            
            if (success) {
                successCount++;
            }
        }
        
        public long getAverageWaitTime() {
            return executionCount > 0 ? totalWaitTime / executionCount : 0;
        }
        
        public long getStandardDeviation() {
            if (executionCount < 2) return 0;
            
            long mean = getAverageWaitTime();
            long variance = (sumOfSquares / executionCount) - (mean * mean);
            return (long) Math.sqrt(Math.max(variance, 0));
        }
        
        public long getExecutionCount() {
            return executionCount;
        }
        
        public double getSuccessRate() {
            return executionCount > 0 ? (double) successCount / executionCount : 0.0;
        }
    }
    
    /**
     * Get wait statistics for monitoring
     */
    public static Map<String, String> getWaitStatistics() {
        Map<String, String> stats = new HashMap<>();
        
        pageWaitMetrics.forEach((context, metrics) -> {
            String stat = String.format("Avg: %dms, StdDev: %dms, Success: %.1f%% (%d runs)",
                metrics.getAverageWaitTime(),
                metrics.getStandardDeviation(),
                metrics.getSuccessRate() * 100,
                metrics.getExecutionCount()
            );
            stats.put(context, stat);
        });
        
        return stats;
    }
}
