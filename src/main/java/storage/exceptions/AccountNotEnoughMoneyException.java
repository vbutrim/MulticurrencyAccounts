package storage.exceptions;

public class AccountNotEnoughMoneyException extends RuntimeException {

    public AccountNotEnoughMoneyException(String clientName, String ccy) {
        super(String.format("Not enough money on '%s's '%s' account", clientName, ccy));
    }
}
