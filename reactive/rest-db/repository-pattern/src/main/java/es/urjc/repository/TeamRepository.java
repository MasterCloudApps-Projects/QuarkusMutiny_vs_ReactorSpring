package es.urjc.repository;

import es.urjc.entity.Team;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TeamRepository implements PanacheRepository<Team> {

    public Uni<Team> findByName(String name) {
        return find("name", name).firstResult();
    }

}
