package storage.data;

import helpers.Currency;
import helpers.GlobalIds;
import lombok.Getter;
import storage.exceptions.AccountWithSuchCcyAlreadyExistsException;

import java.util.HashMap;
import java.util.Map;

public final class Client {

    @Getter
    private long id;

    @Getter
    private String name;

    @Getter
    private String passportId;

    private Map<Currency, Long> accountIdPerCcy = new HashMap<>();

    public Client(String name, String passportId, Currency ccyOfInitialAccount, long idOfInitialAccount) {
        this(name, passportId);
        addAccount(ccyOfInitialAccount, idOfInitialAccount);
    }

    private Client(String name, String passportId) {
        this.id = GlobalIds.ClientIdentifier.getAndIncrement();
        this.name = name;
        this.passportId = passportId;
    }

    public void addAccount(Currency ccy, long accountId) {
        if (accountIdPerCcy.containsKey(ccy)) {
            throw new AccountWithSuchCcyAlreadyExistsException(id, ccy);
        }
        accountIdPerCcy.put(ccy, accountId);
    }

    public Map<Currency, Long> getOpenedAccounts() {
        return accountIdPerCcy;
    }
}
