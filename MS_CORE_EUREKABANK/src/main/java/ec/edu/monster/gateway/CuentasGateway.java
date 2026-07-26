package ec.edu.monster.gateway;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
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
 * Gateway para MS_CUENTAS.
 * Enruta /accounts al microservicio MS_CUENTAS.
 * El endpoint interno PUT /accounts/{id}/balance NO se expone aquí — es solo para MS_TRANSACCIONES.
 */
@Path("/accounts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CuentasGateway {

    private static final String MS_CUENTAS_URL =
            System.getenv().getOrDefault("MS_CUENTAS_URL", "http://localhost:8080/MS_CUENTAS_EUREKABANK/api");

    @GET
    public Response getAll(@Context HttpHeaders headers) {
        return forward("GET", MS_CUENTAS_URL + "/accounts", null, headers);
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id, @Context HttpHeaders headers) {
        return forward("GET", MS_CUENTAS_URL + "/accounts/" + id, null, headers);
    }

    @GET
    @Path("/client/{clientId}")
    public Response getByClient(@PathParam("clientId") Long clientId, @Context HttpHeaders headers) {
        return forward("GET", MS_CUENTAS_URL + "/accounts/client/" + clientId, null, headers);
    }

    @POST
    public Response create(String body, @Context HttpHeaders headers) {
        return forward("POST", MS_CUENTAS_URL + "/accounts", body, headers);
    }

    @PUT
    @Path("/{id}/status")
    public Response updateStatus(@PathParam("id") Long id, String body, @Context HttpHeaders headers) {
        return forward("PUT", MS_CUENTAS_URL + "/accounts/" + id + "/status", body, headers);
    }

    @GET
    @Path("/{id}/balance")
    public Response getBalance(@PathParam("id") Long id, @Context HttpHeaders headers) {
        return forward("GET", MS_CUENTAS_URL + "/accounts/" + id + "/balance", null, headers);
    }

    private Response forward(String method, String url, String body, HttpHeaders headers) {
        Client httpClient = ClientBuilder.newClient();
        try {
            String auth = headers.getHeaderString("Authorization");
            var target = httpClient.target(url).request(MediaType.APPLICATION_JSON);
            if (auth != null) target = target.header("Authorization", auth);

            Response upstream;
            switch (method) {
                case "POST": upstream = target.post(body != null ? Entity.json(body) : Entity.json("")); break;
                case "PUT":  upstream = target.put(body != null ? Entity.json(body) : Entity.json("")); break;
                default:     upstream = target.get(); break;
            }

            String responseBody = upstream.readEntity(String.class);
            return Response.status(upstream.getStatus()).type(MediaType.APPLICATION_JSON).entity(responseBody).build();
        } catch (Exception e) {
            return Response.status(503).type(MediaType.APPLICATION_JSON)
                    .entity("{\"message\":\"MS_CUENTAS unavailable: " + e.getMessage() + "\"}").build();
        } finally {
            httpClient.close();
        }
    }
}
