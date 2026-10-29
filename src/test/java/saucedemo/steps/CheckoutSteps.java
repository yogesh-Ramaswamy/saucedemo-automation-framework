package saucedemo.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.math.BigDecimal;
import java.util.List;
import org.openqa.selenium.WebElement;
import saucedemo.driver.SharedDriver;
import saucedemo.pages.pom.CartPage;
import saucedemo.pages.pom.CheckoutCompletePage;
import saucedemo.pages.pom.CheckoutInformationPage;
import saucedemo.pages.pom.CheckoutOverviewPage;
import saucedemo.support.PriceParser;
import saucedemo.support.UiWait;

public class CheckoutSteps {
    private final SharedDriver sharedDriver;
    private boolean emptyOrderReachedConfirmation;

    public CheckoutSteps(SharedDriver sharedDriver) {
        this.sharedDriver = sharedDriver;
    }

    @When("the user submits checkout details first name {string}, last name {string}, postal code {string}")
    public void theUserSubmitsCheckoutDetails(
            String firstName,
            String lastName,
            String postalCode) {
        enterText(CheckoutInformationPage.field_FirstName(sharedDriver.get()), firstName);
        enterText(CheckoutInformationPage.field_LastName(sharedDriver.get()), lastName);
        enterText(CheckoutInformationPage.field_PostalCode(sharedDriver.get()), postalCode);
        UiWait.untilClickable(sharedDriver, CheckoutInformationPage::button_Continue).click();
    }

    @When("the user submits valid checkout details")
    public void theUserSubmitsValidCheckoutDetails() {
        theUserSubmitsCheckoutDetails("Ava", "Tester", "10001");
        UiWait.untilText(
                sharedDriver,
                CheckoutOverviewPage::label_CheckoutOverviewTitle,
                "Checkout: Overview");
    }

    @Then("the checkout error is {string}")
    public void theCheckoutErrorIs(String expectedError) {
        UiWait.untilText(sharedDriver, CheckoutInformationPage::label_CheckoutError, expectedError);
        assertEquals(expectedError,
                CheckoutInformationPage.label_CheckoutError(sharedDriver.get()).getText());
        assertTrue(sharedDriver.get().getCurrentUrl().contains("checkout-step-one.html"));
    }

    @Then("whitespace-only checkout details are rejected")
    public void whitespaceOnlyCheckoutDetailsAreRejected() {
        assertTrue(sharedDriver.get().getCurrentUrl().contains("checkout-step-one.html"),
                "Whitespace-only values must not continue to Checkout Overview");
        assertTrue(CheckoutInformationPage.label_CheckoutError(sharedDriver.get()).isDisplayed(),
                "A required-field validation message should be displayed");
    }

    @Then("checkout overview contains exactly:")
    public void checkoutOverviewContainsExactly(DataTable products) {
        List<String> actual = CheckoutOverviewPage.labels_OverviewItemNames(sharedDriver.get())
                .stream()
                .map(WebElement::getText)
                .toList();
        assertEquals(products.asList(), actual);
    }

    @Then("the overview subtotal, tax, and total are arithmetically consistent")
    public void overviewTotalsAreConsistent() {
        BigDecimal itemSum = CheckoutOverviewPage.labels_OverviewItemPrices(sharedDriver.get())
                .stream()
                .map(WebElement::getText)
                .map(PriceParser::parse)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal displayedSubtotal = PriceParser.parse(
                CheckoutOverviewPage.label_ItemSubtotal(sharedDriver.get()).getText());
        BigDecimal displayedTax = PriceParser.parse(
                CheckoutOverviewPage.label_Tax(sharedDriver.get()).getText());
        BigDecimal displayedTotal = PriceParser.parse(
                CheckoutOverviewPage.label_Total(sharedDriver.get()).getText());

        assertEquals(0, itemSum.compareTo(displayedSubtotal),
                "Displayed subtotal should equal the sum of item prices");
        assertTrue(displayedTax.compareTo(BigDecimal.ZERO) >= 0,
                "Displayed tax should not be negative");
        assertEquals(0, displayedSubtotal.add(displayedTax).compareTo(displayedTotal),
                "Displayed total should equal subtotal plus displayed tax");
    }

    @When("the user finishes the order")
    public void theUserFinishesTheOrder() {
        UiWait.untilClickable(sharedDriver, CheckoutOverviewPage::button_Finish).click();
        UiWait.untilText(
                sharedDriver,
                CheckoutCompletePage::label_CheckoutCompleteTitle,
                "Checkout: Complete!");
    }

    @Then("the order confirmation is displayed")
    public void theOrderConfirmationIsDisplayed() {
        assertEquals(
                "Thank you for your order!",
                CheckoutCompletePage.label_ConfirmationHeader(sharedDriver.get()).getText());
        assertFalse(
                CheckoutCompletePage.label_ConfirmationText(sharedDriver.get()).getText().isBlank());
    }

    @When("the user cancels from Checkout Overview")
    public void theUserCancelsFromCheckoutOverview() {
        UiWait.untilClickable(sharedDriver, CheckoutOverviewPage::button_Cancel).click();
        UiWait.untilUrlContains(sharedDriver, "inventory.html");
    }

    @Then("no order confirmation is displayed")
    public void noOrderConfirmationIsDisplayed() {
        assertFalse(sharedDriver.get().getCurrentUrl().contains("checkout-complete.html"));
        assertFalse(CheckoutCompletePage.confirmation_IsPresent(sharedDriver.get()));
    }

    @When("the user attempts to complete checkout with an empty cart")
    public void theUserAttemptsToCompleteCheckoutWithAnEmptyCart() {
        emptyOrderReachedConfirmation = false;
        if (!CartPage.button_Checkout_IsPresent(sharedDriver.get())
                || !CartPage.button_Checkout(sharedDriver.get()).isEnabled()) {
            return;
        }

        CartPage.button_Checkout(sharedDriver.get()).click();
        if (!sharedDriver.get().getCurrentUrl().contains("checkout-step-one.html")
                || !CheckoutInformationPage.form_IsPresent(sharedDriver.get())) {
            return;
        }

        enterText(CheckoutInformationPage.field_FirstName(sharedDriver.get()), "Ava");
        enterText(CheckoutInformationPage.field_LastName(sharedDriver.get()), "Tester");
        enterText(CheckoutInformationPage.field_PostalCode(sharedDriver.get()), "10001");
        CheckoutInformationPage.button_Continue(sharedDriver.get()).click();
        if (!sharedDriver.get().getCurrentUrl().contains("checkout-step-two.html")
                || !CheckoutOverviewPage.button_Finish_IsPresent(sharedDriver.get())) {
            return;
        }

        CheckoutOverviewPage.button_Finish(sharedDriver.get()).click();
        emptyOrderReachedConfirmation =
                sharedDriver.get().getCurrentUrl().contains("checkout-complete.html")
                        && CheckoutCompletePage.confirmation_IsPresent(sharedDriver.get());
    }

    @Then("the application prevents an empty order from being completed")
    public void theApplicationPreventsAnEmptyOrder() {
        assertFalse(emptyOrderReachedConfirmation,
                "A zero-item order reached the order confirmation page");
    }

    private void enterText(WebElement field, String value) {
        field.clear();
        field.sendKeys(value);
    }
}
