package com.dilshana.qa.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.dilshana.qa.base.BaseTest;
import com.dilshana.qa.pages.DropdownPage;
import com.dilshana.qa.pages.DynamicLoadingPage;
import com.dilshana.qa.pages.TablesPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import java.util.List;
import org.testng.annotations.Test;

/** Common UI component patterns, run against the-internet.herokuapp.com. */
@Epic("UI components")
@Feature("Web elements")
public class WebElementsTest extends BaseTest {

    @Test(groups = "regression", description = "A dropdown option can be selected by its visible text")
    @Severity(SeverityLevel.NORMAL)
    public void dropdownOptionCanBeSelected() {
        DropdownPage page = new DropdownPage(driver()).open().select("Option 2");

        assertThat(page.selectedOption()).isEqualTo("Option 2");
    }

    @Test(groups = "regression", description = "An element added after loading is found without sleeping")
    @Severity(SeverityLevel.NORMAL)
    public void dynamicallyLoadedElementIsFound() {
        DynamicLoadingPage page = new DynamicLoadingPage(driver()).open().start();

        assertThat(page.resultText()).isEqualTo("Hello World!");
    }

    @Test(groups = "regression", description = "Sorting a web table by Due amount orders it ascending")
    @Severity(SeverityLevel.NORMAL)
    public void tableCanBeSortedByDueAmount() {
        TablesPage page = new TablesPage(driver()).open().sortByDue();

        List<Double> amounts = page.dueAmounts();

        assertThat(amounts).hasSizeGreaterThan(1).isSorted();
    }
}
