package com.dilshana.qa.pages;

import com.dilshana.qa.config.Settings;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** https://the-internet.herokuapp.com/dynamic_loading/2 : the element is added to the page after loading. */
public class DynamicLoadingPage extends BasePage {

    private static final By START = By.cssSelector("#start button");
    private static final By RESULT = By.cssSelector("#finish h4");

    public DynamicLoadingPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open the dynamic loading page")
    public DynamicLoadingPage open() {
        driver.get(Settings.internetUrl() + "/dynamic_loading/2");
        visible(START);
        return this;
    }

    @Step("Start loading")
    public DynamicLoadingPage start() {
        click(START);
        return this;
    }

    /** Waits for the element that appears after the loading bar finishes. */
    public String resultText() {
        return textOf(RESULT);
    }
}
