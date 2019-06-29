package storage.data;

import helpers.GlobalIds;
import lombok.Getter;
import lombok.ToString;
import storage.exceptions.AccountBalanceLimitationException;
import storage.exceptions.AccountNotEnoughMoneyException;
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
        if (Long.MAX_VALUE - cash < balance) {
            throw new AccountBalanceLimitationException(id, balance);
        }
        this.balance += cash;
    }

    public void withdraw(long amount) {
        if (balance - amount < 0) {
            throw new AccountNotEnoughMoneyException(this.id);
        }

        this.balance -= amount;
    }
}
