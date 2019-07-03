package controllers;

import com.google.gson.Gson;
import controllers.dtos.ExtendedAccountRequestDto;
import controllers.dtos.TransferRequestDto;
import helpers.AccountAction;
import helpers.Currency;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import org.mockito.Mock;
import services.TransactionsService;
import storage.data.Transaction;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TransactionsControllerTest extends JerseyTest {

    private static final Gson gson = new Gson();
    private static final String CLIENT_NAME = "Keanu Reeves";
    private static final String CLIENT_NAME_TO = "Laurence Fishburne";
    private static final Currency CURRENCY = Currency.USD;
    private static final Long AMOUNT = 10L;

    private static final String TRANSFER_API_ENTRY_POINT = "/transfer";

    @Mock
    private TransactionsService transactionsService;

    @Override
    protected Application configure() {
        transactionsService = mock(TransactionsService.class);

        return new ResourceConfig()
                .register(new TransactionsController(transactionsService));
    }

    @Test
    public void shouldReturnAllTransactionsOnGet() {
        // Given
        List<Transaction> expectedHistory = Collections.emptyList();
        when(transactionsService.getTransactionsHistory()).thenReturn(expectedHistory);

        // When
        Response response = target(TRANSFER_API_ENTRY_POINT)
                .request()
                .get();

        // Then
        //noinspection ResultOfMethodCallIgnored
        verify(transactionsService, times(1)).getTransactionsHistory();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(gson.toJson(expectedHistory), response.readEntity(String.class));
    }

    @Test
    public void shouldTransferMoneyBetweenAccountsOnPost() {
        // Given
        doNothing().when(transactionsService).transferMoneyFromTo(CLIENT_NAME, CLIENT_NAME_TO, CURRENCY, AMOUNT);
        TransferRequestDto transferRequestDto = new TransferRequestDto(CLIENT_NAME, CLIENT_NAME_TO, CURRENCY.toString(), AMOUNT);

        // When
        Response response = target(TRANSFER_API_ENTRY_POINT)
                .request()
                .post(Entity.entity(transferRequestDto, MediaType.APPLICATION_JSON));

        // Then
        verify(transactionsService, times(1)).transferMoneyFromTo(any(String.class), any(String.class), any(Currency.class), any(Long.class));
        verify(transactionsService, times(1)).transferMoneyFromTo(CLIENT_NAME, CLIENT_NAME_TO, CURRENCY, AMOUNT);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("SUCCESSFULLY TRANSFERRED", response.readEntity(String.class));
    }

    @Test
    public void shouldWithdrawCashOnPut() {
        // Given
        ExtendedAccountRequestDto extAccountRequestDto = new ExtendedAccountRequestDto(
                CLIENT_NAME,
                CURRENCY.toString(),
                AccountAction.WITHDRAW.toString(),
                AMOUNT);

        // When
        Response response = target(TRANSFER_API_ENTRY_POINT)
                .request()
                .put(Entity.entity(extAccountRequestDto, MediaType.APPLICATION_JSON));

        // Then
        verify(transactionsService, times(1)).withdrawCashFromAccountOfClient(any(String.class), any(Currency.class), any(Long.class));
        verify(transactionsService, times(0)).topUpAccountBalanceOfClient(any(String.class), any(Currency.class), any(Long.class));
        verify(transactionsService, times(1)).withdrawCashFromAccountOfClient(CLIENT_NAME, CURRENCY, AMOUNT);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        String responseMessage = response.readEntity(String.class);
        assertTrue(responseMessage.contains(CLIENT_NAME));
        assertTrue(responseMessage.contains(CURRENCY.toString()));
        assertTrue(responseMessage.contains(AMOUNT.toString()));
        assertTrue(responseMessage.contains("withdrew"));
    }

    @Test
    public void shouldTopUpAmountOnPut() {
        // Given
        ExtendedAccountRequestDto extAccountRequestDto = new ExtendedAccountRequestDto(
                CLIENT_NAME,
                CURRENCY.toString(),
                AccountAction.TOP_UP.toString(),
                AMOUNT);

        // When
        Response response = target(TRANSFER_API_ENTRY_POINT)
                .request()
                .put(Entity.entity(extAccountRequestDto, MediaType.APPLICATION_JSON));

        // Then
        verify(transactionsService, times(0)).withdrawCashFromAccountOfClient(any(String.class), any(Currency.class), any(Long.class));
        verify(transactionsService, times(1)).topUpAccountBalanceOfClient(any(String.class), any(Currency.class), any(Long.class));
        verify(transactionsService, times(1)).topUpAccountBalanceOfClient(CLIENT_NAME, CURRENCY, AMOUNT);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        String responseMessage = response.readEntity(String.class);
        assertTrue(responseMessage.contains(CLIENT_NAME));
        assertTrue(responseMessage.contains(CURRENCY.toString()));
        assertTrue(responseMessage.contains(AMOUNT.toString()));
        assertTrue(responseMessage.contains("top up"));
    }

    @Test
    public void shouldReturnBadRequestOnIncorrectPost() {
        // Given
        TransferRequestDto transferRequestDto = new TransferRequestDto(CLIENT_NAME, CURRENCY.toString(), AMOUNT);

        // When
        Response response = target(TRANSFER_API_ENTRY_POINT)
                .request()
                .post(Entity.entity(transferRequestDto, MediaType.APPLICATION_JSON));

        // Then
        verify(transactionsService, times(0)).transferMoneyFromTo(any(String.class), any(String.class), any(Currency.class), any(Long.class));
        verify(transactionsService, times(0)).transferMoneyFromTo(CLIENT_NAME, CLIENT_NAME_TO, CURRENCY, AMOUNT);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void shouldReturnBadRequestOnIncorrectPut() {
        // Given
        ExtendedAccountRequestDto extAccountRequestDto = new ExtendedAccountRequestDto(CLIENT_NAME, CURRENCY.toString(), AMOUNT);

        // When
        Response response = target(TRANSFER_API_ENTRY_POINT)
                .request()
                .put(Entity.entity(extAccountRequestDto, MediaType.APPLICATION_JSON));

        // Then
        verify(transactionsService, times(0)).withdrawCashFromAccountOfClient(any(String.class), any(Currency.class), any(Long.class));
        verify(transactionsService, times(0)).topUpAccountBalanceOfClient(any(String.class), any(Currency.class), any(Long.class));
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }
}
