package es.urjc.repository;

import es.urjc.entity.Stadium;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StadiumRepository implements PanacheRepository<Stadium> {

    public Uni<Stadium> findByName(String name) {
        return find("name", name).firstResult();
    }

}
