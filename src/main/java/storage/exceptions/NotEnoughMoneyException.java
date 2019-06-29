package storage.exceptions;

public class NotEnoughMoneyException extends RuntimeException {

    public NotEnoughMoneyException(long id) {
        super(String.format("Request failed. Not enough money in Account %s", id));
    }
}
