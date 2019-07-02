package controllers;

import helpers.Currency;
import helpers.exceptions.AccountsMustBeDifferentOnTransferringException;
import helpers.exceptions.IncorrectActionAsArgumentException;
import helpers.exceptions.IncorrectCurrencyAsArgumentException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import storage.exceptions.AccountBalanceLimitationException;
import storage.exceptions.AccountNotEnoughMoneyException;
import storage.exceptions.AccountNotFoundException;
import storage.exceptions.AccountWithSuchCcyAlreadyExistsException;
import storage.exceptions.ClientAlreadyExistsException;
import storage.exceptions.ClientHasNonZeroBalancedAccount;
import storage.exceptions.ClientNotFoundException;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.Response;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ThrowableExceptionMapperTest {

    private Throwable actualException;
    private Response.Status expectedResponseStatus;

    public ThrowableExceptionMapperTest(Throwable actualException, Response.Status expectedResponseStatus) {
        this.actualException = actualException;
        this.expectedResponseStatus = expectedResponseStatus;
    }


    @Parameterized.Parameters(name = "{index}: {0}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new ClientNotFoundException(""), Response.Status.NOT_FOUND},
                {new AccountNotFoundException(null), Response.Status.NOT_FOUND},
                {new AccountBalanceLimitationException(0L, 0L), Response.Status.FORBIDDEN},
                {new AccountNotEnoughMoneyException(0L), Response.Status.FORBIDDEN},
                {new AccountWithSuchCcyAlreadyExistsException(0L, Currency.DEFAULT_VALUE), Response.Status.FORBIDDEN},
                {new AccountsMustBeDifferentOnTransferringException(), Response.Status.FORBIDDEN},
                {new ClientHasNonZeroBalancedAccount(""), Response.Status.FORBIDDEN},
                {new ClientAlreadyExistsException(""), Response.Status.FORBIDDEN},
                {new IncorrectActionAsArgumentException(""), Response.Status.BAD_REQUEST},
                {new IncorrectCurrencyAsArgumentException(""), Response.Status.BAD_REQUEST},
                {new RuntimeException(), Response.Status.INTERNAL_SERVER_ERROR}
        });
    }

    @Test
    public void shouldConvertThrowableToResponseWithCorrectStatus() {
        // Given
        ThrowableExceptionMapper mapper = createInstance();

        // When
        int actualResponseStatusId = mapper.toResponse(actualException).getStatus();

        // Then
        assertEquals(expectedResponseStatus.getStatusCode(), actualResponseStatusId);
    }

    private ThrowableExceptionMapper createInstance() {
        return new ThrowableExceptionMapper();
    }
}
