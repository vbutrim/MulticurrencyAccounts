package helpers;

import helpers.exceptions.IncorrectCurrencyAsArgumentException;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class CurrencyTest {

    @Test
    public void containsShouldReturnTrueIfValueIsInEnum() {
        // Given

        // When
        boolean expectedContains = Currency.contains("RUB");

        // Then
        assertTrue(expectedContains);
    }

    @Test(expected = IncorrectCurrencyAsArgumentException.class)
    public void containsShouldThrowIncorrectCurrencyAsArgumentExceptionIfNotContains() {
        // Given

        // When
        Currency.contains("Any_value_not_Currency");

        // Then
    }
}
