package controllers;

import helpers.AccountAction;
import helpers.Currency;
import services.TransactionsService;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/transfer")
@Produces(MediaType.APPLICATION_JSON)
public final class TransactionsController {

    private final TransactionsService transactionsService;

    public TransactionsController(TransactionsService transactionsService) {
        this.transactionsService = transactionsService;
    }

    /*
     * Print all Transactions history
     */
    @GET
    public Response doGet() {
        return Response.ok().build();
    }

    /*
     * Withdraw or Top Up Account's Balance (ClientName & Currency)
     * + Parameters: Action (Transfer
     */
    @PUT
    public Response doPut(@QueryParam("clientNameFrom") String clientNameFrom,
                          @QueryParam("currency") String currency,
                          @QueryParam("action") String action,
                          @QueryParam("amount") Long amountMoney) {
        if (clientNameFrom == null || clientNameFrom.isEmpty() ||
                currency == null || currency.isEmpty() || Currency.contains(currency) ||
                action == null || action.isEmpty() || !AccountAction.containsAndNotEqualsToTransfer(action)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.ok().build();
    }

    /*
     * Transfer money from Client (nameFrom) to Client (nameTo)
     * + Parameters: Currency (String), Amount of Money (Long)
     */
    @POST
    public Response doPost(@QueryParam("clientNameFrom") String clientNameFrom,
                           @QueryParam("clientNameTo") String clientNameTo,
                           @QueryParam("currency") String currency,
                           @QueryParam("amount") Long amountMoney) {
        return Response.ok().build();
    }
}
