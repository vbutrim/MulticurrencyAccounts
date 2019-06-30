package services;

import org.junit.Before;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;

public class TransactionsServiceTest {

    @Mock
    private ClientsService clientsService;

    @Before
    public void setUp() {
        clientsService = mock(ClientsService.class);
    }

    // TODO: provide more tests here

    private TransactionsService createInstance() {
        return new TransactionsService(clientsService);
    }
}
