package controllers;

import com.google.gson.Gson;
import database.DBException;
import database.datasets.ClientsDataSet;
import services.ClientsService;
import storage.data.Client;
import storage.helpers.Currency;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Path("/clients")
@Produces(MediaType.APPLICATION_JSON)
public class ClientsController {
    private static final String CLIENT_IT_ENTRY_POINT = "id";

    private final ClientsService clientsService;

    public ClientsController(ClientsService clientsService) {
        this.clientsService = clientsService;
    }

    /*
     * Get all clients Names and Ids
     */
    @GET
    public Response doGet(){
        List<Client> clientObj = null;
        try {
            clientObj = clientsService.getAllClients();
        } catch (DBException e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }

        if (clientObj == null || clientObj.isEmpty()) {
            return Response
                    .status(Response.Status.NO_CONTENT)
                    .entity("No clients")
                    .build();
        }

        Gson gson = new Gson();
        String json = gson.toJson(clientObj);
        return Response.ok(json).build();
    }

    /*
     * Get Client information
     */
    @GET
    @Path("/{" + CLIENT_IT_ENTRY_POINT + "}")
    public Response doGet(@PathParam(CLIENT_IT_ENTRY_POINT) Long clientId){
        if (clientId == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Client clientObj = clientsService.getExistingClientById(clientId);

        if (clientObj == null) {
            return Response
                    .status(Response.Status.NO_CONTENT)
                    .entity(String.format("Client with id %s not found", clientId))
                    .build();
        }

        Gson gson = new Gson();
        String json = gson.toJson(clientObj);
        return Response.ok(json).build();
    }

    /*
     * Register new Client and Account for it
     */
    @POST
    public Response doPost(@QueryParam("name") String name, @QueryParam("passportId") String passportId) {
/*        String name = request.getName();
        String passportId = request.getPassportId();*/

        if (name == null || passportId == null || name.isEmpty() || passportId.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Long id = null;

        id = clientsService.registerNewClient(name, passportId, Currency.EUR);

        if (id == null) {
            return Response
                    .status(Response.Status.CONFLICT)
                    .entity("Client with the same Name already exists")
                    .build();
        }
        /*Gson gson = new Gson();
        String json = gson.toJson(profile);*/

        return Response
                .ok(String.format("Client with id %s was created", id)).build();

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
