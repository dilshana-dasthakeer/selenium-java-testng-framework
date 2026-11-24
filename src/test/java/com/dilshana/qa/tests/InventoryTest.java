package com.dilshana.qa.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.dilshana.qa.base.BaseTest;
import com.dilshana.qa.pages.CartPage;
import com.dilshana.qa.pages.InventoryPage;
import com.dilshana.qa.pages.InventoryPage.Sort;
import com.dilshana.qa.pages.LoginPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import java.util.Comparator;
import java.util.List;
import org.testng.annotations.Test;

@Epic("Shop")
@Feature("Products")
public class InventoryTest extends BaseTest {

    private static final String BACKPACK = "Sauce Labs Backpack";
    private static final String BIKE_LIGHT = "Sauce Labs Bike Light";

    private InventoryPage loggedIn() {
        return new LoginPage(driver()).open().loginAs("standard_user", "secret_sauce");
    }

    @Test(groups = "regression", description = "Six products are listed")
    @Severity(SeverityLevel.NORMAL)
    public void sixProductsAreListed() {
        assertThat(loggedIn().itemNames()).hasSize(6);
    }

    @Test(groups = "regression", description = "Sorting by price, low to high, orders prices ascending")
    @Severity(SeverityLevel.NORMAL)
    public void sortByPriceLowToHigh() {
        List<Double> prices = loggedIn().sortBy(Sort.PRICE_LOW_TO_HIGH).itemPrices();

        assertThat(prices).isSorted();
    }

    @Test(groups = "regression", description = "Sorting by name, Z to A, orders names descending")
    @Severity(SeverityLevel.NORMAL)
    public void sortByNameZToA() {
        List<String> names = loggedIn().sortBy(Sort.NAME_Z_TO_A).itemNames();

        assertThat(names).isSortedAccordingTo(Comparator.reverseOrder());
    }

    @Test(groups = {"smoke", "regression"}, description = "Adding products updates the cart badge")
    @Severity(SeverityLevel.CRITICAL)
    public void addingProductsUpdatesCartBadge() {
        InventoryPage inventory = loggedIn();
        assertThat(inventory.cartCount()).as("empty cart shows no badge").isZero();

        inventory.addToCart(BACKPACK).addToCart(BIKE_LIGHT);

        assertThat(inventory.cartCount()).isEqualTo(2);
    }

    @Test(groups = "regression", description = "Removing a product lowers the cart badge")
    @Severity(SeverityLevel.NORMAL)
    public void removingProductLowersCartBadge() {
        InventoryPage inventory = loggedIn().addToCart(BACKPACK).addToCart(BIKE_LIGHT);

        inventory.removeFromCart(BACKPACK);

        assertThat(inventory.cartCount()).isEqualTo(1);
    }

    @Test(groups = "regression", description = "The cart lists exactly the products that were added")
    @Severity(SeverityLevel.CRITICAL)
    public void cartContainsAddedProducts() {
        CartPage cart = loggedIn().addToCart(BACKPACK).addToCart(BIKE_LIGHT).openCart();

        assertThat(cart.itemNames()).containsExactlyInAnyOrder(BACKPACK, BIKE_LIGHT);
    }
}
