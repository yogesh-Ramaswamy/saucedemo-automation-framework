package saucedemo.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import org.openqa.selenium.WebElement;
import saucedemo.driver.SharedDriver;
import saucedemo.pages.pom.CartPage;
import saucedemo.pages.pom.CheckoutInformationPage;
import saucedemo.pages.pom.NavigationPage;
import saucedemo.support.ProductIds;
import saucedemo.support.UiWait;

public class CartSteps {
    private final SharedDriver sharedDriver;

    public CartSteps(SharedDriver sharedDriver) {
        this.sharedDriver = sharedDriver;
    }

    @When("the user opens the cart")
    public void theUserOpensTheCart() {
        UiWait.untilClickable(sharedDriver, NavigationPage::link_Cart).click();
        UiWait.untilText(sharedDriver, CartPage::label_CartTitle, "Your Cart");
    }

    @Then("the cart badge count is {int}")
    public void theCartBadgeCountIs(int expectedCount) {
        sharedDriver.waitFor().until(
                driver -> NavigationPage.value_CartBadge(driver) == expectedCount);
        assertEquals(expectedCount, NavigationPage.value_CartBadge(sharedDriver.get()));
    }

    @Then("the cart contains {string}")
    public void theCartContains(String productName) {
        assertTrue(cartItemNames().contains(productName),
                "Cart should contain " + productName);
    }

    @Then("the cart does not contain {string}")
    public void theCartDoesNotContain(String productName) {
        assertFalse(cartItemNames().contains(productName),
                "Cart should not contain " + productName);
    }

    @Then("the cart contains exactly:")
    public void theCartContainsExactly(DataTable products) {
        assertEquals(products.asList(), cartItemNames());
    }

    @Then("the cart contains {int} item")
    @Then("the cart contains {int} items")
    public void theCartContainsItemCount(int expectedCount) {
        assertEquals(expectedCount, CartPage.panels_CartItems(sharedDriver.get()).size());
    }

    @When("the user removes {string} from the Cart page")
    public void theUserRemovesProductFromCartPage(String productName) {
        String productId = ProductIds.fromDisplayName(productName);
        UiWait.untilClickable(
                sharedDriver,
                driver -> CartPage.button_RemoveFromCart(driver, productId)).click();
        sharedDriver.waitFor().until(driver -> !cartItemNames().contains(productName));
    }

    @Given("the cart is empty")
    public void theCartIsEmpty() {
        assertEquals(0, NavigationPage.value_CartBadge(sharedDriver.get()),
                "A fresh authenticated scenario should start with an empty cart");
    }

    @Then("the cart is empty after the order")
    public void theCartIsEmptyAfterTheOrder() {
        assertEquals(0, NavigationPage.value_CartBadge(sharedDriver.get()));
        theUserOpensTheCart();
        assertTrue(CartPage.panels_CartItems(sharedDriver.get()).isEmpty(),
                "Cart should contain no products after order completion");
    }

    @When("the user starts checkout")
    public void theUserStartsCheckout() {
        UiWait.untilClickable(sharedDriver, CartPage::button_Checkout).click();
        UiWait.untilText(
                sharedDriver,
                CheckoutInformationPage::label_CheckoutInformationTitle,
                "Checkout: Your Information");
    }

    private List<String> cartItemNames() {
        return CartPage.labels_CartItemNames(sharedDriver.get()).stream()
                .map(WebElement::getText)
                .toList();
    }
}
