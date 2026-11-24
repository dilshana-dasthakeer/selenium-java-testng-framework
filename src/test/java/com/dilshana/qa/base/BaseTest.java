package com.dilshana.qa.base;

import com.dilshana.qa.config.Settings;
import com.dilshana.qa.driver.DriverFactory;
import com.dilshana.qa.driver.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Starts a fresh browser for every test method and closes it afterwards.
 * The driver lives in a ThreadLocal, so test classes must not keep it in a field.
 */
public abstract class BaseTest {

    @BeforeMethod(alwaysRun = true)
    public void startBrowser() {
        DriverManager.set(DriverFactory.create(Settings.browser()));
    }

    @AfterMethod(alwaysRun = true)
    public void stopBrowser() {
        DriverManager.quit();
    }

    protected WebDriver driver() {
        return DriverManager.get();
    }
}
