package es.urjc.repository;

import es.urjc.entity.Stadium;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class StadiumRepository implements PanacheRepository<Stadium> {

    public Optional<Stadium> findByName(String name) {
        return find("name", name).firstResultOptional();
    }

}
