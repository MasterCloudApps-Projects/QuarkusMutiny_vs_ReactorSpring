package es.urjc.resource;

import es.urjc.dto.PlayerBasicInformation;
import es.urjc.repository.PlayerRepository;
import io.quarkus.vertx.web.Route;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.RoutingContext;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class MyDeclarativeRoutes {

    private final PlayerRepository playerRepository;

    public MyDeclarativeRoutes(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Route(path = "/players", methods = Route.HttpMethod.GET, produces = "application/json")
    Uni<List<PlayerBasicInformation>> greetings(RoutingContext rc) {

        return this.playerRepository.listAll()
                .map(playerList -> playerList.stream()
                        .map(player -> new PlayerBasicInformation(player.getId(), player.getName(), player.getGoals()))
                        .collect(Collectors.toList()));




    }
}
