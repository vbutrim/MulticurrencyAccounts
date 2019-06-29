package storage.data;

import lombok.Getter;
import lombok.Setter;
import storage.helpers.Currency;
import storage.helpers.GlobalIds;
import storage.exceptions.AccountWithSuchCcyAlreadyExistsException;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Client {

    private long id;

    private String name;

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
}
