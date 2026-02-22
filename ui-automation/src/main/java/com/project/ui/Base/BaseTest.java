package com.project.ui.Base;

import com.project.ui.Config.Configreader;
import com.project.ui.utils.Screenshot;

import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    private static final Logger logger = LogManager.getLogger(BaseTest.class);

    protected WebDriver driver;
    protected String currentTestName;

    @BeforeClass
    public void setUp() {

        logger.info("Initializing WebDriver...");

        driver = DriverFactory.getDriver();
        driver.manage().window().maximize();
        
        // Reset browser zoom to 100%
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.body.style.zoom='100%'");
        logger.info("Browser zoom level reset to 100%");

        logger.info("WebDriver initialized successfully.");
    }

    @BeforeMethod
    public void beforeEachTest(Method method) {

        currentTestName = method.getName();

        logger.info("========== START TEST: {} ==========", currentTestName);
        String url = Configreader.getProperty("url");
        driver.get(url);
        
        // Reset browser zoom to 100% after navigation
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.body.style.zoom='100%'");
        logger.info("Browser zoom level reset to 100% for test: {}", currentTestName);
    }

    @AfterMethod(alwaysRun = true)
    public void afterEachTest(ITestResult result) {

        String testName = result.getMethod().getMethodName();

        if (result.getStatus() == ITestResult.FAILURE) {
            logger.error("Test failed: {}. Capturing screenshot...", testName);
            Screenshot.takeScreenshotOnAssertion(driver, testName + "_FAILED");
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            logger.info("Test passed: {}. Capturing screenshot...", testName);
            Screenshot.takeScreenshot(driver, testName + "_PASSED", "Test Passed");
        }
        logger.info("========== END TEST: {} ==========\n", testName);
    }

    @AfterClass
    public void tearDown() {

        if (driver != null) {
            try {
                logger.info("Closing browser...");
                driver.quit();
                logger.info("Browser closed successfully.");
            } catch (Exception e) {
                logger.error("Error during driver quit", e);
            }
        }
    }
}
