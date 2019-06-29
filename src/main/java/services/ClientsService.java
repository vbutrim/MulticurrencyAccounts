package services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import database.DBException;
import database.datasets.ClientsDataSet;

import java.util.List;

@Singleton
public class ClientsService {

    private final DBService dbService;

    @Inject
    public ClientsService(DBService dbService) {
        this.dbService = dbService;
    }

    public Long registerNewClient(String name, String passportId) throws DBException {
        try {
            if (getExistingClientByName(name) != null) {
                return null;
            }
            return dbService.registerClient(name, passportId);
        } catch (DBException e) {
            throw new DBException(e);
        }
    }

    public List<ClientsDataSet> getAllClients() throws DBException {
        try {
            return dbService.getAllClients();
        } catch (DBException e) {
            throw new DBException(e);
        }
    }

    public ClientsDataSet getExistingClientById(Long clientId) {
        try {
            return dbService.getClientById(clientId);
        } catch (DBException e) {
            //
            return null;
        }
    }

    public ClientsDataSet getExistingClientByName(String name) {
        try {
            return dbService.getClientByName(name);
        } catch (DBException e) {
            //
            return null;
        }
    }
}
