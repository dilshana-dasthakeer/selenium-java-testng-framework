package com.dilshana.qa.pages;

import com.dilshana.qa.config.Settings;
import io.qameta.allure.Step;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/** https://the-internet.herokuapp.com/tables */
public class TablesPage extends BasePage {

    private static final By DUE_HEADER = By.cssSelector("#table1 thead th:nth-child(4)");
    private static final By DUE_CELLS = By.cssSelector("#table1 tbody tr td:nth-child(4)");
    private static final By LAST_NAME_CELLS = By.cssSelector("#table1 tbody tr td:nth-child(1)");

    public TablesPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open the tables page")
    public TablesPage open() {
        driver.get(Settings.internetUrl() + "/tables");
        visible(DUE_HEADER);
        return this;
    }

    @Step("Sort the table by the Due column")
    public TablesPage sortByDue() {
        click(DUE_HEADER);
        return this;
    }

    public List<Double> dueAmounts() {
        return allVisible(DUE_CELLS).stream()
                .map(e -> Double.parseDouble(e.getText().replace("$", "")))
                .toList();
    }

    public List<String> lastNames() {
        return allVisible(LAST_NAME_CELLS).stream().map(WebElement::getText).toList();
    }
}
