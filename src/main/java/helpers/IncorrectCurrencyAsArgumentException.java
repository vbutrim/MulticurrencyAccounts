package helpers;

public class IncorrectCurrencyAsArgumentException extends RuntimeException {

    public IncorrectCurrencyAsArgumentException(String value) {
        super(String.format("Incorrect Currency '%s' as an Argument", value));
    }
}
