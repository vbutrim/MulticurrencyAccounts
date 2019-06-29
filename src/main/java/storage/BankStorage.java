package storage;

import storage.data.Account;
import storage.data.Client;
import helpers.Currency;

import java.util.List;

public interface BankStorage {

    long registerNewClient(String name, String passportId, Currency ccyOfInitialAccount);

    List<Client> getAllClients();

    Client getClientById(Long id);

    long createAccountForClient(String name, Currency ccy);

    Account getAccountOfClient(String name, Currency ccy);

    void withdrawCashFromAccountOfClient(String name, Currency ccy, long cash);

    void topUpAccountBalanceOfClient(String name, Currency ccy, long amount);
}
