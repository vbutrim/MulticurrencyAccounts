package servlets;

import com.google.gson.Gson;
import com.google.inject.Inject;
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
        Gson gson = new Gson();
        String json = gson.toJson("empty");
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(json);
        response.setStatus(HttpServletResponse.SC_OK);

    }

    /*
     * Register new Client and Account for it
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
