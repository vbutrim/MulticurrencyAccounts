package storage.exceptions;


import storage.helpers.Currency;

public class AccountWithSuchCcyAlreadyExistsException extends RuntimeException {

    public AccountWithSuchCcyAlreadyExistsException(long id, Currency ccy) {
        super(String.format("Request failed. Client with id '%s' has already opened '%s' account", id, ccy));
    }
}
