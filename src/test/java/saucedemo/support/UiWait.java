package saucedemo.support;

import java.util.function.Function;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import saucedemo.driver.SharedDriver;

public final class UiWait {
    private UiWait() {
    }

    public static WebElement untilVisible(
            SharedDriver sharedDriver,
            Function<WebDriver, WebElement> finder) {
        return sharedDriver.waitFor().until(driver -> {
            try {
                WebElement element = finder.apply(driver);
                return element.isDisplayed() ? element : null;
            } catch (NoSuchElementException | StaleElementReferenceException exception) {
                return null;
            }
        });
    }

    public static WebElement untilClickable(
            SharedDriver sharedDriver,
            Function<WebDriver, WebElement> finder) {
        return sharedDriver.waitFor().until(driver -> {
            try {
                WebElement element = finder.apply(driver);
                return element.isDisplayed() && element.isEnabled() ? element : null;
            } catch (NoSuchElementException | StaleElementReferenceException exception) {
                return null;
            }
        });
    }

    public static void untilUrlContains(SharedDriver sharedDriver, String fragment) {
        sharedDriver.waitFor().until(ExpectedConditions.urlContains(fragment));
    }

    public static void untilText(
            SharedDriver sharedDriver,
            Function<WebDriver, WebElement> finder,
            String expectedText) {
        sharedDriver.waitFor().until(driver -> {
            try {
                return expectedText.equals(finder.apply(driver).getText());
            } catch (NoSuchElementException | StaleElementReferenceException exception) {
                return false;
            }
        });
    }
}
