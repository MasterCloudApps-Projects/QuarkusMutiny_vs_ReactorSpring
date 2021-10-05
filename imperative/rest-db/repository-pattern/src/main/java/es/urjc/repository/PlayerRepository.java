package es.urjc.repository;

import es.urjc.entity.Player;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class PlayerRepository implements PanacheRepository<Player> {

    public Optional<Player> findByName(String name) {
        return find("name", name).firstResultOptional();
    }

}
