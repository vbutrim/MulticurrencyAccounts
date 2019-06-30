package services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import helpers.Currency;
import storage.data.Account;

@Singleton
public final class TransactionsService {

    private final ClientsService clientsService;

    @Inject
    public TransactionsService(ClientsService clientsService) {
        this.clientsService = clientsService;
    }

    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    public void withdrawCashFromAccountOfClient(String name, Currency ccy, long cash) {
        Account foundAccount = clientsService.getAccountOfClient(name, ccy);
        synchronized (foundAccount) {
            foundAccount.withdraw(cash);
        }
    }

    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    public void topUpAccountBalanceOfClient(String name, Currency ccy, long amount) {
        Account foundAccount = clientsService.getAccountOfClient(name, ccy);
        synchronized (foundAccount) {
            foundAccount.topUp(amount);
        }
    }

    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    public void transferMoneyFromTo(String nameOfFromClient, String nameOfToClient, Currency ccy, long amount) {
        Account foundAccountFrom = clientsService.getAccountOfClient(nameOfFromClient, ccy);
        Account foundAccountTo = clientsService.getAccountOfClient(nameOfToClient, ccy);

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
    }

    private void transferMoneyFromTo(Account accountFrom, Account accountTo, long amount) {
        accountFrom.withdraw(amount);
        accountTo.topUp(amount);
    }
}
