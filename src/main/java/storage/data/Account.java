package storage.data;

import helpers.GlobalIds;
import lombok.Getter;
import lombok.ToString;
import storage.exceptions.NotEnoughMoneyException;
import helpers.Currency;

@Getter
@ToString
public final class Account {

    private long id;

    private Currency ccy;

    private long balance;

    public Account(Currency ccy) {
        this.id = GlobalIds.AccountIdentifier.getAndIncrement();
        this.ccy = ccy;
        this.balance = 0;
    }

    public void topUp(long cash) {
        this.balance += cash;
    }

    public void withdraw(long amount) {
        if (balance - amount < 0) {
            throw new NotEnoughMoneyException(this.id);
        }

        this.balance -= amount;
    }
}
