package saucedemo.pages.pom;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public final class ProductsPage {
    private static final By TITLE = By.cssSelector("[data-test='title']");
    private static final By INVENTORY_LIST = By.cssSelector("[data-test='inventory-list']");
    private static final By PRODUCT_NAMES = By.cssSelector("[data-test='inventory-item-name']");
    private static final By PRODUCT_DESCRIPTIONS = By.cssSelector("[data-test='inventory-item-desc']");
    private static final By PRODUCT_PRICES = By.cssSelector("[data-test='inventory-item-price']");
    private static final By PRODUCT_IMAGES = By.cssSelector("[data-test='inventory-item'] img");
    private static final By ADD_BUTTONS = By.cssSelector("[data-test^='add-to-cart-']");
    private static final By SORT = By.cssSelector("[data-test='product-sort-container']");

    private ProductsPage() {
    }

    public static WebElement label_ProductsTitle(WebDriver sharedDriver) {
        return sharedDriver.findElement(TITLE);
    }

    public static WebElement panel_InventoryList(WebDriver sharedDriver) {
        return sharedDriver.findElement(INVENTORY_LIST);
    }

    public static List<WebElement> labels_ProductNames(WebDriver sharedDriver) {
        return sharedDriver.findElements(PRODUCT_NAMES);
    }

    public static List<WebElement> labels_ProductDescriptions(WebDriver sharedDriver) {
        return sharedDriver.findElements(PRODUCT_DESCRIPTIONS);
    }

    public static List<WebElement> labels_ProductPrices(WebDriver sharedDriver) {
        return sharedDriver.findElements(PRODUCT_PRICES);
    }

    public static List<WebElement> images_Products(WebDriver sharedDriver) {
        return sharedDriver.findElements(PRODUCT_IMAGES);
    }

    public static List<WebElement> buttons_AddToCart(WebDriver sharedDriver) {
        return sharedDriver.findElements(ADD_BUTTONS);
    }

    public static WebElement dropdown_SortProducts(WebDriver sharedDriver) {
        return sharedDriver.findElement(SORT);
    }

    public static WebElement button_AddToCart(WebDriver sharedDriver, String productId) {
        return sharedDriver.findElement(
                By.cssSelector("[data-test='add-to-cart-" + productId + "']"));
    }

    public static WebElement button_RemoveFromProducts(WebDriver sharedDriver, String productId) {
        return sharedDriver.findElement(
                By.cssSelector("[data-test='remove-" + productId + "']"));
    }

    public static boolean button_AddToCart_IsPresent(WebDriver sharedDriver, String productId) {
        return !sharedDriver.findElements(
                By.cssSelector("[data-test='add-to-cart-" + productId + "']")).isEmpty();
    }

    public static boolean button_Remove_IsPresent(WebDriver sharedDriver, String productId) {
        return !sharedDriver.findElements(
                By.cssSelector("[data-test='remove-" + productId + "']")).isEmpty();
    }

    public static boolean image_IsLoaded(WebDriver sharedDriver, WebElement image) {
        if (!(sharedDriver instanceof JavascriptExecutor javascriptExecutor)) {
            throw new IllegalStateException("WebDriver does not support JavaScript image checks");
        }
        Object result = javascriptExecutor.executeScript(
                "return arguments[0].complete && arguments[0].naturalWidth > 0;", image);
        return Boolean.TRUE.equals(result);
    }
}
