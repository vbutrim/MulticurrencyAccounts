package helpers;

import helpers.exceptions.IncorrectCurrencyAsArgumentException;

public enum Currency {
    USD("USD"),
    EUR("EUR"),
    GBP("GBP"),
    RUB("RUB");

    public static final Currency DEFAULT_VALUE = Currency.EUR;

    private final String text;

    Currency(String text) {
        this.text = text;
    }

    public static boolean contains(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }

        try {
            valueOf(text);
            return true;
        } catch (IllegalArgumentException e) {
            throw new IncorrectCurrencyAsArgumentException(text);
        }
    }

    @Override
    public String toString() {
        return this.text;
    }
}
