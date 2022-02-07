package es.urjc.repository;

import es.urjc.entity.Team;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class TeamRepository implements PanacheRepository<Team> {

    public Optional<Team> findByName(String name) {
        return find("name", name).firstResultOptional();
    }

}
