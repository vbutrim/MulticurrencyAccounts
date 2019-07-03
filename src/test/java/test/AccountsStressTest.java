package test;

import helpers.Currency;
import org.junit.Before;
import org.junit.Test;
import services.ClientsService;
import services.TransactionsService;
import storage.BankStorage;
import storage.BankStorageImpl;
import storage.SafeBankStorageProxy;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

/*
 * Just Modeling of how `Components` tests (running app with calling REST API using Cucumber, e.g.) could be look like
 */
public final class AccountsStressTest {

    private static final String CLIENT_NAME = "Tony Montana";
    private static final String PASSPORT_ID = "ScarFace1983";
    private static final Currency CCY = Currency.USD;
    private static final String CLIENT_NAME_FROM = "Manny Ribera";
    private static final String PASSPORT_ID_FROM = "LittleFriend";

    private static final int THREADS_COUNT = 1000;

    private ClientsService clientsService;
    private TransactionsService transactionsService;
    private ExecutorService executorService;

    @Before
    public void setUp() {
        BankStorage bankStorage = new SafeBankStorageProxy(new BankStorageImpl());
        clientsService = new ClientsService(bankStorage);
        transactionsService = new TransactionsService(clientsService);
        executorService = Executors.newFixedThreadPool(THREADS_COUNT);
    }

    @Test
    public void accountBalanceShouldBeZeroAfterConcurrentManipulations() {
        // Given
        clientsService.registerNewClient(CLIENT_NAME, PASSPORT_ID, CCY);
        transactionsService.topUpAccountBalanceOfClient(CLIENT_NAME, CCY, THREADS_COUNT);

        CyclicBarrier barrier = new CyclicBarrier(THREADS_COUNT, () -> assertEquals(0, clientsService.getAccountOfClient(CLIENT_NAME, CCY).getBalance()));

        // When
        for (int i = 0; i < THREADS_COUNT; ++i) {
            executorService.submit(() -> {
                transactionsService.withdrawCashFromAccountOfClient(CLIENT_NAME, CCY, 1);
                try {
                    barrier.await();
                } catch (Exception e) {
                    //
                }
            });
        }

        // Then
        // CyclicBarrier.barrierAction
    }

    @Test
    public void accountBalanceShouldBeCorrectAfterWithdrawalsAndTopUps() {
        // Given
        clientsService.registerNewClient(CLIENT_NAME, PASSPORT_ID, CCY);
        clientsService.registerNewClient(CLIENT_NAME_FROM, PASSPORT_ID_FROM, CCY);
        long expectedAmount = THREADS_COUNT;
        transactionsService.topUpAccountBalanceOfClient(CLIENT_NAME, CCY, expectedAmount);

        CyclicBarrier barrier = new CyclicBarrier(THREADS_COUNT, () -> assertEquals(expectedAmount, clientsService.getAccountOfClient(CLIENT_NAME, CCY).getBalance()));

        // When
        for (int i = 0; i < THREADS_COUNT / 2; ++i) {
            executorService.submit(() -> {
                transactionsService.withdrawCashFromAccountOfClient(CLIENT_NAME, CCY, 1);
                try {
                    barrier.await();
                } catch (Exception e) {
                    //
                }
            });

            executorService.submit(() -> {
                transactionsService.topUpAccountBalanceOfClient(CLIENT_NAME, CCY, 1);
                try {
                    barrier.await();
                } catch (Exception e) {
                    //
                }
            });
        }

        // Then
        // CyclicBarrier.barrierAction
    }

    @Test
    public void accountBalanceShouldBeCorrectAfterWithdrawalsTopUpsAndTransfers() {
        // Given
        clientsService.registerNewClient(CLIENT_NAME, PASSPORT_ID, CCY);
        clientsService.registerNewClient(CLIENT_NAME_FROM, PASSPORT_ID_FROM, CCY);
        long expectedAmount = THREADS_COUNT;
        transactionsService.topUpAccountBalanceOfClient(CLIENT_NAME, CCY, expectedAmount);
        transactionsService.topUpAccountBalanceOfClient(CLIENT_NAME_FROM, CCY, expectedAmount);

        CyclicBarrier barrier = new CyclicBarrier(THREADS_COUNT, () -> {
            assertEquals(2 * expectedAmount, clientsService.getAccountOfClient(CLIENT_NAME, CCY).getBalance());
            assertEquals(0, clientsService.getAccountOfClient(CLIENT_NAME_FROM, CCY).getBalance());
        });

        // When
        for (int i = 0; i < THREADS_COUNT / 3; ++i) {
            executorService.submit(() -> {
                transactionsService.withdrawCashFromAccountOfClient(CLIENT_NAME, CCY, 1);
                try {
                    barrier.await();
                } catch (Exception e) {
                    //
                }
            });

            executorService.submit(() -> {
                transactionsService.transferMoneyFromTo(CLIENT_NAME_FROM, CLIENT_NAME, CCY, 1);
                try {
                    barrier.await();
                } catch (Exception e) {
                    //
                }
            });

            executorService.submit(() -> {
                transactionsService.topUpAccountBalanceOfClient(CLIENT_NAME, CCY, 1);
                try {
                    barrier.await();
                } catch (Exception e) {
                    //
                }
            });
        }

        // Then
        // CyclicBarrier.barrierAction
    }

    @Test
    public void accountBalanceShouldBeCorrectAfterWithdrawalsTopUpsAndMultiTransfers() {
        // Given
        clientsService.registerNewClient(CLIENT_NAME, PASSPORT_ID, CCY);
        clientsService.registerNewClient(CLIENT_NAME_FROM, PASSPORT_ID_FROM, CCY);
        long expectedAmount = THREADS_COUNT;
        transactionsService.topUpAccountBalanceOfClient(CLIENT_NAME, CCY, expectedAmount);
        transactionsService.topUpAccountBalanceOfClient(CLIENT_NAME_FROM, CCY, expectedAmount);

        CyclicBarrier barrier = new CyclicBarrier(THREADS_COUNT, () -> {
            assertEquals(expectedAmount, clientsService.getAccountOfClient(CLIENT_NAME, CCY).getBalance());
            assertEquals(expectedAmount, clientsService.getAccountOfClient(CLIENT_NAME_FROM, CCY).getBalance());
        });

        // When
        for (int i = 0; i < THREADS_COUNT / 4; ++i) {
            executorService.submit(() -> {
                transactionsService.withdrawCashFromAccountOfClient(CLIENT_NAME, CCY, 1);
                try {
                    barrier.await();
                } catch (Exception e) {
                    //
                }
            });

            executorService.submit(() -> {
                transactionsService.transferMoneyFromTo(CLIENT_NAME_FROM, CLIENT_NAME, CCY, 1);
                try {
                    barrier.await();
                } catch (Exception e) {
                    //
                }
            });

            executorService.submit(() -> {
                transactionsService.transferMoneyFromTo(CLIENT_NAME, CLIENT_NAME_FROM, CCY, 1);
                try {
                    barrier.await();
                } catch (Exception e) {
                    //
                }
            });

            executorService.submit(() -> {
                transactionsService.topUpAccountBalanceOfClient(CLIENT_NAME, CCY, 1);
                try {
                    barrier.await();
                } catch (Exception e) {
                    //
                }
            });
        }

        // Then
        // CyclicBarrier.barrierAction
    }
}
