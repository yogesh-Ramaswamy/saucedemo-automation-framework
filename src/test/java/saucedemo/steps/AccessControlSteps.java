package saucedemo.steps;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import saucedemo.driver.SharedDriver;
import saucedemo.pages.pom.LoginPage;
import saucedemo.support.LoginActions;
import saucedemo.support.UiWait;

public class AccessControlSteps {
    private final SharedDriver sharedDriver;
    private final LoginActions loginActions;

    public AccessControlSteps(SharedDriver sharedDriver, LoginActions loginActions) {
        this.sharedDriver = sharedDriver;
        this.loginActions = loginActions;
    }

    @When("the user opens protected path {string}")
    public void theUserOpensProtectedPath(String path) {
        String normalizedPath = path.startsWith("/") ? path.substring(1) : path;
        sharedDriver.get().get(loginActions.baseUrl() + normalizedPath);
    }

    @Then("protected content is not accessible")
    public void protectedContentIsNotAccessible() {
        UiWait.untilVisible(sharedDriver, LoginPage::field_UserName);
        String currentUrl = sharedDriver.get().getCurrentUrl();
        assertFalse(currentUrl.contains("inventory.html"));
        assertFalse(currentUrl.contains("cart.html"));
        assertFalse(currentUrl.contains("checkout-step-one.html"));
        assertTrue(LoginPage.field_UserName(sharedDriver.get()).isDisplayed());
    }
}
