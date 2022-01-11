package es.urjc.repository;

import es.urjc.entity.Movie;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class MovieRepository implements PanacheRepository<Movie> {

    public Uni<List<Movie>> findByPage(Page page) {
        return findAll().page(page).list();
    }


}
