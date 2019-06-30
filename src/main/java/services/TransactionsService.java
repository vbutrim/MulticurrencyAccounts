package services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import helpers.AccountAction;
import helpers.Currency;
import storage.data.Account;
import storage.data.Transaction;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Singleton
public final class TransactionsService {

    private final ClientsService clientsService;
    private final List<Transaction> transactionsHistory = new CopyOnWriteArrayList<>();

    @Inject
    public TransactionsService(ClientsService clientsService) {
        this.clientsService = clientsService;
    }

    public List<Transaction> getTransactionsHistory() {
        return transactionsHistory;
    }

    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    public void withdrawCashFromAccountOfClient(String name, Currency ccy, long cash) {
        Account foundAccount = clientsService.getAccountOfClient(name, ccy);
        synchronized (foundAccount) {
            foundAccount.withdraw(cash);
        }
        transactionsHistory.add(Transaction.ofSingleOperation(name, ccy, AccountAction.WITHDRAW, cash));
    }

    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    public void topUpAccountBalanceOfClient(String name, Currency ccy, long amount) {
        Account foundAccount = clientsService.getAccountOfClient(name, ccy);
        synchronized (foundAccount) {
            foundAccount.topUp(amount);
        }
        transactionsHistory.add(Transaction.ofSingleOperation(name, ccy, AccountAction.TOP_UP, amount));
    }

    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    public void transferMoneyFromTo(String nameOfFromClient, String nameOfToClient, Currency ccy, long amount) {
        Account foundAccountFrom = clientsService.getAccountOfClient(nameOfFromClient, ccy);
        Account foundAccountTo = clientsService.getAccountOfClient(nameOfToClient, ccy);

        /*
         * To prevent deadlocks order it. Otherwise, if we have situation with mutual transactions
         * (Client1 sends money Client2, Client2 sends money Client1)
         *  --- Thread A ---
         *                  ----> blocks first account
         *                  ----> is waiting second account
         *  --- Thread B ---
         *                  ----> blocks second account
         *                  ----> is waiting first account
         */
        if (foundAccountFrom.getId() < foundAccountTo.getId()) {
            synchronized (foundAccountFrom) {
                synchronized (foundAccountTo) {
                    transferMoneyFromTo(foundAccountFrom, foundAccountTo, amount);
                }
            }
        } else {
            synchronized (foundAccountTo) {
                synchronized (foundAccountFrom) {
                    transferMoneyFromTo(foundAccountFrom, foundAccountTo, amount);
                }
            }
        }
        transactionsHistory.add(Transaction.ofTransferring(nameOfFromClient, nameOfToClient, ccy, AccountAction.TRANSFER, amount));
    }

    private void transferMoneyFromTo(Account accountFrom, Account accountTo, long amount) {
        accountFrom.withdraw(amount);
        accountTo.topUp(amount);
    }
}
