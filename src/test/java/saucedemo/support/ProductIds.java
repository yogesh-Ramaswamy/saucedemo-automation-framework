package saucedemo.support;

import java.util.Locale;

public final class ProductIds {
    private ProductIds() {
    }

    public static String fromDisplayName(String productName) {
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("Product name must not be blank");
        }
        return productName.trim()
                .toLowerCase(Locale.ROOT)
                .replace(' ', '-');
    }
}
