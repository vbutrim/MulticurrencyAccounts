package services;

import helpers.Currency;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import storage.BankStorage;
import storage.data.Account;
import storage.data.Client;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ClientsServiceTest {

    private static final String CLIENT_NAME = "Robert Plant";
    private static final String PASSPORT_ID = "1948";
    private static final Currency CURRENCY = Currency.DEFAULT_VALUE;

    @Mock
    private BankStorage bankStorage;

    @Before
    public void setUp() {
        bankStorage = mock(BankStorage.class);
    }

    @Test
    public void shouldRegisterNewClientWithBankStorage() {
        // Given
        ClientsService clientsService = createInstance();

        final long expectedClientId = 1L;
        when(bankStorage.registerNewClient(CLIENT_NAME, PASSPORT_ID, CURRENCY)).thenReturn(expectedClientId);

        // When
        long actualClientId = clientsService.registerNewClient(CLIENT_NAME, PASSPORT_ID, CURRENCY);

        // Then
        Assert.assertEquals(expectedClientId, actualClientId);
        verify(bankStorage, times(1)).registerNewClient(any(String.class), any(String.class), any(Currency.class));
        verify(bankStorage, times(1)).registerNewClient(CLIENT_NAME, PASSPORT_ID, CURRENCY);
    }

    @Test
    public void shouldCloseClientAndAccountsWithNoMoneyWithBankStorage() {
        // Given
        ClientsService clientsService = createInstance();

        final long clientId = 1L;
        doNothing().when(bankStorage).closeClientAndAccountsWithNoMoney(clientId);

        // When
        clientsService.closeClientAndAccountsWithNoMoney(clientId);

        // Then
        verify(bankStorage, times(1)).closeClientAndAccountsWithNoMoney(any(Long.class));
        verify(bankStorage, times(1)).closeClientAndAccountsWithNoMoney(clientId);
    }

    @Test
    public void shouldReturnAllClientsFromBankStorage() {
        // Given
        ClientsService clientsService = createInstance();

        final List<Client> expectedListOfAllRegisteredClients = new ArrayList<>();
        when(bankStorage.getAllClients()).thenReturn(expectedListOfAllRegisteredClients);

        // When
        List<Client> actualListOfAllRegisteredClients = clientsService.getAllClients();

        // Then
        Assert.assertEquals(expectedListOfAllRegisteredClients, actualListOfAllRegisteredClients);
        verify(bankStorage, times(1)).getAllClients();
    }

    @Test
    public void shouldReturnExistingClientByIdFromBankStorage() {
        // Given
        ClientsService clientsService = createInstance();

        Client expectedClient = mock(Client.class);
        when(bankStorage.getClientById(1L)).thenReturn(expectedClient);

        // When
        Client actualClient = clientsService.getExistingClientById(1L);

        // Then
        Assert.assertEquals(expectedClient, actualClient);
        verify(bankStorage, times(1)).getClientById(any(Long.class));
        verify(bankStorage, times(1)).getClientById(1L);
    }

    @Test
    public void shouldCreateAccountForClientWithBankStorage() {
        // Given
        ClientsService clientsService = createInstance();

        long expectedAccountId = 1L;
        when(bankStorage.createAccountForClient(CLIENT_NAME, CURRENCY)).thenReturn(expectedAccountId);

        // When
        long actualAccountId = clientsService.createAccountForClient(CLIENT_NAME, CURRENCY);

        // Then
        Assert.assertEquals(expectedAccountId, actualAccountId);
        verify(bankStorage, times(1)).createAccountForClient(any(String.class), any(Currency.class));
        verify(bankStorage, times(1)).createAccountForClient(CLIENT_NAME, CURRENCY);
    }

    @Test
    public void shouldReturnAccountOfClientFromBankStorage() {
        // Given
        ClientsService clientsService = createInstance();

        Account expectedAccount = mock(Account.class);
        when(bankStorage.getAccountOfClient(CLIENT_NAME, CURRENCY)).thenReturn(expectedAccount);

        // When
        Account actualAccount = clientsService.getAccountOfClient(CLIENT_NAME, CURRENCY);

        // Then
        Assert.assertEquals(expectedAccount, actualAccount);
        verify(bankStorage, times(1)).getAccountOfClient(any(String.class), any(Currency.class));
        verify(bankStorage, times(1)).getAccountOfClient(CLIENT_NAME, CURRENCY);
    }

    private ClientsService createInstance() {
        return new ClientsService(bankStorage);
    }
}
