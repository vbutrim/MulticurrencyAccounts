package helpers.exceptions;

public class AccountsMustBeDifferentOnTransferringException extends RuntimeException {

    public AccountsMustBeDifferentOnTransferringException() {
        super("Accounts must be different on transferring");
    }
}
