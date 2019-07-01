package storage;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import helpers.Currency;
import storage.data.Account;
import storage.data.Client;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Singleton
public class SafeBankStorageProxy implements BankStorage {

    private final BankStorage nonSafeBankStorage;
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();

    @Inject
    public SafeBankStorageProxy(BankStorageImpl nonSafeBankStorage) {
        this.nonSafeBankStorage = nonSafeBankStorage;
    }

    @Override
    public long registerNewClient(String name, String passportId, Currency ccyOfInitialAccount) {
        rwLock.writeLock().lock();
        try {
            return nonSafeBankStorage.registerNewClient(name, passportId, ccyOfInitialAccount);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public void closeClientAndAccountsWithZeroBalance(String name) {
        rwLock.writeLock().lock();
        try {
            nonSafeBankStorage.closeClientAndAccountsWithZeroBalance(name);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public List<Client> getAllClients() {
        rwLock.readLock().lock();
        try {
            return nonSafeBankStorage.getAllClients();
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public Client getClientById(Long id) {
        rwLock.readLock().lock();
        try {
            return nonSafeBankStorage.getClientById(id);
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public long createAccountForClient(String name, Currency ccy) {
        rwLock.writeLock().lock();
        try {
            return nonSafeBankStorage.createAccountForClient(name, ccy);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public Account getAccountOfClient(String name, Currency ccy) {
        rwLock.readLock().lock();
        try {
            return nonSafeBankStorage.getAccountOfClient(name, ccy);
        } finally {
            rwLock.readLock().unlock();
        }
    }
}
