package services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import database.DBException;
import database.datasets.ClientsDataSet;

@Singleton
public class ClientsService {
    private final DBService dbService;

    @Inject
    public ClientsService(DBService dbService) {
        this.dbService = dbService;
    }

    public Long registerNewClient(String name, String passportId) throws DBException {
        try {
            if (getExistingClientId(name) != null) {
                return null;
            }
            return dbService.registerClient(name, passportId);
        } catch (DBException e) {
            throw new DBException(e);
        }
    }

    public ClientsDataSet getExistingClientId(String name) {
        try {
            return dbService.getClient(name);
        } catch (DBException e) {
            //
            return null;
        }
    }
}
