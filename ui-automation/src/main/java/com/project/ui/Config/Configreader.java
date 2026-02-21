package com.project.ui.Config;

import java.io.InputStream;
import java.util.Properties;

public class Configreader {
     private static Properties prop;

    public static Properties loadConfig() {
        try {
            prop = new Properties();
            InputStream inputStream = Configreader.class.getClassLoader()
                .getResourceAsStream("Config.properties");
            
            if (inputStream == null) {
                throw new RuntimeException("Config.properties file not found in classpath");
            }
            
            prop.load(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load configuration", e);
        }
        return prop;
    }

    public static String getProperty(String key) {
        return loadConfig().getProperty(key);
    }
}
