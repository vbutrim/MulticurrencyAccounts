package storage.data;

import helpers.AccountAction;
import helpers.Currency;
import helpers.GlobalIds;
import lombok.ToString;

@ToString
public final class Transaction {

    private static final String NULL_STRING_VALUE = "<null>";

    private final long id;
    private final AccountAction action;
    private final String clientNameFrom;
    private final String clientNameTo;
    private final Currency currencyOfOperation;
    private final long amount;

    private Transaction(String clientNameFrom, String clientNameTo, Currency currencyOfOperation, AccountAction action, long amount) {
        this.id = GlobalIds.TransactionIdentifier.getAndIncrement();
        this.action = action;
        this.clientNameFrom = clientNameFrom;
        this.clientNameTo = clientNameTo;
        this.currencyOfOperation = currencyOfOperation;
        this.amount = amount;
    }

    public static Transaction ofTransferring(String clientNameFrom, String clientNameTo, Currency ccy, AccountAction action, long amount) {
        return new Transaction(clientNameFrom, clientNameTo, ccy, action, amount);
    }

    public static Transaction ofSingleOperation(String clientName, Currency ccy, AccountAction action, long amount) {

        return new Transaction(clientName, NULL_STRING_VALUE, ccy, action, amount);

    }
}
