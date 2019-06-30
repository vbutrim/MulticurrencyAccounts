package storage.exceptions;

import helpers.Currency;

public class AccountWithSuchCcyAlreadyExistsException extends RuntimeException {

    public AccountWithSuchCcyAlreadyExistsException(long id, Currency ccy) {
        super(String.format("Client with id '%s' has already opened '%s' account", id, ccy));
    }

    public AccountWithSuchCcyAlreadyExistsException(String name, Currency ccy) {
        super(String.format("Client with name '%s' has already opened '%s' account", name, ccy));
    }
}
