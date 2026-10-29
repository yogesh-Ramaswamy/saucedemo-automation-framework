package saucedemo.pages.pom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public final class CheckoutCompletePage {
    private static final By TITLE = By.cssSelector("[data-test='title']");
    private static final By CONFIRMATION_HEADER = By.cssSelector("[data-test='complete-header']");
    private static final By CONFIRMATION_TEXT = By.cssSelector("[data-test='complete-text']");
    private static final By BACK_HOME = By.cssSelector("[data-test='back-to-products']");

    private CheckoutCompletePage() {
    }

    public static WebElement label_CheckoutCompleteTitle(WebDriver sharedDriver) {
        return sharedDriver.findElement(TITLE);
    }

    public static WebElement label_ConfirmationHeader(WebDriver sharedDriver) {
        return sharedDriver.findElement(CONFIRMATION_HEADER);
    }

    public static WebElement label_ConfirmationText(WebDriver sharedDriver) {
        return sharedDriver.findElement(CONFIRMATION_TEXT);
    }

    public static WebElement button_BackHome(WebDriver sharedDriver) {
        return sharedDriver.findElement(BACK_HOME);
    }

    public static boolean confirmation_IsPresent(WebDriver sharedDriver) {
        return !sharedDriver.findElements(CONFIRMATION_HEADER).isEmpty();
    }
}
