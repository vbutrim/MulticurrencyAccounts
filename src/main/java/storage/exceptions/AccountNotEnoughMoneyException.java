package storage.exceptions;

public class AccountNotEnoughMoneyException extends RuntimeException {

    public AccountNotEnoughMoneyException(long id) {
        super(String.format("Request failed. Not enough money in Account %s", id));
    }
}
