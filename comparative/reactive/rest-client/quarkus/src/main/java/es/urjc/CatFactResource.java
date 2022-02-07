package es.urjc;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/cat-fact")
@RequestScoped
public class CatFactResource {

    @RestClient
    CatFactResourceClient catFactResourceClient;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<CatFact> getCatFact() {
        return this.catFactResourceClient.getFact();
    }
}
