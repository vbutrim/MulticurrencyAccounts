package controllers;

import com.google.gson.Gson;
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
    private final Gson gson = new Gson();

    public TransactionsController(TransactionsService transactionsService) {
        this.transactionsService = transactionsService;
    }

    /*
     * Print all Transactions history
     */
    @GET
    public Response doGet() {
        String json = gson.toJson(transactionsService.getTransactionsHistory());
        return Response.ok(json).build();
    }

    /*
     * Withdraw or Top Up Account's Balance (ClientName & Currency)
     * + Parameters: Action (Transfer
     */
    @PUT
    public Response doPut(@QueryParam("clientName") String clientName,
                          @QueryParam("currency") String currency,
                          @QueryParam("action") String action,
                          @QueryParam("amount") Long amountMoney) {
        if (clientName == null || clientName.isEmpty() ||
                currency == null || currency.isEmpty() || !Currency.contains(currency) ||
                action == null || action.isEmpty() || !AccountAction.containsAndNotEqualsToTransfer(action) ||
                amountMoney == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        AccountAction withdrawOrTopUp = AccountAction.valueOf(action);
        StringBuilder endActionDescription = new StringBuilder(String.format("'%s's account: Successfully ", clientName));

        switch (withdrawOrTopUp) {
            case WITHDRAW: {
                transactionsService.withdrawCashFromAccountOfClient(clientName, Currency.valueOf(currency), amountMoney);
                endActionDescription.append("withdrew");
                break;
            }
            case TOP_UP: {
                transactionsService.topUpAccountBalanceOfClient(clientName, Currency.valueOf(currency), amountMoney);
                endActionDescription.append("top up");
            }
        }

        endActionDescription.append(String.format("%s %ss", amountMoney, currency));
        return Response.ok(gson.toJson(endActionDescription.toString())).build();
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
