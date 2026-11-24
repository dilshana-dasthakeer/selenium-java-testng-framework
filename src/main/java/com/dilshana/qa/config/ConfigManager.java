package com.dilshana.qa.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads settings for the selected environment from config/{env}.properties.
 * Any key can be overridden at run time with a JVM system property (-Dbrowser=firefox)
 * or an environment variable (BROWSER=firefox), in that order of priority.
 */
public final class ConfigManager {

    private static final Properties PROPS = load();

    private ConfigManager() {
    }

    private static Properties load() {
        String env = System.getProperty("env", System.getenv().getOrDefault("ENV", "qa"));
        String file = "config/" + env + ".properties";
        Properties props = new Properties();
        try (InputStream in = ConfigManager.class.getClassLoader().getResourceAsStream(file)) {
            if (in == null) {
                throw new IllegalStateException("Missing config file on classpath: " + file);
            }
            props.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read " + file, e);
        }
        props.setProperty("env", env);
        return props;
    }

    public static String get(String key) {
        String sys = System.getProperty(key);
        if (sys != null && !sys.isBlank()) {
            return sys.trim();
        }
        String envVar = System.getenv(key.toUpperCase().replace('.', '_'));
        if (envVar != null && !envVar.isBlank()) {
            return envVar.trim();
        }
        String value = PROPS.getProperty(key);
        if (value == null) {
            throw new IllegalStateException("No value configured for '" + key + "'");
        }
        return value.trim();
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static boolean getBool(String key) {
        return Boolean.parseBoolean(get(key));
    }
}
