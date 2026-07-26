package ec.edu.monster.gateway;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
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
 * Gateway para MS_CLIENTES.
 * Enruta /auth y /clients al microservicio MS_CLIENTES.
 * El CORE reenvía el header Authorization intacto.
 */
@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClientesGateway {

    private static final String MS_CLIENTES_URL =
            System.getenv().getOrDefault("MS_CLIENTES_URL", "http://localhost:8080/MS_CLIENTES_EUREKABANK/api");

    // ── AUTH ──────────────────────────────────────────────────────────────────

    @POST
    @Path("/auth/login")
    public Response login(String body, @Context HttpHeaders headers) {
        return forward("POST", MS_CLIENTES_URL + "/auth/login", body, headers);
    }

    // ── CLIENTS ───────────────────────────────────────────────────────────────

    @GET
    @Path("/clients")
    public Response getAllClients(@Context HttpHeaders headers) {
        return forward("GET", MS_CLIENTES_URL + "/clients", null, headers);
    }

    @GET
    @Path("/clients/{id}")
    public Response getClientById(@PathParam("id") Long id, @Context HttpHeaders headers) {
        return forward("GET", MS_CLIENTES_URL + "/clients/" + id, null, headers);
    }

    @GET
    @Path("/clients/dni/{dni}")
    public Response getClientByDni(@PathParam("dni") String dni, @Context HttpHeaders headers) {
        return forward("GET", MS_CLIENTES_URL + "/clients/dni/" + dni, null, headers);
    }

    @POST
    @Path("/clients")
    public Response createClient(String body, @Context HttpHeaders headers) {
        return forward("POST", MS_CLIENTES_URL + "/clients", body, headers);
    }

    @PUT
    @Path("/clients/{id}")
    public Response updateClient(@PathParam("id") Long id, String body, @Context HttpHeaders headers) {
        return forward("PUT", MS_CLIENTES_URL + "/clients/" + id, body, headers);
    }

    @DELETE
    @Path("/clients/{id}")
    public Response deleteClient(@PathParam("id") Long id, @Context HttpHeaders headers) {
        return forward("DELETE", MS_CLIENTES_URL + "/clients/" + id, null, headers);
    }

    // ── PARAMETERS ────────────────────────────────────────────────────────────

    @GET
    @Path("/parameters")
    public Response getAllParameters(@Context HttpHeaders headers) {
        return forward("GET", MS_CLIENTES_URL + "/parameters", null, headers);
    }

    @GET
    @Path("/parameters/{key}")
    public Response getParameterByKey(@PathParam("key") String key, @Context HttpHeaders headers) {
        return forward("GET", MS_CLIENTES_URL + "/parameters/" + key, null, headers);
    }

    @POST
    @Path("/parameters")
    public Response createParameter(String body, @Context HttpHeaders headers) {
        return forward("POST", MS_CLIENTES_URL + "/parameters", body, headers);
    }

    @PUT
    @Path("/parameters/{id}")
    public Response updateParameter(@PathParam("id") Long id, String body, @Context HttpHeaders headers) {
        return forward("PUT", MS_CLIENTES_URL + "/parameters/" + id, body, headers);
    }

    // ── Forward helper ────────────────────────────────────────────────────────

    private Response forward(String method, String url, String body, HttpHeaders headers) {
        Client httpClient = ClientBuilder.newClient();
        try {
            String auth = headers.getHeaderString("Authorization");
            var target = httpClient.target(url).request(MediaType.APPLICATION_JSON);
            if (auth != null) target = target.header("Authorization", auth);

            Response upstream;
            switch (method) {
                case "POST":   upstream = target.post(body != null ? Entity.json(body) : Entity.json("")); break;
                case "PUT":    upstream = target.put(body != null ? Entity.json(body) : Entity.json("")); break;
                case "DELETE": upstream = target.delete(); break;
                default:       upstream = target.get(); break;
            }

            String responseBody = upstream.readEntity(String.class);
            return Response.status(upstream.getStatus())
                    .type(MediaType.APPLICATION_JSON)
                    .entity(responseBody)
                    .build();
        } catch (Exception e) {
            return Response.status(503)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"message\":\"MS_CLIENTES unavailable: " + e.getMessage() + "\"}")
                    .build();
        } finally {
            httpClient.close();
        }
    }
}
