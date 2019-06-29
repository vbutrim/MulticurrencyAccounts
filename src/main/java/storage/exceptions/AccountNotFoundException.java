package storage.exceptions;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(Long id) {
        super(String.format("Account with id '%s' not found", id));
    }
}
