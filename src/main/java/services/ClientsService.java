package services;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ClientsService {
    private final DBService dbService;

    @Inject
    public ClientsService(DBService dbService) {
        this.dbService = dbService;
    }
}
