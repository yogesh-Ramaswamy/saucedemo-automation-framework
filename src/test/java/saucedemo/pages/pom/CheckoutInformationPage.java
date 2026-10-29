package saucedemo.pages.pom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public final class CheckoutInformationPage {
    private static final By TITLE = By.cssSelector("[data-test='title']");
    private static final By FIRST_NAME = By.cssSelector("[data-test='firstName']");
    private static final By LAST_NAME = By.cssSelector("[data-test='lastName']");
    private static final By POSTAL_CODE = By.cssSelector("[data-test='postalCode']");
    private static final By CONTINUE = By.cssSelector("[data-test='continue']");
    private static final By CANCEL = By.cssSelector("[data-test='cancel']");
    private static final By ERROR = By.cssSelector("[data-test='error']");

    private CheckoutInformationPage() {
    }

    public static WebElement label_CheckoutInformationTitle(WebDriver sharedDriver) {
        return sharedDriver.findElement(TITLE);
    }

    public static WebElement field_FirstName(WebDriver sharedDriver) {
        return sharedDriver.findElement(FIRST_NAME);
    }

    public static WebElement field_LastName(WebDriver sharedDriver) {
        return sharedDriver.findElement(LAST_NAME);
    }

    public static WebElement field_PostalCode(WebDriver sharedDriver) {
        return sharedDriver.findElement(POSTAL_CODE);
    }

    public static WebElement button_Continue(WebDriver sharedDriver) {
        return sharedDriver.findElement(CONTINUE);
    }

    public static WebElement button_Cancel(WebDriver sharedDriver) {
        return sharedDriver.findElement(CANCEL);
    }

    public static WebElement label_CheckoutError(WebDriver sharedDriver) {
        return sharedDriver.findElement(ERROR);
    }

    public static boolean form_IsPresent(WebDriver sharedDriver) {
        return !sharedDriver.findElements(FIRST_NAME).isEmpty();
    }
}
