package es.urjc.repository;

import es.urjc.entity.Movie;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class MovieRepository implements PanacheMongoRepository<Movie> {

    public List<Movie> findByPage(Integer pageNumber, Integer pageSize) {
        return findAll(Sort.descending("imdb.rating")).page(pageNumber, pageSize).list();
    }

}
