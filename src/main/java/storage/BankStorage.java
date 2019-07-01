package storage;

import storage.data.Account;
import storage.data.Client;
import helpers.Currency;

import java.util.List;

public interface BankStorage {

    long registerNewClient(String name, String passportId, Currency ccyOfInitialAccount);

    void closeClientAndAccountsWithZeroBalance(String name);

    List<Client> getAllClients();

    Client getClientById(Long id);

    long createAccountForClient(String name, Currency ccy);

    Account getAccountOfClient(String name, Currency ccy);

}
