package com.dilshana.qa.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import com.dilshana.qa.base.BaseTest;
import com.dilshana.qa.data.CheckoutData;
import com.dilshana.qa.data.JsonDataReader;
import com.dilshana.qa.pages.CheckoutPage;
import com.dilshana.qa.pages.InventoryPage;
import com.dilshana.qa.pages.LoginPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import java.util.List;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Epic("Shop")
@Feature("Checkout")
public class CheckoutTest extends BaseTest {

    private static final String BACKPACK = "Sauce Labs Backpack";
    private static final String ONESIE = "Sauce Labs Onesie";

    @DataProvider(name = "checkoutErrors", parallel = true)
    public Object[][] checkoutErrors() {
        List<CheckoutData> rows = JsonDataReader.readList("testdata/checkout-errors.json", CheckoutData.class);
        return rows.stream().map(r -> new Object[] {r}).toArray(Object[][]::new);
    }

    private CheckoutPage startCheckout() {
        InventoryPage inventory = new LoginPage(driver()).open().loginAs("standard_user", "secret_sauce");
        return inventory.addToCart(BACKPACK).addToCart(ONESIE).openCart().checkout();
    }

    @Test(groups = {"smoke", "regression"}, description = "A user can buy two products end to end")
    @Severity(SeverityLevel.BLOCKER)
    public void userCanCompleteAPurchase() {
        CheckoutPage checkout = startCheckout()
                .fillForm("Dilshana", "Dasthakeer", "20000")
                .continueToOverview()
                .finish();

        assertThat(checkout.confirmationMessage()).isEqualTo("Thank you for your order!");
    }

    @Test(groups = "regression", description = "The overview subtotal is the sum of the item prices")
    @Severity(SeverityLevel.CRITICAL)
    public void overviewSubtotalMatchesItemPrices() {
        CheckoutPage overview = startCheckout()
                .fillForm("Dilshana", "Dasthakeer", "20000")
                .continueToOverview();

        double expected = overview.overviewItemPrices().stream().mapToDouble(Double::doubleValue).sum();

        assertThat(overview.subtotal()).isCloseTo(expected, within(0.001));
    }

    @Test(groups = "regression", description = "The overview total is subtotal plus tax")
    @Severity(SeverityLevel.CRITICAL)
    public void overviewTotalIsSubtotalPlusTax() {
        CheckoutPage overview = startCheckout()
                .fillForm("Dilshana", "Dasthakeer", "20000")
                .continueToOverview();

        assertThat(overview.total()).isCloseTo(overview.subtotal() + overview.tax(), within(0.001));
    }

    @Test(groups = "regression", dataProvider = "checkoutErrors",
            description = "Missing form fields show the correct error")
    @Severity(SeverityLevel.NORMAL)
    public void missingFieldShowsError(CheckoutData data) {
        CheckoutPage checkout = startCheckout()
                .fillForm(data.firstName(), data.lastName(), data.postalCode())
                .continueExpectingError();

        assertThat(checkout.errorMessage()).isEqualTo(data.expectedError());
    }
}
