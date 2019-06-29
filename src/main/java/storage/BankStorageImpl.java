package storage;

import com.google.inject.Singleton;
import storage.data.Account;
import storage.data.Client;
import storage.exceptions.AccountNotFoundException;
import storage.exceptions.AccountWithSuchCcyAlreadyExistsException;
import storage.exceptions.ClientAlreadyExistsException;
import helpers.Currency;
import storage.exceptions.ClientNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public final class BankStorageImpl implements BankStorage {

    private final Map<String, Client> clientsDatabase = new HashMap<>();
    private final Map<Long, String> clientNamePerId = new HashMap<>();

    private final Map<Long, Account> accountsDatabase = new HashMap<>();

    BankStorageImpl() {
    }

    /*
     * Clients features
     */

    @Override
    public long registerNewClient(String name, String passportId, Currency ccyOfInitialAccount) {
        if (clientsDatabase.containsKey(name)) {
            throw new ClientAlreadyExistsException(name);
        }

        Account accountForNewClient = new Account(ccyOfInitialAccount);
        Client newClient = new Client(name, passportId, ccyOfInitialAccount, accountForNewClient.getId());

        accountsDatabase.put(accountForNewClient.getId(), accountForNewClient);
        clientsDatabase.put(name, newClient);
        clientNamePerId.put(newClient.getId(), name);

        return newClient.getId();
    }

    @Override
    public List<Client> getAllClients() {
        return new ArrayList<>(clientsDatabase.values());
    }

    @Override
    public Client getClientById(Long id) {
        if (id == null || !clientNamePerId.containsKey(id)) {
            throw new ClientNotFoundException(id);
        }

        Client foundClient = clientsDatabase.getOrDefault(clientNamePerId.get(id), null);

        if (foundClient == null) {
            throw new ClientNotFoundException(id);
        }

        return foundClient;
    }

    /*
     * Accounts features
     */

    @Override
    public long createAccountForClient(String name, Currency ccy) {
        Client client = clientsDatabase.getOrDefault(name, null);

        if (client == null) {
            throw new ClientNotFoundException(name);
        }

        if (client.getOpenedAccounts().containsKey(ccy)) {
            throw new AccountWithSuchCcyAlreadyExistsException(name, ccy);
        }

        Account newAccount = new Account(ccy);
        client.addAccount(ccy, newAccount.getId());
        accountsDatabase.put(newAccount.getId(), newAccount);

        return newAccount.getId();
    }

    @Override
    public void withdrawCashFromAccountOfClient(String name, Currency ccy, long cash) {
        getAccountOfClient(name, ccy).withdraw(cash);
    }

    @Override
    public void topUpAccountBalanceOfClient(String name, Currency ccy, long amount) {
        getAccountOfClient(name, ccy).topUp(amount);
    }

    private Account getAccountOfClient(String name, Currency ccy) {
        Client client = clientsDatabase.getOrDefault(name, null);

        if (client == null) {
            throw new ClientNotFoundException(name);
        }

        Long accountId = client.getOpenedAccounts().getOrDefault(ccy, null);

        if (accountId == null) {
            throw new AccountNotFoundException(name, ccy);
        }

        Account foundAccount = accountsDatabase.getOrDefault(accountId, null);

        if (foundAccount == null) {
            throw new AccountNotFoundException(accountId);
        }

        return foundAccount;
    }
}
