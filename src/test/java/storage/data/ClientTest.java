package storage.data;

import helpers.Currency;
import helpers.GlobalIds;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import storage.exceptions.AccountWithSuchCcyAlreadyExistsException;

import java.util.Map;

public class ClientTest {

    private static final String CLIENT_NAME = "Blackrock";
    private static final String CLIENT_PASSPORT_ID = "48156K";

    @Before
    public void setUp() {
        GlobalIds.ClientIdentifier.set(100);
    }

    @Test
    public void shouldCreateClientWithAccountAndCorrectId() {
        // Given

        // When
        Client client = createInstance();

        // Then
        Assert.assertEquals(CLIENT_NAME, client.getName());
        Assert.assertEquals(CLIENT_PASSPORT_ID, client.getPassportId());
        Assert.assertEquals(100, client.getId());
        Assert.assertEquals(101, GlobalIds.ClientIdentifier.get());

        Map<Currency, Long> accounts = client.getOpenedAccounts();

        Assert.assertEquals(1, accounts.size());
        Assert.assertTrue(accounts.containsKey(Currency.DEFAULT_VALUE));
        Assert.assertTrue(accounts.containsValue(1010L));
    }

    @Test
    public void shouldReturnAllAccountsOnOpenedAccounts() {
        // Given
        Client client = createInstance();
        client.addAccount(Currency.GBP, 1011);
        client.addAccount(Currency.USD, 1012);
        client.addAccount(Currency.RUB, 1013);

        // When
        Map<Currency, Long> accounts = client.getOpenedAccounts();

        // Then
        Assert.assertEquals(4, accounts.size());
        Assert.assertTrue(accounts.containsKey(Currency.EUR));
        Assert.assertTrue(accounts.containsKey(Currency.GBP));
        Assert.assertTrue(accounts.containsKey(Currency.USD));
        Assert.assertTrue(accounts.containsKey(Currency.RUB));
    }

    @Test
    public void shouldAddAccount() {
        // Given
        Client client = createInstance();

        // When
        client.addAccount(Currency.GBP, 1011);

        // Then
        Map<Currency, Long> accounts = client.getOpenedAccounts();
        Assert.assertEquals(2, accounts.size());
        Assert.assertTrue(accounts.containsKey(Currency.GBP));
        Assert.assertTrue(accounts.containsValue(1011L));
    }

    @Test(expected = AccountWithSuchCcyAlreadyExistsException.class)
    public void shouldNotAddAccountWithConflictCcy() {
        // Given
        Client client = createInstance();

        // When
        client.addAccount(Currency.DEFAULT_VALUE, 1011);

        // Then
    }

    private Client createInstance() {
        return new Client(CLIENT_NAME, CLIENT_PASSPORT_ID, Currency.DEFAULT_VALUE, 1010);
    }
}
