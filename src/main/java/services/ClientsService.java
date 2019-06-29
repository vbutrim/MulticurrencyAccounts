package services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import helpers.Currency;
import storage.BankStorage;
import storage.data.Client;

import java.util.List;

@Singleton
public final class ClientsService {

    private final BankStorage bankStorage;

    @Inject
    public ClientsService(BankStorage bankStorage) {
        this.bankStorage = bankStorage;
    }

    public long registerNewClient(String name, String passportId, Currency ccy) {
        return this.bankStorage.registerNewClient(name, passportId, ccy);
    }

    public List<Client> getAllClients() {
        return this.bankStorage.getAllClients();
    }

    public Client getExistingClientById(Long clientId) {
        return this.bankStorage.getClientById(clientId);
    }
}
