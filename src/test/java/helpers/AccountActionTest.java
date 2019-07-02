package helpers;

import helpers.exceptions.IncorrectActionAsArgumentException;
import org.junit.Assert;
import org.junit.Test;

public class AccountActionTest {

    @Test
    public void containsShouldReturnTrueIfValueIsInEnum() {
        // Given

        // When
        boolean containsAndNotEqualsToTransfer = AccountAction.containsAndNotEqualsToTransfer("WITHDRAW");

        // Then
        Assert.assertTrue(containsAndNotEqualsToTransfer);
    }

    @Test(expected = IncorrectActionAsArgumentException.class)
    public void containsShouldThrowIncorrectActionAsArgumentExceptionIfNotContains() {
        // Given

        // When
        AccountAction.containsAndNotEqualsToTransfer("Any_value_not_Action");

        // Then
    }

    @Test
    public void containsShouldReturnFalseIfActionTransfer() {
        // Given

        // When
        boolean containsAndNotEqualsToTransfer = AccountAction.containsAndNotEqualsToTransfer("TRANSFER");

        // Then
        Assert.assertFalse(containsAndNotEqualsToTransfer);
    }
}
