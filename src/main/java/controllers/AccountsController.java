package controllers;

import com.google.gson.Gson;
import helpers.Currency;
import services.ClientsService;
import storage.data.Account;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
public final class AccountsController {

    private static final Gson gson = new Gson();

    private final ClientsService clientsService;

    public AccountsController(ClientsService clientsService) {
        this.clientsService = clientsService;
    }

    /*
     * Get Account by Client Name and Currency
     */
    @GET
    public Response doGet(@QueryParam("clientName") String clientName,
                          @QueryParam("currency") String ccy) {
        if (clientName == null || ccy == null || clientName.isEmpty() || ccy.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Account foundClient = clientsService.getAccountOfClient(clientName, Currency.valueOf(ccy));

        String json = gson.toJson(foundClient);
        return Response.ok(json).build();
    }

    /*
     * Create new Account for Client with Currency
     */
    @POST
    public Response doPost(@QueryParam("clientName") String clientName,
                           @QueryParam("currency") String ccy) {
        if (clientName == null || ccy == null || clientName.isEmpty() || ccy.isEmpty() || !Currency.contains(ccy)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        long createdAccountId = clientsService.createAccountForClient(clientName, Currency.valueOf(ccy));

        String json = gson.toJson(String.format("'%s' account with id '%s' for Client '%s' was successfully created", ccy, createdAccountId, clientName));
        return Response.ok(json).build();
    }
}
