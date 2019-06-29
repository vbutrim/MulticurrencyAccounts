package controllers;

import com.google.gson.Gson;
import helpers.Currency;
import services.ClientsService;
import storage.data.Client;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/clients")
@Produces(MediaType.APPLICATION_JSON)
public final class ClientsController {

    private static final String CLIENT_ID_ENTRY_POINT = "id";

    private final ClientsService clientsService;
    private final Gson gson = new Gson();

    public ClientsController(ClientsService clientsService) {
        this.clientsService = clientsService;
    }

    /*
     * Get all clients Names and Ids
     */
    @GET
    public Response doGet() {
        String json = gson.toJson(clientsService.getAllClients());
        return Response.ok(json).build();
    }

    /*
     * Get Client information
     */
    @GET
    @Path("/{" + CLIENT_ID_ENTRY_POINT + "}")
    public Response doGet(@PathParam(CLIENT_ID_ENTRY_POINT) Long clientId) {
        if (clientId == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Client foundClient = clientsService.getExistingClientById(clientId);

        String json = gson.toJson(foundClient);
        return Response.ok(json).build();
    }

    /*
     * Register new Client and Account for it
     */
    @POST
    public Response doPost(@QueryParam("name") String name, @QueryParam("passportId") String passportId, @QueryParam("ccyOfInitialAccount") String ccyOfInitialAccount) {
        if (name == null || passportId == null || name.isEmpty() || passportId.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        long id;

        if (ccyOfInitialAccount == null || ccyOfInitialAccount.isEmpty() || !Currency.contains(ccyOfInitialAccount)) {
            id = clientsService.registerNewClient(name, passportId, Currency.DEFAULT_VALUE);
        } else {
            id = clientsService.registerNewClient(name, passportId, Currency.valueOf(ccyOfInitialAccount));
        }

        return Response.ok(gson.toJson(String.format("Client with id '%s' and account were successfully created", id))).build();
    }
}
