package storage;

import helpers.Currency;
import helpers.GlobalIds;
import org.junit.Assert;
import org.junit.Test;
import storage.data.Account;
import storage.data.Client;
import storage.exceptions.AccountNotFoundException;
import storage.exceptions.AccountWithSuchCcyAlreadyExistsException;
import storage.exceptions.ClientAlreadyExistsException;
import storage.exceptions.ClientNotFoundException;

import java.util.List;
import java.util.Map;

public class BankStorageImplTest {

    private final static String CLIENT_NAME = "Chuck Berry";
    private final static String PASSPORT_ID = "JohnyB.Goode1958";

    @Test
    public void shouldCreateStorageWithEmptyClientsOnStart() {
        // Given

        // When
        BankStorage bankStorage = createInstance();

        // Then
        Assert.assertEquals(0, bankStorage.getAllClients().size());
    }

    @Test
    public void shouldRegisterNewClient() {
        // Given
        BankStorage bankStorage = createInstance();

        // When
        bankStorage.registerNewClient(CLIENT_NAME, PASSPORT_ID, Currency.DEFAULT_VALUE);

        // Then
        List<Client> clients = bankStorage.getAllClients();
        Assert.assertEquals(1, clients.size());

        Client addedClient = clients.get(0);
        Assert.assertEquals(CLIENT_NAME, addedClient.getName());
        Assert.assertEquals(PASSPORT_ID, addedClient.getPassportId());

        Map<Currency, Long> accountsPerClient = addedClient.getOpenedAccounts();
        Assert.assertEquals(1, accountsPerClient.size());
        Assert.assertTrue(accountsPerClient.containsKey(Currency.DEFAULT_VALUE));
    }

    @Test(expected = ClientAlreadyExistsException.class)
    public void shouldNotRegisterNewClientWithExistingName() {
        // Given
        BankStorage bankStorage = createInstance();
        bankStorage.registerNewClient(CLIENT_NAME, PASSPORT_ID, Currency.DEFAULT_VALUE);

        // When
        bankStorage.registerNewClient(CLIENT_NAME, "JohnyCash1932", Currency.EUR);

        // Then
    }

    @Test
    public void shouldFindClientById() {
        // Given
        BankStorage bankStorage = createInstance();
        GlobalIds.ClientIdentifier.set(1000);
        bankStorage.registerNewClient(CLIENT_NAME, PASSPORT_ID, Currency.DEFAULT_VALUE);

        // When
        Client actualClient = bankStorage.getClientById(1000L);

        // Then
        Assert.assertNotNull(actualClient);
        Assert.assertEquals(CLIENT_NAME, actualClient.getName());
    }


    @Test(expected = ClientNotFoundException.class)
    public void shouldNotSearchClientWithEmptyAndNullId() {
        // Given
        BankStorage bankStorage = createInstance();

        // When
        bankStorage.getClientById(0L);

        // Then
    }

    @Test(expected = ClientNotFoundException.class)
    public void shouldNotSearchClientWithNullId() {
        // Given
        BankStorage bankStorage = createInstance();

        // When
        bankStorage.getClientById(null);

        // Then
    }

    @Test
    public void shouldReturnAccountOfClient() {
        // Given
        BankStorage bankStorage = createInstance();
        bankStorage.registerNewClient(CLIENT_NAME, PASSPORT_ID, Currency.DEFAULT_VALUE);

        // When
        Account actualAccount = bankStorage.getAccountOfClient(CLIENT_NAME, Currency.DEFAULT_VALUE);

        // Then
        Client actualClient = bankStorage.getAllClients().get(0);
        long expectedAccountId = actualClient.getOpenedAccounts().get(Currency.DEFAULT_VALUE);

        Assert.assertEquals(expectedAccountId, actualAccount.getId());
        Assert.assertEquals(Currency.DEFAULT_VALUE, actualAccount.getCcy());
        Assert.assertEquals(0L, actualAccount.getBalance());
    }

    @Test(expected = ClientNotFoundException.class)
    public void shouldNotReturnAccountIfClientDoesNotExist() {
        // Given
        BankStorage bankStorage = createInstance();
        bankStorage.registerNewClient(CLIENT_NAME, PASSPORT_ID, Currency.DEFAULT_VALUE);

        // When
        Account actualAccount = bankStorage.getAccountOfClient("JohnyCash1932", Currency.DEFAULT_VALUE);

        // Then
    }

    @Test(expected = AccountNotFoundException.class)
    public void shouldNotReturnAccountIfAccountWithCcyDoesNotExist() {
        // Given
        BankStorage bankStorage = createInstance();
        bankStorage.registerNewClient(CLIENT_NAME, PASSPORT_ID, Currency.DEFAULT_VALUE);

        // When
        Account actualAccount = bankStorage.getAccountOfClient(CLIENT_NAME, Currency.RUB);

        // Then
    }

    @Test
    public void shouldCreateAccountForClient() {
        // Given
        BankStorage bankStorage = createInstance();
        bankStorage.registerNewClient(CLIENT_NAME, PASSPORT_ID, Currency.DEFAULT_VALUE);

        // When
        long returningAccountId = bankStorage.createAccountForClient(CLIENT_NAME, Currency.RUB);

        // Then
        Account actualAccount = bankStorage.getAccountOfClient(CLIENT_NAME, Currency.RUB);
        Assert.assertEquals(actualAccount.getId(), returningAccountId);
        Assert.assertNotNull(actualAccount);
        Assert.assertEquals(Currency.RUB, actualAccount.getCcy());
        Assert.assertEquals(0L, actualAccount.getBalance());
    }

    @Test(expected = AccountWithSuchCcyAlreadyExistsException.class)
    public void shouldNotCreateAccountForClientIfCcyAlreadyExists() {
        // Given
        BankStorage bankStorage = createInstance();
        bankStorage.registerNewClient(CLIENT_NAME, PASSPORT_ID, Currency.DEFAULT_VALUE);

        // When
        bankStorage.createAccountForClient(CLIENT_NAME, Currency.DEFAULT_VALUE);

        // Then
    }

    @Test
    public void shouldTopUpOnClientsAccount() {
        // Given
        BankStorage bankStorage = createInstance();
        bankStorage.registerNewClient(CLIENT_NAME, PASSPORT_ID, Currency.DEFAULT_VALUE);

        // When
        bankStorage.topUpAccountBalanceOfClient(CLIENT_NAME, Currency.DEFAULT_VALUE, 100L);

        // Then
        Account actualAccount = bankStorage.getAccountOfClient(CLIENT_NAME, Currency.DEFAULT_VALUE);
        Assert.assertEquals(100L, actualAccount.getBalance());
    }

    @Test
    public void shouldWithdrawCashOfClientsAccount() {
        // Given
        BankStorage bankStorage = createInstance();
        bankStorage.registerNewClient(CLIENT_NAME, PASSPORT_ID, Currency.DEFAULT_VALUE);
        bankStorage.topUpAccountBalanceOfClient(CLIENT_NAME, Currency.DEFAULT_VALUE, 100L);

        // When
        bankStorage.withdrawCashFromAccountOfClient(CLIENT_NAME, Currency.DEFAULT_VALUE, 10L);

        // Then
        Account actualAccount = bankStorage.getAccountOfClient(CLIENT_NAME, Currency.DEFAULT_VALUE);
        Assert.assertEquals(90L, actualAccount.getBalance());
    }

    private BankStorage createInstance() {
        return new BankStorageImpl();
    }
}
