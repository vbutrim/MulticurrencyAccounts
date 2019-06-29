package storage.exceptions;

public class AccountBalanceLimitationException extends RuntimeException {
    public AccountBalanceLimitationException(long id, long balance) {
        super(String.format("Request failed. Account with id '%s' is near limitation %s/%s", id, balance, Long.MAX_VALUE));
    }
}
