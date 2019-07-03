package controllers;

import com.google.gson.Gson;
import helpers.Currency;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import org.mockito.Mock;
import services.ClientsService;
import storage.data.Client;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Form;
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

public class ClientsControllerTest extends JerseyTest {

    private static final Gson gson = new Gson();
    private static final String CLIENT_NAME = "A. Bandera";
    private static final String PASSPORT_ID = "10aug1960";
    private static final Currency CURRENCY = Currency.RUB;

    @Mock
    private ClientsService clientsService;

    @Override
    protected Application configure() {
        clientsService = mock(ClientsService.class);

        return new ResourceConfig()
                .register(new ClientsController(clientsService));
    }

    @Test
    public void shouldReturnAllClientsOnGet() {
        // Given
        List<Client> expectedClients = Collections.emptyList();
        when(clientsService.getAllClients()).thenReturn(expectedClients);

        // When
        Response response = target("/clients").request().get();

        // Then
        verify(clientsService, times(1)).getAllClients();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(gson.toJson(expectedClients), response.readEntity(String.class));
    }

    @Test
    public void shouldReturnCorrectClientOnGetClientById() {
        // Given
        long clientId = 1L;
        Client expectedClient = mock(Client.class);
        when(clientsService.getClientById(clientId)).thenReturn(expectedClient);

        // When
        Response response = target("/clients/" + clientId).request().get();

        // Then
        verify(clientsService, times(1)).getClientById(any(Long.class));
        verify(clientsService, times(1)).getClientById(clientId);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(gson.toJson(expectedClient), response.readEntity(String.class));
    }

    @Test
    public void shouldCreateClientAndDefaultCurrencyAccountOnPost() {
        // Given
        Long expectedClientId = 1L;
        when(clientsService.registerNewClient(CLIENT_NAME, PASSPORT_ID, Currency.DEFAULT_VALUE)).thenReturn(expectedClientId);

        // When
        Response response = target("/clients")
                .queryParam("name", CLIENT_NAME)
                .queryParam("passportId", PASSPORT_ID)
                .request()
                .post(null);

        // Then
        verify(clientsService, times(1)).registerNewClient(any(String.class), any(String.class), any(Currency.class));
        verify(clientsService, times(1)).registerNewClient(CLIENT_NAME, PASSPORT_ID, Currency.DEFAULT_VALUE);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        String responseMessage = response.readEntity(String.class);
        assertTrue(responseMessage.contains(CLIENT_NAME));
        assertTrue(responseMessage.contains(expectedClientId.toString()));
        assertTrue(responseMessage.contains(Currency.DEFAULT_VALUE.toString()));
        assertTrue(responseMessage.contains("were successfully created"));
    }

    @Test
    public void shouldCreateClientAndCurrencyAccountOnPost() {
        // Given
        Long expectedClientId = 1L;
        when(clientsService.registerNewClient(CLIENT_NAME, PASSPORT_ID, CURRENCY)).thenReturn(expectedClientId);

        // When
        Response response = target("/clients")
                .queryParam("name", CLIENT_NAME)
                .queryParam("passportId", PASSPORT_ID)
                .queryParam("ccyOfInitialAccount", CURRENCY)
                .request()
                .post(null);

        // Then
        verify(clientsService, times(1)).registerNewClient(any(String.class), any(String.class), any(Currency.class));
        verify(clientsService, times(1)).registerNewClient(CLIENT_NAME, PASSPORT_ID, CURRENCY);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        String responseMessage = response.readEntity(String.class);
        assertTrue(responseMessage.contains(CLIENT_NAME));
        assertTrue(responseMessage.contains(expectedClientId.toString()));
        assertTrue(responseMessage.contains(CURRENCY.toString()));
        assertTrue(responseMessage.contains("were successfully created"));
    }

    @Test
    public void shouldCallCloseClientAndAccountsOnDelete() {
        // Given
        Long clientId = 1L;
        doNothing().when(clientsService).closeClientAndAccountsWithNoMoney(clientId);

        // When
        Response response = target("/clients/" + clientId)
                .request()
                .delete();

        // Then
        verify(clientsService, times(1)).closeClientAndAccountsWithNoMoney(any(Long.class));
        verify(clientsService, times(1)).closeClientAndAccountsWithNoMoney(clientId);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        String responseMessage = response.readEntity(String.class);
        assertTrue(responseMessage.contains(clientId.toString()));
        assertTrue(responseMessage.contains("been successfully closed"));
    }
}
