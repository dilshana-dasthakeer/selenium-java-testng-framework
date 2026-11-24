package com.dilshana.qa.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.dilshana.qa.base.BaseTest;
import com.dilshana.qa.config.Settings;
import com.dilshana.qa.data.JsonDataReader;
import com.dilshana.qa.data.LoginData;
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
@Feature("Login")
public class LoginTest extends BaseTest {

    private static final String PASSWORD = "secret_sauce";

    @DataProvider(name = "invalidLogins", parallel = true)
    public Object[][] invalidLogins() {
        List<LoginData> rows = JsonDataReader.readList("testdata/invalid-logins.json", LoginData.class);
        return rows.stream().map(r -> new Object[] {r}).toArray(Object[][]::new);
    }

    @Test(groups = {"smoke", "regression"}, description = "A valid user reaches the products page")
    @Severity(SeverityLevel.BLOCKER)
    public void validUserCanLogIn() {
        InventoryPage inventory = new LoginPage(driver()).open().loginAs("standard_user", PASSWORD);

        assertThat(inventory.isLoaded()).as("products page is shown").isTrue();
        assertThat(inventory.currentUrl()).endsWith("/inventory.html");
    }

    @Test(groups = "regression", dataProvider = "invalidLogins",
            description = "Invalid credentials show the correct error")
    @Severity(SeverityLevel.CRITICAL)
    public void invalidLoginShowsError(LoginData data) {
        LoginPage login = new LoginPage(driver()).open()
                .loginExpectingError(data.username(), data.password());

        assertThat(login.errorMessage()).isEqualTo(data.expectedError());
        assertThat(login.currentUrl()).isEqualTo(Settings.baseUrl() + "/");
    }

    @Test(groups = "regression", description = "A logged-in user can log out")
    @Severity(SeverityLevel.NORMAL)
    public void userCanLogOut() {
        LoginPage login = new LoginPage(driver()).open()
                .loginAs("standard_user", PASSWORD)
                .logout();

        assertThat(login.currentUrl()).isEqualTo(Settings.baseUrl() + "/");
    }

    @Test(groups = "regression", description = "Protected pages redirect to login when not signed in")
    @Severity(SeverityLevel.CRITICAL)
    public void inventoryIsProtected() {
        driver().get(Settings.baseUrl() + "/inventory.html");
        LoginPage login = new LoginPage(driver());

        assertThat(login.errorMessage())
                .contains("You can only access '/inventory.html' when you are logged in");
    }
}
