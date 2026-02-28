package com.project.ui.Base;

import com.project.ui.Config.Configreader;
import com.epam.healenium.SelfHealingDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.Map;

public class DriverFactory {

    public static WebDriver getDriver() {

        // Setup WebDriverManager to automatically download ChromeDriver
        WebDriverManager.chromedriver().setup();

        // Healnium server
        System.setProperty("hlm.server.url", "http://localhost:9090");

        String browser = Configreader.getProperty("browser");
        String healingEnabled = Configreader.getProperty("heal-enabled");

        WebDriver baseDriver;

        if ("chrome".equalsIgnoreCase(browser)) {

            ChromeOptions options = new ChromeOptions();

            // 🔕 Disable password save popup
            Map<String, Object> prefs = new HashMap<>();
            // ❌ Disable save password
            prefs.put("credentials_enable_service", false);
            prefs.put("profile.password_manager_enabled", false);

            // ❌ Disable save address / autofill popup
            prefs.put("profile.autofill_profile_enabled", false);
            prefs.put("autofill.profile_enabled", false);

            // ❌ Disable notifications
            prefs.put("profile.default_content_setting_values.notifications", 2);
            
            // 🔍 Set default zoom level to 100% (1.0)
            prefs.put("profile.default_zoom_level", 0.0);

            options.setExperimentalOption("prefs", prefs);

            baseDriver = new ChromeDriver(options);

        } else {
            ChromeOptions options = new ChromeOptions();
            baseDriver = new ChromeDriver(options);
        }

        if ("true".equalsIgnoreCase(healingEnabled)) {
            System.out.println("🩹 Starting Self-Healing Driver...");
            return SelfHealingDriver.create(baseDriver);
        }

        return baseDriver;
    }
}
