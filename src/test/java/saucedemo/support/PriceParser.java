package saucedemo.support;

import java.math.BigDecimal;

public final class PriceParser {
    private PriceParser() {
    }

    public static BigDecimal parse(String displayedValue) {
        if (displayedValue == null) {
            throw new IllegalArgumentException("Displayed price must not be null");
        }
        String numericValue = displayedValue.replaceAll("[^0-9.-]", "");
        if (numericValue.isBlank()) {
            throw new IllegalArgumentException("Displayed value does not contain a price: " + displayedValue);
        }
        return new BigDecimal(numericValue);
    }
}
