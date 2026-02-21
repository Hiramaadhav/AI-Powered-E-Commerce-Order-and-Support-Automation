package com.project.api.Config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * APIConfig class to manage API configuration properties
 */
public class APIConfig {
    private static final Logger logger = LogManager.getLogger(APIConfig.class);
    private static Properties properties;
    private static final String CONFIG_FILE_PATH = "src/test/resources/Config.properties";

    static {
        try {
            properties = new Properties();
            FileInputStream fileInputStream = new FileInputStream(CONFIG_FILE_PATH);
            properties.load(fileInputStream);
            fileInputStream.close();
        } catch (IOException e) {
            logger.error("Error loading configuration file", e);
        }
    }

    /**
     * Get base URL from properties
     */
    public static String getBaseURL() {
        return properties.getProperty("url", "https://automationexercise.com");
    }

    /**
     * Get implicit wait from properties
     */
    public static int getImplicitWait() {
        return Integer.parseInt(properties.getProperty("implicitWait", "10"));
    }

    /**
     * Get explicit wait from properties
     */
    public static int getExplicitWait() {
        return Integer.parseInt(properties.getProperty("explicitWait", "20"));
    }

    /**
     * Get property by key
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Get property by key with default value
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}
