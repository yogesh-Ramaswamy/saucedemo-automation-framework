package saucedemo.config;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Locale;
import java.util.Properties;

public class TestConfig {
    private static final String CONFIG_RESOURCE = "config.properties";

    private final Properties properties = new Properties();

    public TestConfig() {
        try (InputStream input = TestConfig.class.getClassLoader().getResourceAsStream(CONFIG_RESOURCE)) {
            if (input == null) {
                throw new IllegalStateException("Missing test configuration resource: " + CONFIG_RESOURCE);
            }
            properties.load(input);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load " + CONFIG_RESOURCE, exception);
        }
    }

    public String baseUrl() {
        String value = requiredValue("base.url");
        return value.endsWith("/") ? value : value + "/";
    }

    public String browserSelection() {
        return requiredValue("browser.selection").trim().toLowerCase(Locale.ROOT);
    }

    public boolean headless() {
        return Boolean.parseBoolean(requiredValue("browser.headless"));
    }

    public Duration explicitWait() {
        return Duration.ofSeconds(positiveLong("explicit.wait.seconds"));
    }

    public Duration pageLoadTimeout() {
        return Duration.ofSeconds(positiveLong("page.load.timeout.seconds"));
    }

    public Duration scriptTimeout() {
        return Duration.ofSeconds(positiveLong("script.timeout.seconds"));
    }

    public String standardUsername() {
        return requiredValue("standard.username");
    }

    public String standardPassword() {
        return requiredValue("standard.password");
    }

    public String lockedUsername() {
        return requiredValue("locked.username");
    }

    private long positiveLong(String key) {
        long value;
        try {
            value = Long.parseLong(requiredValue(key));
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Configuration value must be an integer: " + key, exception);
        }
        if (value <= 0) {
            throw new IllegalArgumentException("Configuration value must be positive: " + key);
        }
        return value;
    }

    private String requiredValue(String key) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue;
        }

        String environmentKey = key.toUpperCase(Locale.ROOT).replace('.', '_');
        String environmentValue = System.getenv(environmentKey);
        if (environmentValue != null && !environmentValue.isBlank()) {
            return environmentValue;
        }

        String propertyValue = properties.getProperty(key);
        if (propertyValue == null || propertyValue.isBlank()) {
            throw new IllegalStateException("Missing required configuration value: " + key);
        }
        return propertyValue;
    }
}
