package storage.data;

import helpers.Currency;
import helpers.GlobalIds;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import storage.exceptions.AccountBalanceLimitationException;
import storage.exceptions.AccountNotEnoughMoneyException;

public class AccountTest {

    @Before
    public void beforeStart() {
        GlobalIds.AccountIdentifier.set(100);
    }

    @Test
    public void shouldCreateAccountWithZeroBalanceAndCorrectId() {
        // Given

        // When
        Account account = createInstance();

        // Then
        Assert.assertEquals(0, account.getBalance());
        Assert.assertEquals(Currency.DEFAULT_VALUE, account.getCcy());
        Assert.assertEquals(100, account.getId());
        Assert.assertEquals(101, GlobalIds.AccountIdentifier.get());
    }

    @Test
    public void shouldTopUpAccount() {
        // Given
        Account account = createInstance();

        // When
        account.topUp(100);

        // Then
        Assert.assertEquals(100, account.getBalance());
    }

    @Test
    public void shouldTakeCashFromAccount() {
        // Given
        Account account = createInstance();
        account.topUp(100);

        // When
        account.withdraw(10);

        // Then
        Assert.assertEquals(90, account.getBalance());
    }

    @Test(expected = AccountNotEnoughMoneyException.class)
    public void shouldNotBeOverDrafted() {
        // Given
        Account account = createInstance();
        account.topUp(5);

        // When
        account.withdraw(10);

        // Then
    }

    @Test(expected = AccountBalanceLimitationException.class)
    public void shouldNotBeOverBalanced() {
        // Given
        Account account = createInstance();
        account.topUp(Long.MAX_VALUE);

        // When
        account.topUp(5);

        // Then
    }

    private Account createInstance() {
        return new Account(Currency.DEFAULT_VALUE);
    }
}
