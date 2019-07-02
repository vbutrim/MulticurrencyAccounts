package controllers;

import com.google.gson.Gson;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import org.mockito.Mock;
import services.ClientsService;
import storage.data.Client;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ClientsControllerTest extends JerseyTest {

    private static final Gson gson = new Gson();

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

    // TODO: provide tests
}
