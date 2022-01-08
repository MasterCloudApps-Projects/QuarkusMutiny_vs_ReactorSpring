package com.example;

import com.example.model.Movie;
import io.quarkus.vertx.web.Route;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Random;

@ApplicationScoped
public class ExampleResource {

    private Random random = new Random();

    @Route(methods = Route.HttpMethod.GET, path = "/movies", produces = MediaType.APPLICATION_JSON)
    public Uni<List<Movie>> hello() {

        return Uni.createFrom()
                .item(() -> random.nextInt(1000))
                .flatMap(pageIndex -> Movie.findAll().page(pageIndex, 20).list());
    }
}