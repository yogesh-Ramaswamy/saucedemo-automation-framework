package saucedemo.pages.pom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public final class LoginPage {
    private static final By USERNAME = By.cssSelector("[data-test='username']");
    private static final By PASSWORD = By.cssSelector("[data-test='password']");
    private static final By LOGIN = By.cssSelector("[data-test='login-button']");
    private static final By ERROR = By.cssSelector("[data-test='error']");

    private LoginPage() {
    }

    public static WebElement field_UserName(WebDriver sharedDriver) {
        return sharedDriver.findElement(USERNAME);
    }

    public static WebElement field_Password(WebDriver sharedDriver) {
        return sharedDriver.findElement(PASSWORD);
    }

    public static WebElement button_Login(WebDriver sharedDriver) {
        return sharedDriver.findElement(LOGIN);
    }

    public static WebElement label_LoginError(WebDriver sharedDriver) {
        return sharedDriver.findElement(ERROR);
    }
}
