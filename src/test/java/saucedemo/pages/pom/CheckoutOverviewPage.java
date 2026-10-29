package saucedemo.pages.pom;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public final class CheckoutOverviewPage {
    private static final By TITLE = By.cssSelector("[data-test='title']");
    private static final By ITEM_NAMES = By.cssSelector("[data-test='inventory-item-name']");
    private static final By ITEM_PRICES = By.cssSelector("[data-test='inventory-item-price']");
    private static final By SUBTOTAL = By.cssSelector("[data-test='subtotal-label']");
    private static final By TAX = By.cssSelector("[data-test='tax-label']");
    private static final By TOTAL = By.cssSelector("[data-test='total-label']");
    private static final By FINISH = By.cssSelector("[data-test='finish']");
    private static final By CANCEL = By.cssSelector("[data-test='cancel']");

    private CheckoutOverviewPage() {
    }

    public static WebElement label_CheckoutOverviewTitle(WebDriver sharedDriver) {
        return sharedDriver.findElement(TITLE);
    }

    public static List<WebElement> labels_OverviewItemNames(WebDriver sharedDriver) {
        return sharedDriver.findElements(ITEM_NAMES);
    }

    public static List<WebElement> labels_OverviewItemPrices(WebDriver sharedDriver) {
        return sharedDriver.findElements(ITEM_PRICES);
    }

    public static WebElement label_ItemSubtotal(WebDriver sharedDriver) {
        return sharedDriver.findElement(SUBTOTAL);
    }

    public static WebElement label_Tax(WebDriver sharedDriver) {
        return sharedDriver.findElement(TAX);
    }

    public static WebElement label_Total(WebDriver sharedDriver) {
        return sharedDriver.findElement(TOTAL);
    }

    public static WebElement button_Finish(WebDriver sharedDriver) {
        return sharedDriver.findElement(FINISH);
    }

    public static WebElement button_Cancel(WebDriver sharedDriver) {
        return sharedDriver.findElement(CANCEL);
    }

    public static boolean button_Finish_IsPresent(WebDriver sharedDriver) {
        return !sharedDriver.findElements(FINISH).isEmpty();
    }
}
