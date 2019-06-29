package storage.data;

import lombok.Getter;
import lombok.ToString;
import storage.exceptions.NotEnoughMoneyException;
import helpers.Currency;

@Getter
@ToString
public class Account {

    private long id;

    /*
     * ManyToOne, OneToMany Hibernate's analogue
     */
    // private final long clientOwnerId;

    private Currency ccy;

    private long balance;

    public Account(Currency ccy) {
        this.ccy = ccy;
        this.balance = 0;
    }

    /*    public Account(long clientOwnerId, Currency ccy) {
        this.id = GlobalIds.AccountIdentifier.getAndIncrement();
        this.clientOwnerId = clientOwnerId;
        this.ccy = ccy;
        this.balance = 0;
    }*/

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
