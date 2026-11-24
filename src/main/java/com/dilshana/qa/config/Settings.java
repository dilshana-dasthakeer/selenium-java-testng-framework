package com.dilshana.qa.config;

import java.time.Duration;

/** Typed access to the settings the framework uses. */
public final class Settings {

    private Settings() {
    }

    public static String env() {
        return ConfigManager.get("env");
    }

    public static String baseUrl() {
        return ConfigManager.get("base.url");
    }

    public static String internetUrl() {
        return ConfigManager.get("internet.url");
    }

    public static Browser browser() {
        return Browser.from(ConfigManager.get("browser"));
    }

    public static boolean headless() {
        return ConfigManager.getBool("headless");
    }

    /** Empty means run locally; otherwise the Selenium Grid URL, e.g. http://localhost:4444. */
    public static String gridUrl() {
        return ConfigManager.get("grid.url");
    }

    public static Duration explicitWait() {
        return Duration.ofSeconds(ConfigManager.getInt("timeout.explicit.seconds"));
    }

    public static Duration pageLoadTimeout() {
        return Duration.ofSeconds(ConfigManager.getInt("timeout.pageload.seconds"));
    }

    public static int maxRetries() {
        return ConfigManager.getInt("retry.max");
    }
}
