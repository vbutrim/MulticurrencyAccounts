package helpers;

public enum Currency {
    USD("USD"),
    EUR("EUR"),
    GBP("GBP"),
    RUB("RUB");

    private final String text;

    Currency(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }
}
