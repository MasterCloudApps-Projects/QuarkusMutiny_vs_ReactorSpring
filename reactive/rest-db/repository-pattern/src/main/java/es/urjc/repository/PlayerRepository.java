package es.urjc.repository;

import es.urjc.entity.Player;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PlayerRepository implements PanacheRepository<Player> {

    public Uni<Player> findByName(String name) {
        return find("name", name).firstResult();
    }

}
