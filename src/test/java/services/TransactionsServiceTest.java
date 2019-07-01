package services;

import helpers.Currency;
import helpers.exceptions.AccountsMustBeDifferentOnTransferringException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import storage.data.Account;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TransactionsServiceTest {

    private static final String CLIENT_NAME = "Ernesto Gevara";
    private static final Currency CURRENCY = Currency.DEFAULT_VALUE;
    private static final String CLIENT_NAME_TO = "Fidel Castro ";

    @Mock
    private ClientsService clientsService;

    @Before
    public void setUp() {
        clientsService = mock(ClientsService.class);
    }

    @Test
    public void shouldCallWithdrawCashOfAccountWithGettingItFromClientsService() {
        // Given
        long withdrawCashAmount = 10L;
        Account expectedAccount = mock(Account.class);

        when(clientsService.getAccountOfClient(CLIENT_NAME, CURRENCY)).thenReturn(expectedAccount);
        doNothing().when(expectedAccount).withdraw(withdrawCashAmount);

        TransactionsService transactionsService = createInstance();

        // When
        transactionsService.withdrawCashFromAccountOfClient(CLIENT_NAME, CURRENCY, 10L);

        // Then
        verify(clientsService, times(1)).getAccountOfClient(any(String.class), any(Currency.class));
        verify(clientsService, times(1)).getAccountOfClient(CLIENT_NAME, CURRENCY);
        verify(expectedAccount, times(1)).withdraw(any(Long.class));
        verify(expectedAccount, times(1)).withdraw(withdrawCashAmount);
    }

    @Test
    public void shouldCallTopUpBalanceOfAccountWithGettingItFromClientsService() {
        // Given
        long topUpAmount = 10L;
        Account expectedAccount = mock(Account.class);

        when(clientsService.getAccountOfClient(CLIENT_NAME, CURRENCY)).thenReturn(expectedAccount);
        doNothing().when(expectedAccount).topUp(topUpAmount);

        TransactionsService transactionsService = createInstance();

        // When
        transactionsService.topUpAccountBalanceOfClient(CLIENT_NAME, CURRENCY, 10L);

        // Then
        verify(clientsService, times(1)).getAccountOfClient(any(String.class), any(Currency.class));
        verify(clientsService, times(1)).getAccountOfClient(CLIENT_NAME, CURRENCY);
        verify(expectedAccount, times(1)).topUp(any(Long.class));
        verify(expectedAccount, times(1)).topUp(topUpAmount);
    }

    @Test
    public void shouldWithdrawFromFirstAndTopUpOnSecondOnTransferMoneyBetweenAccounts()
    {
        // Given
        long transferAmount = 10L;
        Account accountFrom = mock(Account.class);
        Account accountTo = mock(Account.class);

        when(clientsService.getAccountOfClient(CLIENT_NAME, CURRENCY)).thenReturn(accountFrom);
        when(clientsService.getAccountOfClient(CLIENT_NAME_TO, CURRENCY)).thenReturn(accountTo);
        doNothing().when(accountFrom).withdraw(transferAmount);
        doNothing().when(accountTo).withdraw(transferAmount);

        TransactionsService transactionsService = createInstance();

        // When
        transactionsService.transferMoneyFromTo(CLIENT_NAME, CLIENT_NAME_TO, CURRENCY, transferAmount);

        // Then
        verify(clientsService, times(2)).getAccountOfClient(any(String.class), any(Currency.class));
        verify(clientsService, times(1)).getAccountOfClient(CLIENT_NAME, CURRENCY);
        verify(clientsService, times(1)).getAccountOfClient(CLIENT_NAME_TO, CURRENCY);
        verify(accountFrom, times(1)).withdraw(any(Long.class));
        verify(accountFrom, times(1)).withdraw(transferAmount);
        verify(accountTo, times(1)).topUp(any(Long.class));
        verify(accountTo, times(1)).topUp(transferAmount);
    }

    @Test(expected = AccountsMustBeDifferentOnTransferringException.class)
    public void shouldThrowExceptionOnMakingTransferringFromAndToTheSameAccount()
    {
        // Given
        TransactionsService transactionsService = createInstance();

        // When
        transactionsService.transferMoneyFromTo(CLIENT_NAME, CLIENT_NAME, CURRENCY, 10L);

        // Then
    }

    private TransactionsService createInstance() {
        return new TransactionsService(clientsService);
    }
}
