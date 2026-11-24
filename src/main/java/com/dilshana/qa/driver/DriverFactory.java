package com.dilshana.qa.driver;

import com.dilshana.qa.config.Browser;
import com.dilshana.qa.config.Settings;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Map;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates a WebDriver for the requested browser, locally or on Selenium Grid.
 * Selenium Manager (built into Selenium 4) downloads matching drivers, so no
 * WebDriverManager dependency is needed.
 */
public final class DriverFactory {

    private static final Logger LOG = LoggerFactory.getLogger(DriverFactory.class);

    /** Stops Chrome/Edge password-manager pop-ups from covering the page after login. */
    private static final Map<String, Object> NO_PASSWORD_PROMPTS = Map.of(
            "credentials_enable_service", false,
            "profile.password_manager_enabled", false,
            "profile.password_manager_leak_detection", false);

    private DriverFactory() {
    }

    public static WebDriver create(Browser browser) {
        boolean headless = Settings.headless();
        Capabilities options = optionsFor(browser, headless);
        String grid = Settings.gridUrl();

        WebDriver driver;
        if (grid.isBlank()) {
            LOG.info("Starting local {} (headless={})", browser, headless);
            driver = switch (browser) {
                case CHROME -> new ChromeDriver((ChromeOptions) options);
                case FIREFOX -> new FirefoxDriver((FirefoxOptions) options);
                case EDGE -> new EdgeDriver((EdgeOptions) options);
            };
        } else {
            LOG.info("Starting {} on Selenium Grid at {}", browser, grid);
            try {
                driver = new RemoteWebDriver(URI.create(grid).toURL(), options);
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("Invalid grid.url: " + grid, e);
            }
        }

        driver.manage().timeouts().pageLoadTimeout(Settings.pageLoadTimeout());
        driver.manage().window().setSize(new Dimension(1440, 900));
        return driver;
    }

    private static Capabilities optionsFor(Browser browser, boolean headless) {
        return switch (browser) {
            case CHROME -> {
                ChromeOptions o = new ChromeOptions();
                o.setExperimentalOption("prefs", NO_PASSWORD_PROMPTS);
                o.addArguments("--disable-dev-shm-usage", "--no-sandbox");
                if (headless) {
                    o.addArguments("--headless=new");
                }
                yield o;
            }
            case EDGE -> {
                EdgeOptions o = new EdgeOptions();
                o.setExperimentalOption("prefs", NO_PASSWORD_PROMPTS);
                o.addArguments("--disable-dev-shm-usage");
                if (headless) {
                    o.addArguments("--headless=new");
                }
                yield o;
            }
            case FIREFOX -> {
                FirefoxOptions o = new FirefoxOptions();
                if (headless) {
                    o.addArguments("-headless");
                }
                yield o;
            }
        };
    }
}
