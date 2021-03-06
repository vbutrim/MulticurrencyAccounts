package main;

import com.google.inject.Guice;
import com.google.inject.Injector;
import controllers.AccountsController;
import controllers.ClientsController;
import controllers.ThrowableExceptionMapper;
import controllers.TransactionsController;
import helpers.InjectingModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import services.ClientsService;
import services.TransactionsService;

import java.io.IOException;
import java.net.URI;

public class Main {

    private static final String BASE_SERVER = "http://localhost:";
    private static final String BASE_PORT = "8080";
    private static final String API_URL_V1 = "/api/v1";

    private static final Injector injector = Guice.createInjector(new InjectingModule());
    private static final Logger logger = LogManager.getLogger("Main");

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            logger.error("Incorrect arguments");
        }

        String portToStart = args.length == 1 ? args[0] : BASE_PORT;

        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(
                URI.create(BASE_SERVER + portToStart + API_URL_V1),
                getResourceConfig());

        logger.info("Server started: {}{}", BASE_SERVER, portToStart);

        try {
            logger.info("Press any key to stop the service...");

            //noinspection ResultOfMethodCallIgnored
            System.in.read();
        } finally {
            server.shutdownNow();
        }
    }

    private static ResourceConfig getResourceConfig() {
        return new ResourceConfig()
                .register(new ClientsController(injector.getInstance(ClientsService.class)))
                .register(new AccountsController(injector.getInstance(ClientsService.class)))
                .register(new TransactionsController(injector.getInstance(TransactionsService.class)))

                .register(new ThrowableExceptionMapper())
                .property(ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR, "true");
    }
}
