package saucedemo.driver;

import java.util.Map;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import saucedemo.config.TestConfig;

public class SharedDriver {
    private static final Dimension DEFAULT_WINDOW_SIZE = new Dimension(1366, 900);

    private final TestConfig config;
    private WebDriver driver;

    public SharedDriver(TestConfig config) {
        this.config = config;
    }

    public void start() {
        if (driver != null) {
            throw new IllegalStateException("WebDriver has already been started for this scenario");
        }

        driver = switch (config.browserSelection()) {
            case "chrome" -> createChromeDriver();
            case "firefox" -> createFirefoxDriver();
            case "edge" -> createEdgeDriver();
            default -> throw new IllegalArgumentException(
                    "Unsupported browser.selection: " + config.browserSelection());
        };

        driver.manage().timeouts().pageLoadTimeout(config.pageLoadTimeout());
        driver.manage().timeouts().scriptTimeout(config.scriptTimeout());
        driver.manage().window().setSize(DEFAULT_WINDOW_SIZE);
    }

    public WebDriver get() {
        if (driver == null) {
            throw new IllegalStateException("WebDriver is not active for this scenario");
        }
        return driver;
    }

    public WebDriverWait waitFor() {
        return new WebDriverWait(get(), config.explicitWait());
    }

    public boolean isStarted() {
        return driver != null;
    }

    public byte[] screenshotBytes() {
        WebDriver activeDriver = get();
        if (!(activeDriver instanceof TakesScreenshot screenshotDriver)) {
            throw new IllegalStateException("Active WebDriver does not support screenshots");
        }
        return screenshotDriver.getScreenshotAs(OutputType.BYTES);
    }

    public String browserName() {
        WebDriver activeDriver = get();
        if (!(activeDriver instanceof HasCapabilities capabilitiesDriver)) {
            throw new IllegalStateException("Active WebDriver does not expose capabilities");
        }
        return capabilitiesDriver.getCapabilities().getBrowserName();
    }

    public int setViewportSize(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Viewport dimensions must be positive");
        }

        WebDriver activeDriver = get();
        if (activeDriver instanceof ChromiumDriver chromiumDriver) {
            chromiumDriver.executeCdpCommand(
                    "Emulation.setDeviceMetricsOverride",
                    Map.of(
                            "width", width,
                            "height", height,
                            "deviceScaleFactor", 1,
                            "mobile", false));
            return viewportWidth();
        }

        for (int attempt = 0; attempt < 3; attempt++) {
            Dimension outerSize = activeDriver.manage().window().getSize();
            int innerWidth = viewportWidth();
            int innerHeight = viewportHeight();
            int adjustedWidth = Math.max(width, outerSize.getWidth() + width - innerWidth);
            int adjustedHeight = Math.max(height, outerSize.getHeight() + height - innerHeight);
            activeDriver.manage().window().setSize(new Dimension(adjustedWidth, adjustedHeight));
            if (viewportWidth() == width && viewportHeight() == height) {
                return width;
            }
        }

        return viewportWidth();
    }

    public int viewportWidth() {
        return scriptNumber("return window.innerWidth;");
    }

    public int viewportHeight() {
        return scriptNumber("return window.innerHeight;");
    }

    public void quit() {
        if (driver == null) {
            return;
        }
        try {
            driver.quit();
        } finally {
            driver = null;
        }
    }

    private int scriptNumber(String script) {
        WebDriver activeDriver = get();
        if (!(activeDriver instanceof JavascriptExecutor javascriptExecutor)) {
            throw new IllegalStateException("Active WebDriver does not support JavaScript execution");
        }
        Object result = javascriptExecutor.executeScript(script);
        if (!(result instanceof Number number)) {
            throw new IllegalStateException("JavaScript did not return a number: " + script);
        }
        return number.intValue();
    }

    private WebDriver createChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications", "--no-default-browser-check");
        if (config.headless()) {
            options.addArguments("--headless=new");
        }
        return new ChromeDriver(options);
    }

    private WebDriver createFirefoxDriver() {
        FirefoxOptions options = new FirefoxOptions();
        if (config.headless()) {
            options.addArguments("-headless");
        }
        return new FirefoxDriver(options);
    }

    private WebDriver createEdgeDriver() {
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--disable-notifications", "--no-default-browser-check");
        if (config.headless()) {
            options.addArguments("--headless=new");
        }
        return new EdgeDriver(options);
    }
}
