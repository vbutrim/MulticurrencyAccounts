package main;

import com.google.inject.Guice;
import com.google.inject.Injector;
import controllers.ClientsController;
import injector.InjectingModule;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import services.ClientsService;

import java.io.IOException;
import java.net.URI;

public class Main {
    private static final String CLIENTS_API_ENTRY_POINT = "/api/v1/clients";
    private static final String ACCOUNTS_API_ENTRY_POINT = "/api/v1/accounts";

/*    public static void main(String[] args) throws Exception {

        Injector injector = Guice.createInjector(new InjectingModule());

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new ClientsController(injector.getInstance(ClientsService.class))), CLIENTS_API_ENTRY_POINT);

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{context});

        Server server = new Server(8080);
        server.setHandler(handlers);

        server.start();
        System.out.println("Server started");
        server.join();
    }*/
    private static final String BASE_SERVER = "http://localhost:";
    private static final String BASE_PORT = "8080";
    private static final String BASE_API_URL = "/api/v1";

    private static final Injector injector = Guice.createInjector(new InjectingModule());

    public static void main(String[] args) throws IOException {
        // TODO: port
        //Injector injector = Guice.createInjector(new InjectingModule());

        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_SERVER + BASE_PORT + BASE_API_URL), getResourceConfig());
        System.out.println("Server started");

        try {
            System.out.println("Press any key to stop the service...");

            //noinspection ResultOfMethodCallIgnored
            System.in.read();
        } finally {
            server.shutdownNow();
        }
    }

    private static ResourceConfig getResourceConfig() {
        final ResourceConfig rc = new ResourceConfig().register(new ClientsController(injector.getInstance(ClientsService.class)));
        rc.property(ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR, "true");
        return rc;
    }
}
