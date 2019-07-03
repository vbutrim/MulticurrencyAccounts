package controllers;

import com.google.gson.Gson;
import controllers.dtos.AccountRequestDto;
import helpers.Currency;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import org.mockito.Mock;
import services.ClientsService;
import storage.data.Account;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AccountsControllerTest extends JerseyTest {

    private static final Gson gson = new Gson();
    private static final String CLIENT_NAME = "George Clooney";
    private static final Currency CURRENCY = Currency.RUB;

    private static final String ACCOUNTS_API_ENTRY_POINT = "/accounts";

    @Mock
    private ClientsService clientsService;

    @Override
    protected Application configure() {
        clientsService = mock(ClientsService.class);

        return new ResourceConfig()
                .register(new AccountsController(clientsService));
    }

    @Test
    public void shouldCreateNewAccountOnPost() {
        // Given
        Long expectedAccountId = 1L;
        when(clientsService.createAccountForClient(CLIENT_NAME, CURRENCY)).thenReturn(expectedAccountId);
        AccountRequestDto accountRequest = new AccountRequestDto(CLIENT_NAME, CURRENCY.toString());

        // When
        Response response = target(ACCOUNTS_API_ENTRY_POINT)
                .request()
                .post(Entity.entity(accountRequest, MediaType.APPLICATION_JSON));

        // Then
        verify(clientsService, times(1)).createAccountForClient(any(String.class), any(Currency.class));
        verify(clientsService, times(1)).createAccountForClient(CLIENT_NAME, CURRENCY);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        String responseMessage = response.readEntity(String.class);
        assertTrue(responseMessage.contains(CLIENT_NAME));
        assertTrue(responseMessage.contains(expectedAccountId.toString()));
        assertTrue(responseMessage.contains(CURRENCY.toString()));
        assertTrue(responseMessage.contains("was successfully created"));
    }

    @Test
    public void shouldReturnAccountOnGet() {
        // Given
        Account expectedAccount = mock(Account.class);
        when(clientsService.getAccountOfClient(CLIENT_NAME, CURRENCY)).thenReturn(expectedAccount);

        // When
        Response response = target(ACCOUNTS_API_ENTRY_POINT)
                .queryParam("clientName", CLIENT_NAME)
                .queryParam("currency", CURRENCY)
                .request()
                .get();

        // Then
        verify(clientsService, times(1)).getAccountOfClient(any(String.class), any(Currency.class));
        verify(clientsService, times(1)).getAccountOfClient(CLIENT_NAME, CURRENCY);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(gson.toJson(expectedAccount), response.readEntity(String.class));
    }

    @Test
    public void shouldReturnBadRequestOnIncorrectGet() {
        // Given

        // When
        Response response = target(ACCOUNTS_API_ENTRY_POINT)
                .queryParam("clientName", CLIENT_NAME)
                .request()
                .get();

        // Then
        verify(clientsService, times(0)).getAccountOfClient(any(String.class), any(Currency.class));
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void shouldReturnBadRequestOnIncorrectPost() {
        // Given
        AccountRequestDto accountRequest = new AccountRequestDto(CLIENT_NAME);

        // When
        Response response = target(ACCOUNTS_API_ENTRY_POINT)
                .request()
                .post(Entity.entity(accountRequest, MediaType.APPLICATION_JSON));

        // Then
        verify(clientsService, times(0)).createAccountForClient(any(String.class), any(Currency.class));
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }
}
