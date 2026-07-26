package ec.edu.monster.gateway;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;

/**
 * Gateway para MS_TRANSACCIONES.
 * Enruta /transactions al microservicio MS_TRANSACCIONES.
 */
@Path("/transactions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TransaccionesGateway {

    private static final String MS_TRANSACCIONES_URL =
            System.getenv().getOrDefault("MS_TRANSACCIONES_URL", "http://localhost:8080/MS_TRANSACCIONES_EUREKABANK/api");

    @POST
    @Path("/deposit")
    public Response deposit(String body, @Context HttpHeaders headers) {
        return forward("POST", MS_TRANSACCIONES_URL + "/transactions/deposit", body, headers);
    }

    @POST
    @Path("/withdraw")
    public Response withdraw(String body, @Context HttpHeaders headers) {
        return forward("POST", MS_TRANSACCIONES_URL + "/transactions/withdraw", body, headers);
    }

    @POST
    @Path("/transfer")
    public Response transfer(String body, @Context HttpHeaders headers) {
        return forward("POST", MS_TRANSACCIONES_URL + "/transactions/transfer", body, headers);
    }

    @GET
    @Path("/account/{accountId}")
    public Response getByAccount(@PathParam("accountId") Long accountId, @Context HttpHeaders headers) {
        return forward("GET", MS_TRANSACCIONES_URL + "/transactions/account/" + accountId, null, headers);
    }

    private Response forward(String method, String url, String body, HttpHeaders headers) {
        Client httpClient = ClientBuilder.newClient();
        try {
            String auth = headers.getHeaderString("Authorization");
            var target = httpClient.target(url).request(MediaType.APPLICATION_JSON);
            if (auth != null) target = target.header("Authorization", auth);

            Response upstream = "GET".equals(method)
                    ? target.get()
                    : target.post(body != null ? Entity.json(body) : Entity.json(""));

            String responseBody = upstream.readEntity(String.class);
            return Response.status(upstream.getStatus()).type(MediaType.APPLICATION_JSON).entity(responseBody).build();
        } catch (Exception e) {
            return Response.status(503).type(MediaType.APPLICATION_JSON)
                    .entity("{\"message\":\"MS_TRANSACCIONES unavailable: " + e.getMessage() + "\"}").build();
        } finally {
            httpClient.close();
        }
    }
}
