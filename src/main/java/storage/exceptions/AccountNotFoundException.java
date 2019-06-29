package storage.exceptions;

import helpers.Currency;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(Long id) {
        super(String.format("Account with id '%s' not found", id));
    }

    public AccountNotFoundException(String name, Currency ccy) {
        super(String.format("'%S' account of '%s' not found", ccy, name));
    }
}
