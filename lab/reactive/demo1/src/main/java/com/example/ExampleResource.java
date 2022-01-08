package com.example;

import com.example.model.Movie;
import io.quarkus.panache.common.Sort;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Random;

@Path("/movies")
public class ExampleResource {

    private Random random = new Random();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Movie> hello() {
        int i = random.nextInt(1000);
        return Movie.findAll().page(i, 20).list();
    }
}