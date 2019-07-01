package helpers.exceptions;

public class IncorrectActionAsArgumentException extends RuntimeException {

    public IncorrectActionAsArgumentException(String value) {
        super(String.format("Incorrect Action '%s' as an Argument", value));
    }
}
