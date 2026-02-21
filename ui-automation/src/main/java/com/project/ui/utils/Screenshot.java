package com.project.ui.utils;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WrapsDriver;
import org.openqa.selenium.remote.Augmenter;

import io.qameta.allure.Allure;

public class Screenshot {

    private static final String SCREENSHOT_DIR = "target/screenshots";
    private static final Class<?> clazz = Screenshot.class;

    public static void takeScreenshotOnAssertion(WebDriver driver, String testName) {
        takeScreenshot(driver, testName, "Assertion Failure");
    }

    public static void takeScreenshot(WebDriver driver, String testName, String label) {

        if (driver == null) {
            Log.error(clazz, "Driver is null. Cannot capture screenshot.");
            return;
        }

        try {
            TakesScreenshot actualDriver = resolveTakesScreenshot(driver);

            if (actualDriver == null) {
                Log.error(clazz, "Driver does not support screenshots.");
                return;
            }

            Files.createDirectories(Paths.get(SCREENSHOT_DIR));

            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS")
                    .format(new Date());

            String fileName = testName + "_" + timestamp + ".png";
            Path filePath = Paths.get(SCREENSHOT_DIR, fileName);

                byte[] screenshotBytes =
                    actualDriver.getScreenshotAs(OutputType.BYTES);

            Files.write(filePath, screenshotBytes);

            Allure.addAttachment(
                    label + " - " + testName,
                    "image/png",
                    new ByteArrayInputStream(screenshotBytes),
                    ".png"
            );

            Log.info(clazz, "Screenshot saved: " + filePath);

        } catch (Exception e) {
            Log.error(clazz, "Failed to capture screenshot", e);
        }
    }

    /**
     * Unwrap Healnium SelfHealingDriver to get actual WebDriver
     */
    private static TakesScreenshot resolveTakesScreenshot(WebDriver driver) {

        WebDriver current = unwrapDriver(driver);

        if (current instanceof TakesScreenshot) {
            return (TakesScreenshot) current;
        }

        if (current instanceof HasCapabilities) {
            try {
                WebDriver augmented = new Augmenter().augment(current);
                if (augmented instanceof TakesScreenshot) {
                    return (TakesScreenshot) augmented;
                }
            } catch (Exception e) {
                Log.error(clazz, "Failed to augment driver for screenshots", e);
            }
        }

        return null;
    }

    private static WebDriver unwrapDriver(WebDriver driver) {

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

        return current;
    }

    private static WebDriver unwrapViaReflection(WebDriver driver) {

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
