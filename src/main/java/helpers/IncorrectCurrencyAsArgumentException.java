package helpers;

class IncorrectCurrencyAsArgumentException extends RuntimeException {

    IncorrectCurrencyAsArgumentException(String value) {
        super(String.format("Incorrect Currency '%s' as an Argument", value));
    }
}
