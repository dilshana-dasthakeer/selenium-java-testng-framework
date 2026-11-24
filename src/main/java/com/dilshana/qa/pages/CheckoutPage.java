package com.dilshana.qa.pages;

import io.qameta.allure.Step;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CheckoutPage extends BasePage {

    private static final By FIRST_NAME = By.cssSelector("[data-test='firstName']");
    private static final By LAST_NAME = By.cssSelector("[data-test='lastName']");
    private static final By POSTAL_CODE = By.cssSelector("[data-test='postalCode']");
    private static final By CONTINUE = By.cssSelector("[data-test='continue']");
    private static final By FINISH = By.cssSelector("[data-test='finish']");
    private static final By ERROR = By.cssSelector("[data-test='error']");
    private static final By SUBTOTAL = By.cssSelector("[data-test='subtotal-label']");
    private static final By TAX = By.cssSelector("[data-test='tax-label']");
    private static final By TOTAL = By.cssSelector("[data-test='total-label']");
    private static final By ITEM_PRICES = By.cssSelector("[data-test='inventory-item-price']");
    private static final By COMPLETE_HEADER = By.cssSelector("[data-test='complete-header']");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    @Step("Fill in the checkout form")
    public CheckoutPage fillForm(String firstName, String lastName, String postalCode) {
        type(FIRST_NAME, firstName);
        type(LAST_NAME, lastName);
        type(POSTAL_CODE, postalCode);
        return this;
    }

    @Step("Continue to the overview")
    public CheckoutPage continueToOverview() {
        click(CONTINUE);
        return this;
    }

    @Step("Continue and expect a form error")
    public CheckoutPage continueExpectingError() {
        click(CONTINUE);
        visible(ERROR);
        return this;
    }

    public String errorMessage() {
        return textOf(ERROR);
    }

    public List<Double> overviewItemPrices() {
        return allVisible(ITEM_PRICES).stream().map(e -> money(e.getText())).toList();
    }

    public double subtotal() {
        return money(textOf(SUBTOTAL));
    }

    public double tax() {
        return money(textOf(TAX));
    }

    public double total() {
        return money(textOf(TOTAL));
    }

    @Step("Finish the order")
    public CheckoutPage finish() {
        click(FINISH);
        return this;
    }

    public String confirmationMessage() {
        return textOf(COMPLETE_HEADER);
    }

    private static double money(String text) {
        return Double.parseDouble(text.replaceAll("[^0-9.]", ""));
    }
}
