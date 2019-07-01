package helpers;

import helpers.exceptions.IncorrectActionAsArgumentException;

public enum AccountAction {
    WITHDRAW("WITHDRAW"),
    TOP_UP("TOP_UP"),
    TRANSFER("TRANSFER"); // Only Technical usage for Transactions History

    private final String text;

    AccountAction(String text) {
        this.text = text;
    }

    public static boolean containsAndNotEqualsToTransfer(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }

        try {
            AccountAction foundAction = valueOf(text);
            return foundAction != AccountAction.TRANSFER;
        } catch (IllegalArgumentException e) {
            throw new IncorrectActionAsArgumentException(text);
        }
    }

    @Override
    public String toString() {
        return this.text;
    }
}
