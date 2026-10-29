package saucedemo.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import saucedemo.driver.SharedDriver;
import saucedemo.pages.pom.LoginPage;
import saucedemo.pages.pom.ProductsPage;
import saucedemo.support.LoginActions;
import saucedemo.support.UiWait;

public class LoginSteps {
    private final SharedDriver sharedDriver;
    private final LoginActions loginActions;

    public LoginSteps(SharedDriver sharedDriver, LoginActions loginActions) {
        this.sharedDriver = sharedDriver;
        this.loginActions = loginActions;
    }

    @Given("the user is on the SauceDemo login page")
    public void theUserIsOnTheLoginPage() {
        loginActions.openLoginPage();
    }

    @When("the user submits username {string} and password {string}")
    public void theUserSubmitsCredentials(String username, String password) {
        loginActions.submitCredentials(username, password);
    }

    @When("the user logs in as the standard user")
    public void theUserLogsInAsTheStandardUser() {
        loginActions.submitCredentials(
                loginActions.standardUsername(),
                loginActions.standardPassword());
        loginActions.waitForProductsPage();
    }

    @Then("the Products page is displayed")
    public void theProductsPageIsDisplayed() {
        loginActions.waitForProductsPage();
        assertEquals("Products", ProductsPage.label_ProductsTitle(sharedDriver.get()).getText());
        assertTrue(sharedDriver.get().getCurrentUrl().contains("inventory.html"));
    }

    @Then("the login error is {string}")
    public void theLoginErrorIs(String expectedError) {
        UiWait.untilText(sharedDriver, LoginPage::label_LoginError, expectedError);
        assertEquals(expectedError, LoginPage.label_LoginError(sharedDriver.get()).getText());
    }

    @Then("the user remains on the login page")
    public void theUserRemainsOnTheLoginPage() {
        assertFalse(sharedDriver.get().getCurrentUrl().contains("inventory.html"));
        assertTrue(LoginPage.field_UserName(sharedDriver.get()).isDisplayed());
    }

    @When("the user logs out")
    public void theUserLogsOut() {
        loginActions.logout();
    }

    @Then("the login page is displayed")
    public void theLoginPageIsDisplayed() {
        UiWait.untilVisible(sharedDriver, LoginPage::field_UserName);
        assertTrue(LoginPage.field_UserName(sharedDriver.get()).isDisplayed());
        assertFalse(sharedDriver.get().getCurrentUrl().contains("inventory.html"));
    }
}
