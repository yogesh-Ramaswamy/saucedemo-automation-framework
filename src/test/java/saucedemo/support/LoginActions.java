package saucedemo.support;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import saucedemo.config.TestConfig;
import saucedemo.driver.SharedDriver;
import saucedemo.pages.pom.LoginPage;
import saucedemo.pages.pom.NavigationPage;
import saucedemo.pages.pom.ProductsPage;

public class LoginActions {
    private final SharedDriver sharedDriver;
    private final TestConfig config;

    public LoginActions(SharedDriver sharedDriver, TestConfig config) {
        this.sharedDriver = sharedDriver;
        this.config = config;
    }

    public void openLoginPage() {
        sharedDriver.get().get(config.baseUrl());
        UiWait.untilVisible(sharedDriver, LoginPage::field_UserName);
    }

    public void submitCredentials(String username, String password) {
        replaceText(LoginPage.field_UserName(sharedDriver.get()), username);
        replaceText(LoginPage.field_Password(sharedDriver.get()), password);
        UiWait.untilClickable(sharedDriver, LoginPage::button_Login).click();
    }

    public void loginAsStandardUser() {
        openLoginPage();
        submitCredentials(config.standardUsername(), config.standardPassword());
        waitForProductsPage();
    }

    public void waitForProductsPage() {
        UiWait.untilUrlContains(sharedDriver, "inventory.html");
        UiWait.untilText(sharedDriver, ProductsPage::label_ProductsTitle, "Products");
    }

    public void logout() {
        UiWait.untilClickable(sharedDriver, NavigationPage::button_Menu).click();
        UiWait.untilClickable(sharedDriver, NavigationPage::link_Logout).click();
        UiWait.untilVisible(sharedDriver, LoginPage::field_UserName);
    }

    public String standardUsername() {
        return config.standardUsername();
    }

    public String standardPassword() {
        return config.standardPassword();
    }

    public String lockedUsername() {
        return config.lockedUsername();
    }

    public String baseUrl() {
        return config.baseUrl();
    }

    private void replaceText(WebElement element, String value) {
        element.clear();
        element.sendKeys(value);
    }
}
