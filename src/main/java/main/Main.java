package main;

import com.google.inject.Guice;
import com.google.inject.Injector;
import injector.InjectingModule;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import services.ClientsService;
import servlets.ClientsServlet;

public class Main {

    private static final String CLIENTS_API_ENTRY_POINT = "/api/v1/clients";

    public static void main(String[] args) throws Exception {

        Injector injector = Guice.createInjector(new InjectingModule());

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new ClientsServlet(injector.getInstance(ClientsService.class))), CLIENTS_API_ENTRY_POINT);

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{context});

        Server server = new Server(8080);
        server.setHandler(handlers);

        server.start();
        System.out.println("Server started");
        server.join();
    }
}
