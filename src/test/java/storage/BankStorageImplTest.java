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
import storage.exceptions.ClientHasNonZeroBalancedAccount;
import storage.exceptions.ClientNotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        BankStorageImpl bankStorage = createInstance();

        // When
        long actualClientId = bankStorage.registerNewClient(CLIENT_NAME, PASSPORT_ID, Currency.DEFAULT_VALUE);

        // Then
        List<Client> clients = bankStorage.getAllClients();
        Assert.assertEquals(1, clients.size());

        Client addedClient = clients.get(0);
        Assert.assertEquals(addedClient.getId(), actualClientId);
        Assert.assertEquals(CLIENT_NAME, addedClient.getName());
        Assert.assertEquals(PASSPORT_ID, addedClient.getPassportId());

        Map<Currency, Long> accountsPerClient = addedClient.getOpenedAccounts();
        Assert.assertEquals(1, accountsPerClient.size());
        Assert.assertTrue(accountsPerClient.containsKey(Currency.DEFAULT_VALUE));

        long actualAccountId = accountsPerClient.values().iterator().next();
        assertTrue(bankStorage.getAccountsDatabase().containsKey(actualAccountId));
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

    @Test()
    public void shouldDeleteClientAndAccountsFromStorageOnCloseClient() {
        // Given
        BankStorageImpl bankStorage = createInstance();
        long actualClientId = bankStorage.registerNewClient(CLIENT_NAME, PASSPORT_ID, Currency.DEFAULT_VALUE);
        bankStorage.createAccountForClient(CLIENT_NAME, Currency.RUB);
        Collection<Long> accountIds = bankStorage.getClientById(actualClientId).getOpenedAccounts().values();

        // When
        bankStorage.closeClientAndAccountsWithNoMoney(actualClientId);

        // Then
        assertThatThrownBy(() -> bankStorage.getClientById(actualClientId))
                .isInstanceOf(ClientNotFoundException.class);
        assertThatThrownBy(() -> bankStorage.getAccountOfClient(CLIENT_NAME, Currency.DEFAULT_VALUE))
                .isInstanceOf(ClientNotFoundException.class);
        assertThatThrownBy(() -> bankStorage.getAccountOfClient(CLIENT_NAME, Currency.RUB))
                .isInstanceOf(ClientNotFoundException.class);

        for (Long accountId : accountIds) {
            assertFalse(bankStorage.getAccountsDatabase().containsKey(accountId));
        }
    }

    @Test(expected = ClientNotFoundException.class)
    public void shouldNotDeleteClientIfNotExistOnCloseClient() {
        // Given
        BankStorageImpl bankStorage = createInstance();

        // When
        bankStorage.closeClientAndAccountsWithNoMoney(25L);

        // Then
    }

    @Test(expected = ClientHasNonZeroBalancedAccount.class)
    public void shouldNotDeleteClientIfHasNonZeroBalancedAccountOnCloseClient() {
        // Given
        BankStorageImpl bankStorage = createInstance();
        long actualClientId = bankStorage.registerNewClient(CLIENT_NAME, PASSPORT_ID, Currency.DEFAULT_VALUE);
        bankStorage.createAccountForClient(CLIENT_NAME, Currency.RUB);
        bankStorage.getAccountOfClient(CLIENT_NAME, Currency.RUB).topUp(100L);

        // When
        bankStorage.closeClientAndAccountsWithNoMoney(actualClientId);

        // Then
    }

    private BankStorageImpl createInstance() {
        return new BankStorageImpl();
    }
}
