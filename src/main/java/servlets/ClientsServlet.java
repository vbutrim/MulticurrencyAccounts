package servlets;

import com.google.gson.Gson;
import database.DBException;
import database.datasets.ClientsDataSet;
import services.ClientsService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ClientsServlet extends HttpServlet {
    private final ClientsService clientsService;

    public ClientsServlet(ClientsService clientsService) {
        this.clientsService = clientsService;
    }

    /*
     * Get accounts per Client
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sessionId = request.getSession().getId();

        String name = request.getParameter("name");

        response.setContentType("text/html;charset=utf-8");

        if (name == null || name.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        ClientsDataSet clientObj = clientsService.getExistingClientId(name);

        if (clientObj == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(String.format("Client with name '%s' not found", name));
            return;
        }

        Gson gson = new Gson();
        String json = gson.toJson(clientObj);
        response.getWriter().println(json);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    /*
     * Register new Client and Account for it
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String sessionId = request.getSession().getId();

        String name = request.getParameter("name");
        String passportId = request.getParameter("passportId");

        response.setContentType("text/html;charset=utf-8");

        if (name == null || passportId == null || name.isEmpty() || passportId.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Long id = null;

        try {
            id = clientsService.registerNewClient(name, passportId);
        } catch (DBException e) {
            response.getWriter().println("Couldn't perform Query. Try later");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        if (id == null) {
            response.getWriter().println("Client with the same Name already exists");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        /*Gson gson = new Gson();
        String json = gson.toJson(profile);*/
        response.getWriter().println(String.format("Client with id %s was created", id));
        response.setStatus(HttpServletResponse.SC_OK);
    }

    /*
     * Add account or whatever
     */
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    /*
     * Delete Client from the server and its accounts
     */
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
