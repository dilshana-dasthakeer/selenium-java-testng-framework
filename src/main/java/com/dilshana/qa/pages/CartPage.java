package com.dilshana.qa.pages;

import io.qameta.allure.Step;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CartPage extends BasePage {

    private static final By ITEM_NAMES = By.cssSelector("[data-test='inventory-item-name']");
    private static final By CHECKOUT = By.cssSelector("[data-test='checkout']");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public List<String> itemNames() {
        visible(CHECKOUT);
        return driver.findElements(ITEM_NAMES).stream().map(WebElement::getText).toList();
    }

    @Step("Start checkout")
    public CheckoutPage checkout() {
        click(CHECKOUT);
        return new CheckoutPage(driver);
    }
}
