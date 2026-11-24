package com.dilshana.qa.pages;

import io.qameta.allure.Step;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class InventoryPage extends BasePage {

    private static final By TITLE = By.cssSelector("[data-test='title']");
    private static final By ITEM_NAMES = By.cssSelector("[data-test='inventory-item-name']");
    private static final By ITEM_PRICES = By.cssSelector("[data-test='inventory-item-price']");
    private static final By SORT = By.cssSelector("[data-test='product-sort-container']");
    private static final By CART_BADGE = By.cssSelector("[data-test='shopping-cart-badge']");
    private static final By CART_LINK = By.cssSelector("[data-test='shopping-cart-link']");
    private static final By MENU_BUTTON = By.id("react-burger-menu-btn");
    private static final By LOGOUT_LINK = By.id("logout_sidebar_link");

    /** Sort options as shown on the page. */
    public enum Sort {
        NAME_A_TO_Z("az"), NAME_Z_TO_A("za"), PRICE_LOW_TO_HIGH("lohi"), PRICE_HIGH_TO_LOW("hilo");

        private final String value;

        Sort(String value) {
            this.value = value;
        }
    }

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return "Products".equals(textOf(TITLE));
    }

    public List<String> itemNames() {
        return allVisible(ITEM_NAMES).stream().map(WebElement::getText).toList();
    }

    public List<Double> itemPrices() {
        return allVisible(ITEM_PRICES).stream()
                .map(e -> Double.parseDouble(e.getText().replace("$", "")))
                .toList();
    }

    @Step("Sort products by {sort}")
    public InventoryPage sortBy(Sort sort) {
        new Select(visible(SORT)).selectByValue(sort.value);
        return this;
    }

    @Step("Add '{productName}' to the cart")
    public InventoryPage addToCart(String productName) {
        click(By.xpath("//div[@data-test='inventory-item']"
                + "[.//*[@data-test='inventory-item-name' and normalize-space()='" + productName + "']]//button"));
        return this;
    }

    @Step("Remove '{productName}' from the cart")
    public InventoryPage removeFromCart(String productName) {
        return addToCart(productName);
    }

    /** Number shown on the cart badge, or 0 when the badge is not shown. */
    public int cartCount() {
        return driver.findElements(CART_BADGE).stream()
                .findFirst()
                .map(e -> Integer.parseInt(e.getText().trim()))
                .orElse(0);
    }

    @Step("Open the cart")
    public CartPage openCart() {
        click(CART_LINK);
        return new CartPage(driver);
    }

    @Step("Log out")
    public LoginPage logout() {
        click(MENU_BUTTON);
        click(LOGOUT_LINK);
        return new LoginPage(driver);
    }
}
