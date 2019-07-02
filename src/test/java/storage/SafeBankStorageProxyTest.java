package storage;

import helpers.Currency;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import storage.data.Account;
import storage.data.Client;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SafeBankStorageProxyTest {

    private static final String CLIENT_NAME = "Chuck Taylor";
    private static final String PASSPORT_ID = "24June1901";
    private static final Currency CURRENCY = Currency.DEFAULT_VALUE;

    @Mock
    private BankStorageImpl nonSafeBankStorage;

    @Before
    public void setup() {
        nonSafeBankStorage = mock(BankStorageImpl.class);
    }

    @Test
    public void shouldRegisterNewClientWithBankStorageImpl() {
        // Given
        SafeBankStorageProxy bankStorageProxy = createInstance();
        long expectedClientId = 1L;
        when(nonSafeBankStorage.registerNewClient(CLIENT_NAME, PASSPORT_ID, CURRENCY)).thenReturn(expectedClientId);

        // When
        long actualId = bankStorageProxy.registerNewClient(CLIENT_NAME, PASSPORT_ID, CURRENCY);

        // Then
        Assert.assertEquals(expectedClientId, actualId);
        verify(nonSafeBankStorage, times(1)).registerNewClient(any(String.class), any(String.class), any(Currency.class));
        verify(nonSafeBankStorage, times(1)).registerNewClient(CLIENT_NAME, PASSPORT_ID, CURRENCY);
    }

    @Test
    public void shouldCloseClientAndAccountsWithNoMoneyWithBankStorageImpl() {
        // Given
        SafeBankStorageProxy bankStorageProxy = createInstance();
        long clientId = 1L;

        // When
        bankStorageProxy.closeClientAndAccountsWithNoMoney(clientId);

        // Then
        verify(nonSafeBankStorage, times(1)).closeClientAndAccountsWithNoMoney(any(Long.class));
        verify(nonSafeBankStorage, times(1)).closeClientAndAccountsWithNoMoney(clientId);
    }

    @Test
    public void shouldGetAllClientsByBankStorageImpl() {
        // Given
        SafeBankStorageProxy bankStorageProxy = createInstance();
        List<Client> expectedClients = Collections.emptyList();
        when(nonSafeBankStorage.getAllClients()).thenReturn(expectedClients);

        // When
        List<Client> actualClients = bankStorageProxy.getAllClients();

        // Then
        Assert.assertEquals(expectedClients, actualClients);
        verify(nonSafeBankStorage, times(1)).getAllClients();
        verify(nonSafeBankStorage, times(1)).getAllClients();
    }

    @Test
    public void shouldGetClientByIdByBankStorageImpl() {
        // Given
        SafeBankStorageProxy bankStorageProxy = createInstance();
        Client expectedClient = mock(Client.class);
        Long clientId = 1L;
        when(nonSafeBankStorage.getClientById(clientId)).thenReturn(expectedClient);

        // When
        Client actualClient = bankStorageProxy.getClientById(clientId);

        // Then
        Assert.assertEquals(expectedClient, actualClient);
        verify(nonSafeBankStorage, times(1)).getClientById(any(Long.class));
        verify(nonSafeBankStorage, times(1)).getClientById(clientId);
    }

    @Test
    public void shouldCreateAccountForClientWithBankStorageImpl() {
        // Given
        SafeBankStorageProxy bankStorageProxy = createInstance();
        long expectedAccountId = 1L;
        when(nonSafeBankStorage.createAccountForClient(CLIENT_NAME, CURRENCY)).thenReturn(expectedAccountId);

        // When
        long actualAccountId = bankStorageProxy.createAccountForClient(CLIENT_NAME, CURRENCY);

        // Then
        Assert.assertEquals(expectedAccountId, actualAccountId);
        verify(nonSafeBankStorage, times(1)).createAccountForClient(any(String.class), any(Currency.class));
        verify(nonSafeBankStorage, times(1)).createAccountForClient(CLIENT_NAME, CURRENCY);
    }

    @Test
    public void shouldGetAccountOfClientByBankStorageImpl() {
        // Given
        SafeBankStorageProxy bankStorageProxy = createInstance();
        Account expectedAccount = mock(Account.class);
        when(nonSafeBankStorage.getAccountOfClient(CLIENT_NAME, CURRENCY)).thenReturn(expectedAccount);

        // When
        Account actualAccount = bankStorageProxy.getAccountOfClient(CLIENT_NAME, CURRENCY);

        // Then
        Assert.assertEquals(expectedAccount, actualAccount);
        verify(nonSafeBankStorage, times(1)).getAccountOfClient(any(String.class), any(Currency.class));
        verify(nonSafeBankStorage, times(1)).getAccountOfClient(CLIENT_NAME, CURRENCY);
    }

    private SafeBankStorageProxy createInstance() {
        return new SafeBankStorageProxy(nonSafeBankStorage);
    }
}
