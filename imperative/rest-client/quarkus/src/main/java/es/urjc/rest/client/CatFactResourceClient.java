package es.urjc.rest.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/fact")
@RegisterRestClient
public interface CatFactResourceClient {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    CatFact getFact();
}
