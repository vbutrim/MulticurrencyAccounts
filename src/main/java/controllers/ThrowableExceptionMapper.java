package controllers;

import com.google.gson.Gson;
import helpers.exceptions.AccountsMustBeDifferentOnTransferringException;
import helpers.exceptions.IncorrectActionAsArgumentException;
import helpers.exceptions.IncorrectCurrencyAsArgumentException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import storage.exceptions.AccountBalanceLimitationException;
import storage.exceptions.AccountNotEnoughMoneyException;
import storage.exceptions.AccountNotFoundException;
import storage.exceptions.AccountWithSuchCcyAlreadyExistsException;
import storage.exceptions.ClientAlreadyExistsException;
import storage.exceptions.ClientHasNonZeroBalancedAccount;
import storage.exceptions.ClientNotFoundException;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ThrowableExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Gson gson = new Gson();
    private static final Logger logger = LogManager.getLogger("ThrowableExceptionMapper");

    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response toResponse(Throwable exception) {
        Response.Status statusForEndUser;

        if (exception instanceof ClientNotFoundException
                || exception instanceof AccountNotFoundException) {
            statusForEndUser = Response.Status.NOT_FOUND;
        } else if (exception instanceof AccountBalanceLimitationException
                || exception instanceof AccountNotEnoughMoneyException
                || exception instanceof AccountWithSuchCcyAlreadyExistsException
                || exception instanceof AccountsMustBeDifferentOnTransferringException
                || exception instanceof ClientHasNonZeroBalancedAccount
                || exception instanceof ClientAlreadyExistsException) {
            statusForEndUser = Response.Status.FORBIDDEN;
        } else if (exception instanceof IncorrectActionAsArgumentException
                || exception instanceof IncorrectCurrencyAsArgumentException) {
            statusForEndUser = Response.Status.BAD_REQUEST;
        } else {
            statusForEndUser = Response.Status.INTERNAL_SERVER_ERROR;
            logger.error("Internal server error", exception);
        }

        return Response
                .status(statusForEndUser)
                .entity(gson.toJson("Request failed. " + exception.getMessage()))
                .build();
    }
}
