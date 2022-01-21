package es.urjc.rest.client;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/cat-fact")
@RequestScoped
public class CatFactResource {

    @Inject
    @RestClient
    CatFactResourceClient catFactResourceClient;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public CatFact getCatFact() {
        return this.catFactResourceClient.getFact();
    }
}
