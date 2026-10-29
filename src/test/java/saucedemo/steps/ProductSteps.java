package saucedemo.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import saucedemo.driver.SharedDriver;
import saucedemo.pages.pom.ProductsPage;
import saucedemo.support.PriceParser;
import saucedemo.support.ProductIds;
import saucedemo.support.UiWait;

public class ProductSteps {
    private final SharedDriver sharedDriver;

    public ProductSteps(SharedDriver sharedDriver) {
        this.sharedDriver = sharedDriver;
    }

    @Then("every product has a name, description, loaded image, price, and Add to Cart control")
    public void everyProductHasCompleteVisibleInformation() {
        UiWait.untilVisible(sharedDriver, ProductsPage::panel_InventoryList);

        List<WebElement> names = ProductsPage.labels_ProductNames(sharedDriver.get());
        List<WebElement> descriptions = ProductsPage.labels_ProductDescriptions(sharedDriver.get());
        List<WebElement> prices = ProductsPage.labels_ProductPrices(sharedDriver.get());
        List<WebElement> images = ProductsPage.images_Products(sharedDriver.get());
        List<WebElement> addButtons = ProductsPage.buttons_AddToCart(sharedDriver.get());

        assertFalse(names.isEmpty(), "The Products page should contain products");
        assertEquals(names.size(), descriptions.size(), "Every product should have a description");
        assertEquals(names.size(), prices.size(), "Every product should have a price");
        assertEquals(names.size(), images.size(), "Every product should have an image");
        assertEquals(names.size(), addButtons.size(), "Every product should have an Add to Cart control");

        names.forEach(element -> assertFalse(element.getText().isBlank(), "Product name should not be blank"));
        descriptions.forEach(element ->
                assertFalse(element.getText().isBlank(), "Product description should not be blank"));
        prices.forEach(element ->
                assertTrue(PriceParser.parse(element.getText()).compareTo(BigDecimal.ZERO) > 0,
                        "Product price should be positive"));
        images.forEach(image -> {
            assertTrue(image.isDisplayed(), "Product image should be visible");
            assertTrue(ProductsPage.image_IsLoaded(sharedDriver.get(), image),
                    "Product image should load successfully");
        });
        addButtons.forEach(button -> {
            assertTrue(button.isDisplayed(), "Add to Cart control should be visible");
            assertTrue(button.isEnabled(), "Add to Cart control should be enabled");
        });
    }

    @When("the user sorts products by {string}")
    public void theUserSortsProductsBy(String sortOption) {
        WebElement dropdown = UiWait.untilVisible(sharedDriver, ProductsPage::dropdown_SortProducts);
        new Select(dropdown).selectByVisibleText(sortOption);
    }

    @Then("product names are ordered {string}")
    public void productNamesAreOrdered(String direction) {
        List<String> actual = textValues(ProductsPage.labels_ProductNames(sharedDriver.get()));
        List<String> expected = new ArrayList<>(actual);
        Comparator<String> comparator = String.CASE_INSENSITIVE_ORDER;
        expected.sort("descending".equalsIgnoreCase(direction) ? comparator.reversed() : comparator);
        assertEquals(expected, actual, "Product names are not ordered " + direction);
    }

    @Then("product prices are ordered {string}")
    public void productPricesAreOrdered(String direction) {
        List<BigDecimal> actual = ProductsPage.labels_ProductPrices(sharedDriver.get()).stream()
                .map(WebElement::getText)
                .map(PriceParser::parse)
                .toList();
        List<BigDecimal> expected = new ArrayList<>(actual);
        Comparator<BigDecimal> comparator = Comparator.naturalOrder();
        expected.sort("descending".equalsIgnoreCase(direction) ? comparator.reversed() : comparator);
        assertEquals(expected, actual, "Product prices are not ordered " + direction);
    }

    @Given("{string} is in the cart")
    @When("the user adds {string} to the cart")
    public void theUserAddsProductToTheCart(String productName) {
        String productId = ProductIds.fromDisplayName(productName);
        UiWait.untilClickable(
                sharedDriver,
                driver -> ProductsPage.button_AddToCart(driver, productId)).click();
        sharedDriver.waitFor().until(
                driver -> ProductsPage.button_Remove_IsPresent(driver, productId));
    }

    @Given("the following products are in the cart:")
    public void theFollowingProductsAreInTheCart(DataTable products) {
        products.asList().forEach(this::theUserAddsProductToTheCart);
    }

    @When("the user removes {string} from the Products page")
    public void theUserRemovesProductFromProductsPage(String productName) {
        String productId = ProductIds.fromDisplayName(productName);
        UiWait.untilClickable(
                sharedDriver,
                driver -> ProductsPage.button_RemoveFromProducts(driver, productId)).click();
        sharedDriver.waitFor().until(
                driver -> ProductsPage.button_AddToCart_IsPresent(driver, productId));
    }

    @Then("the Products page does not offer another Add to Cart action for {string}")
    public void productsPageDoesNotOfferDuplicateAdd(String productName) {
        String productId = ProductIds.fromDisplayName(productName);
        assertTrue(ProductsPage.button_Remove_IsPresent(sharedDriver.get(), productId),
                "The selected product should expose Remove after being added");
        assertFalse(ProductsPage.button_AddToCart_IsPresent(sharedDriver.get(), productId),
                "The selected product should not expose another Add to Cart action");
    }

    private List<String> textValues(List<WebElement> elements) {
        return elements.stream().map(WebElement::getText).toList();
    }
}
