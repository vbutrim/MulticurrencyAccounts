package storage;

import com.google.inject.Singleton;
import storage.data.Account;
import storage.data.Client;
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

    public BankStorageImpl() {

    }

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
    public Client getClientById(Long id) throws ClientNotFoundException {
        if (id == null || !clientNamePerId.containsKey(id)) {
            throw new ClientNotFoundException(id);
        }

        Client foundedClient = clientsDatabase.getOrDefault(clientNamePerId.get(id), null);

        if (foundedClient == null) {
            throw new ClientNotFoundException(id);
        }

        return foundedClient;
    }
}
