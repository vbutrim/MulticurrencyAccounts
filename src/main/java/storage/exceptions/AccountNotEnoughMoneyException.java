package storage.exceptions;

public class AccountNotEnoughMoneyException extends RuntimeException {

    public AccountNotEnoughMoneyException(long id) {
        super(String.format("Not enough money on Account %s", id));
    }
}
