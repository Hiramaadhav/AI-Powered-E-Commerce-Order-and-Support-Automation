package com.project.ai.ui.visual;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.EyesRunner;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.TestResultsSummary;
import com.applitools.eyes.selenium.BrowserType;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import com.applitools.eyes.visualgrid.model.DeviceName;
import com.applitools.eyes.visualgrid.model.ScreenOrientation;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;
import org.openqa.selenium.WrapsDriver;
import org.openqa.selenium.WebDriver;
import com.project.ui.utils.Log;

/**
 * Visual AI Validator using Applitools Eyes
 * Validates visual aspects of UI that functional tests cannot detect
 * - CSS bugs, layout shifts, misalignments
 * - Cross-browser visual differences
 * - Responsive design issues
 * - Visual regressions
 */
public class VisualAIValidator {

    private static final Class<?> clazz = VisualAIValidator.class;
    private Eyes eyes;
    private EyesRunner runner;
    private Configuration config;
    private boolean isEnabled;
    private boolean isTestOpen;

    public VisualAIValidator() {
        this.isEnabled = isVisualTestingEnabled();
        if (isEnabled) {
            initializeEyes();
        } else {
            Log.info(clazz, "Visual AI Testing is disabled. Set APPLITOOLS_API_KEY to enable.");
        }
    }

    /**
     * Initialize Applitools Eyes with Visual Grid for cross-browser testing
     */
    private void initializeEyes() {
        try {
            // Create Visual Grid Runner for fast cross-browser execution
            runner = new VisualGridRunner(5);

            eyes = new Eyes(runner);
            config = new Configuration();

            // Set API Key from environment variable
            String apiKey = System.getenv("APPLITOOLS_API_KEY");
            if (apiKey == null || apiKey.isEmpty()) {
                throw new RuntimeException("APPLITOOLS_API_KEY is not set!");
            }
            config.setApiKey(apiKey);

            // Configure batch info
            config.setBatch(new BatchInfo("E-Commerce UI Test Suite"));

            // Configure browsers for cross-browser testing
            config.addBrowser(1920, 1080, BrowserType.CHROME);
            config.addBrowser(1920, 1080, BrowserType.FIREFOX);
            config.addBrowser(1920, 1080, BrowserType.EDGE_CHROMIUM);

            // Configure mobile devices
            config.addDeviceEmulation(DeviceName.iPhone_X, ScreenOrientation.PORTRAIT);
            config.addDeviceEmulation(DeviceName.Galaxy_S9_Plus, ScreenOrientation.PORTRAIT);
            config.addDeviceEmulation(DeviceName.iPad_Pro, ScreenOrientation.LANDSCAPE);

            eyes.setConfiguration(config);

            Log.info(clazz, "Visual AI Validator initialized with Applitools Eyes");
        } catch (Exception e) {
            Log.error(clazz, "Failed to initialize Visual AI: " + e.getMessage());
            isEnabled = false;
        }
    }

    /**
     * Start visual testing session
     */
    public void startTest(WebDriver driver, String testName, String appName) {
        if (!isEnabled)
            return;

        try {
            WebDriver actualDriver = unwrapDriver(driver);
            eyes.open(actualDriver, appName, testName, new RectangleSize(1920, 1080));
            isTestOpen = true;
            Log.info(clazz, "Started visual test: " + testName);
        } catch (Exception e) {
            isTestOpen = false;
            Log.error(clazz, "Failed to start visual test: " + e.getMessage());
        }
    }

    /**
     * Capture and validate full page screenshot
     */
    public void checkWindow(String stepName) {
        if (!isEnabled)
            return;

        try {
            eyes.check(stepName, Target.window().fully());
            Log.info(clazz, "Visual checkpoint: " + stepName);
        } catch (Exception e) {
            Log.error(clazz, "Visual checkpoint failed: " + e.getMessage());
        }
    }

    /**
     * Capture and validate specific region
     */
    public void checkRegion(String stepName, String cssSelector) {
        if (!isEnabled)
            return;

        try {
            eyes.check(stepName, Target.region(org.openqa.selenium.By.cssSelector(cssSelector)));
            Log.info(clazz, "Visual region checkpoint: " + stepName);
        } catch (Exception e) {
            Log.error(clazz, "Visual region checkpoint failed: " + e.getMessage());
        }
    }

    /**
     * Validate page layout (ignore dynamic content)
     */
    public void checkLayout(String stepName) {
        if (!isEnabled)
            return;

        try {
            eyes.check(stepName, Target.window().fully().layout());
            Log.info(clazz, "Layout validation: " + stepName);
        } catch (Exception e) {
            Log.error(clazz, "Layout validation failed: " + e.getMessage());
        }
    }

    /**
     * Validate with dynamic content regions ignored
     */
    public void checkIgnoringRegions(String stepName, String... cssSelectors) {
        if (!isEnabled)
            return;

        try {
            com.applitools.eyes.selenium.fluent.SeleniumCheckSettings checkSettings = Target.window().fully();
            for (String selector : cssSelectors) {
                checkSettings = checkSettings.ignore(org.openqa.selenium.By.cssSelector(selector));
            }
            eyes.check(stepName, checkSettings);
            Log.info(clazz, "Visual checkpoint with ignored regions: " + stepName);
        } catch (Exception e) {
            Log.error(clazz, "Visual checkpoint failed: " + e.getMessage());
        }
    }

    /**
     * Close current test and get results
     */
    public void endTest() {
        if (!isEnabled || !isTestOpen)
            return;

        try {
            eyes.closeAsync();
            isTestOpen = false;
            Log.info(clazz, "Visual test closed");
        } catch (Exception e) {
            Log.error(clazz, "Failed to close visual test: " + e.getMessage());
        }
    }

    /**
     * Get all test results (call after all tests complete)
     */
    public TestResultsSummary getAllTestResults() {
        if (!isEnabled)
            return null;

        try {
            TestResultsSummary allTestResults = runner.getAllTestResults(false);
            Log.info(clazz, "Visual AI Results: " + allTestResults.toString());
            return allTestResults;
        } catch (Exception e) {
            Log.error(clazz, "Failed to get visual test results: " + e.getMessage());
            return null;
        }
    }

    /**
     * Abort current test (use in case of errors)
     */
    public void abortTest() {
        if (!isEnabled || !isTestOpen)
            return;

        try {
            eyes.abortAsync();
            isTestOpen = false;
        } catch (Exception e) {
            Log.error(clazz, "Failed to abort visual test: " + e.getMessage());
        }
    }

    /**
     * Check if visual testing is enabled
     */
    private boolean isVisualTestingEnabled() {
        String enabled = System.getProperty("visual.ai.enabled", "true");
        return Boolean.parseBoolean(enabled);
    }

    /**
     * Quick validation for e-commerce critical elements
     */
    public void validateEcommerceCheckpoint(String checkpointName) {
        if (!isEnabled)
            return;

        try {
            // Validate entire page but ignore dynamic content
            eyes.check(checkpointName,
                    Target.window().fully()
                            .ignore(org.openqa.selenium.By.cssSelector(".price-dynamic")) // Price updates
                            .ignore(org.openqa.selenium.By.cssSelector(".timestamp")) // Timestamps
                            .ignore(org.openqa.selenium.By.cssSelector(".ad-banner")) // Advertisements
                            .ignore(org.openqa.selenium.By.cssSelector(".carousel-slide"))) // Rotating banners
            ;

            Log.info(clazz, "E-commerce visual validation: " + checkpointName);
        } catch (Exception e) {
            Log.error(clazz, "E-commerce validation failed: " + e.getMessage());
        }
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    private WebDriver unwrapDriver(WebDriver driver) {
        WebDriver current = driver;

        for (int i = 0; i < 5; i++) {
            if (current instanceof WrapsDriver) {
                current = ((WrapsDriver) current).getWrappedDriver();
                continue;
            }

            WebDriver reflected = unwrapViaReflection(current);
            if (reflected != null && reflected != current) {
                current = reflected;
                continue;
            }

            break;
        }

        return current == null ? driver : current;
    }

    private WebDriver unwrapViaReflection(WebDriver driver) {
        String[] methodNames = { "getWrappedDriver", "getDriver", "getDelegate", "getWebDriver" };

        for (String name : methodNames) {
            try {
                java.lang.reflect.Method method = driver.getClass().getMethod(name);
                if (WebDriver.class.isAssignableFrom(method.getReturnType())) {
                    Object result = method.invoke(driver);
                    if (result instanceof WebDriver) {
                        return (WebDriver) result;
                    }
                }
            } catch (Exception ignored) {
                // Try the next method name
            }
        }

        try {
            java.lang.reflect.Method[] methods = driver.getClass().getMethods();
            for (java.lang.reflect.Method method : methods) {
                if (method.getParameterCount() != 0) {
                    continue;
                }

                if (!WebDriver.class.isAssignableFrom(method.getReturnType())) {
                    continue;
                }

                Object result = method.invoke(driver);
                if (result instanceof WebDriver) {
                    return (WebDriver) result;
                }
            }
        } catch (Exception ignored) {
            // Reflection fallback failed
        }

        return null;
    }
}
