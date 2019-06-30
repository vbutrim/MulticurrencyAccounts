package controllers;

import com.google.gson.Gson;
import helpers.Currency;
import services.ClientsAccountsService;
import storage.data.Account;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
public final class AccountsController {

    private final static String ACCOUNT_ID_ENTRY_POINT = "id";

    private final ClientsAccountsService clientsAccountsService;
    private final Gson gson = new Gson();

    public AccountsController(ClientsAccountsService clientsAccountsService) {
        this.clientsAccountsService = clientsAccountsService;
    }

    /*
     * Get Account By Client name and Currency
     */
    @GET
    public Response doGet(@QueryParam("clientName") String clientName, @QueryParam("currency") String ccy) {
        if (clientName == null || ccy == null || clientName.isEmpty() || ccy.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Account foundClient = clientsAccountsService.getAccountOfClient(clientName, Currency.valueOf(ccy));

        String json = gson.toJson(foundClient);
        return Response.ok(json).build();
    }
}
