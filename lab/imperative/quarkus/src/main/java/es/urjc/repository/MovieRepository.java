package es.urjc.repository;

import es.urjc.entity.Movie;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class MovieRepository implements PanacheRepository<Movie> {

    public List<Movie> findByPage(Page page) {
        return findAll().page(page).list();
    }


}
