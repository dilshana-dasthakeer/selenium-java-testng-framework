package com.dilshana.qa.pages;

import com.dilshana.qa.config.Settings;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** Shared actions for all page objects. Every interaction waits explicitly; there are no sleeps. */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Settings.explicitWait());
    }

    protected WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected List<WebElement> allVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected void type(By locator, String text) {
        WebElement field = visible(locator);
        field.clear();
        field.sendKeys(text);
    }

    protected String textOf(By locator) {
        return visible(locator).getText().trim();
    }

    protected boolean isDisplayed(By locator) {
        try {
            return visible(locator).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    protected boolean isAbsent(By locator) {
        return wait.until(ExpectedConditions.numberOfElementsToBe(locator, 0));
    }

    public String currentUrl() {
        return driver.getCurrentUrl();
    }
}
