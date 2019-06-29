package storage;

import storage.data.Client;
import storage.helpers.Currency;

import java.util.List;

public interface BankStorage {

    long registerNewClient(String name, String passportId, Currency ccyOfInitialAccount);

    List<Client> getAllClients();

    Client getClientById(Long id);
}
