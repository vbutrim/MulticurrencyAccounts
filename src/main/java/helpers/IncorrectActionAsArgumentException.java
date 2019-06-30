package helpers;

class IncorrectActionAsArgumentException extends RuntimeException {

    IncorrectActionAsArgumentException(String value) {
        super(String.format("Incorrect Action '%s' as an Argument", value));
    }
}
