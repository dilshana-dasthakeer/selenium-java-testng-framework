package com.dilshana.qa.pages;

import com.dilshana.qa.config.Settings;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

/** https://the-internet.herokuapp.com/dropdown */
public class DropdownPage extends BasePage {

    private static final By DROPDOWN = By.id("dropdown");

    public DropdownPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open the dropdown page")
    public DropdownPage open() {
        driver.get(Settings.internetUrl() + "/dropdown");
        visible(DROPDOWN);
        return this;
    }

    @Step("Select '{option}'")
    public DropdownPage select(String option) {
        new Select(visible(DROPDOWN)).selectByVisibleText(option);
        return this;
    }

    public String selectedOption() {
        return new Select(visible(DROPDOWN)).getFirstSelectedOption().getText();
    }
}
