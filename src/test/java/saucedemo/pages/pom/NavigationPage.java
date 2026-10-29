package saucedemo.pages.pom;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public final class NavigationPage {
    private static final By CART = By.cssSelector("[data-test='shopping-cart-link']");
    private static final By CART_BADGE = By.cssSelector("[data-test='shopping-cart-badge']");
    private static final By MENU = By.id("react-burger-menu-btn");
    private static final By LOGOUT = By.cssSelector("[data-test='logout-sidebar-link']");

    private NavigationPage() {
    }

    public static WebElement link_Cart(WebDriver sharedDriver) {
        return sharedDriver.findElement(CART);
    }

    public static WebElement label_CartBadge(WebDriver sharedDriver) {
        return sharedDriver.findElement(CART_BADGE);
    }

    public static int value_CartBadge(WebDriver sharedDriver) {
        List<WebElement> badges = sharedDriver.findElements(CART_BADGE);
        return badges.isEmpty() ? 0 : Integer.parseInt(badges.getFirst().getText());
    }

    public static WebElement button_Menu(WebDriver sharedDriver) {
        return sharedDriver.findElement(MENU);
    }

    public static WebElement link_Logout(WebDriver sharedDriver) {
        return sharedDriver.findElement(LOGOUT);
    }
}
