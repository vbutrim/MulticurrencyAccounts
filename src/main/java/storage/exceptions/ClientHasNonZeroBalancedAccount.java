package storage.exceptions;

public class ClientHasNonZeroBalancedAccount extends RuntimeException {

    public ClientHasNonZeroBalancedAccount(String name) {
        super(String.format("Client '%s' has account with non zero balance. Please, withdraw all cash before close accounts", name));
    }
}
