package saucedemo.pages.pom;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public final class CartPage {
    private static final By TITLE = By.cssSelector("[data-test='title']");
    private static final By CART_ITEMS = By.cssSelector("[data-test='inventory-item']");
    private static final By ITEM_NAMES = By.cssSelector("[data-test='inventory-item-name']");
    private static final By ITEM_PRICES = By.cssSelector("[data-test='inventory-item-price']");
    private static final By CHECKOUT = By.cssSelector("[data-test='checkout']");

    private CartPage() {
    }

    public static WebElement label_CartTitle(WebDriver sharedDriver) {
        return sharedDriver.findElement(TITLE);
    }

    public static List<WebElement> panels_CartItems(WebDriver sharedDriver) {
        return sharedDriver.findElements(CART_ITEMS);
    }

    public static List<WebElement> labels_CartItemNames(WebDriver sharedDriver) {
        return sharedDriver.findElements(ITEM_NAMES);
    }

    public static List<WebElement> labels_CartItemPrices(WebDriver sharedDriver) {
        return sharedDriver.findElements(ITEM_PRICES);
    }

    public static WebElement button_RemoveFromCart(WebDriver sharedDriver, String productId) {
        return sharedDriver.findElement(
                By.cssSelector("[data-test='remove-" + productId + "']"));
    }

    public static WebElement button_Checkout(WebDriver sharedDriver) {
        return sharedDriver.findElement(CHECKOUT);
    }

    public static boolean button_Checkout_IsPresent(WebDriver sharedDriver) {
        return !sharedDriver.findElements(CHECKOUT).isEmpty();
    }
}
