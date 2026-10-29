package saucedemo.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import java.util.List;
import java.util.Locale;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import saucedemo.config.TestConfig;
import saucedemo.driver.SharedDriver;
import saucedemo.pages.pom.CartPage;
import saucedemo.pages.pom.CheckoutInformationPage;
import saucedemo.pages.pom.LoginPage;
import saucedemo.pages.pom.NavigationPage;
import saucedemo.pages.pom.ProductsPage;

public class CompatibilitySteps {
    private final SharedDriver sharedDriver;
    private final TestConfig config;

    public CompatibilitySteps(SharedDriver sharedDriver, TestConfig config) {
        this.sharedDriver = sharedDriver;
        this.config = config;
    }

    @Given("the browser viewport is {int} by {int} pixels")
    public void theBrowserViewportIs(int width, int height) {
        int actualWidth = sharedDriver.setViewportSize(width, height);
        assertEquals(width, actualWidth, "Browser did not reach the requested inner viewport width");
        assertEquals(height, sharedDriver.viewportHeight(),
                "Browser did not reach the requested inner viewport height");
    }

    @Then("login controls are usable without horizontal overflow")
    public void loginControlsAreUsable() {
        assertElementsUsable(List.of(
                LoginPage.field_UserName(sharedDriver.get()),
                LoginPage.field_Password(sharedDriver.get()),
                LoginPage.button_Login(sharedDriver.get())));
        assertNoHorizontalOverflow();
    }

    @Then("product controls are usable without horizontal overflow")
    public void productControlsAreUsable() {
        assertElementsUsable(List.of(
                ProductsPage.label_ProductsTitle(sharedDriver.get()),
                ProductsPage.dropdown_SortProducts(sharedDriver.get()),
                NavigationPage.link_Cart(sharedDriver.get()),
                ProductsPage.buttons_AddToCart(sharedDriver.get()).getFirst()));
        assertNoHorizontalOverflow();
    }

    @Then("cart controls are usable without horizontal overflow")
    public void cartControlsAreUsable() {
        assertElementsUsable(List.of(
                CartPage.label_CartTitle(sharedDriver.get()),
                CartPage.button_Checkout(sharedDriver.get())));
        assertNoHorizontalOverflow();
    }

    @Then("checkout controls are usable without horizontal overflow")
    public void checkoutControlsAreUsable() {
        assertElementsUsable(List.of(
                CheckoutInformationPage.field_FirstName(sharedDriver.get()),
                CheckoutInformationPage.field_LastName(sharedDriver.get()),
                CheckoutInformationPage.field_PostalCode(sharedDriver.get()),
                CheckoutInformationPage.button_Continue(sharedDriver.get())));
        assertNoHorizontalOverflow();
    }

    @Given("the active browser matches the configured browser")
    public void activeBrowserMatchesConfiguredBrowser() {
        String expected = config.browserSelection();
        String actual = sharedDriver.browserName().toLowerCase(Locale.ROOT);
        boolean matches = switch (expected) {
            case "edge" -> actual.contains("edge");
            default -> actual.contains(expected);
        };
        assertTrue(matches,
                "Expected browser " + expected + " but WebDriver reported " + actual);
    }

    private void assertElementsUsable(List<WebElement> elements) {
        elements.forEach(element -> {
            assertTrue(element.isDisplayed(), "Expected control to be displayed");
            assertTrue(isWithinHorizontalViewport(element),
                    "Control is clipped outside the horizontal viewport");
        });
    }

    private boolean isWithinHorizontalViewport(WebElement element) {
        Object result = javascriptExecutor().executeScript(
                "const r = arguments[0].getBoundingClientRect();"
                        + "return r.left >= 0 && r.right <= window.innerWidth;",
                element);
        return Boolean.TRUE.equals(result);
    }

    private void assertNoHorizontalOverflow() {
        Object result = javascriptExecutor().executeScript(
                "return document.documentElement.scrollWidth <= window.innerWidth + 1;");
        assertTrue(Boolean.TRUE.equals(result), "Page has unexpected horizontal overflow");
    }

    private JavascriptExecutor javascriptExecutor() {
        if (!(sharedDriver.get() instanceof JavascriptExecutor javascriptExecutor)) {
            throw new IllegalStateException("WebDriver does not support JavaScript viewport checks");
        }
        return javascriptExecutor;
    }
}
