/*
package es.urjc.resource;

import es.urjc.dto.*;
import es.urjc.entity.Car;
import es.urjc.entity.Player;
import es.urjc.repository.CarRepository;
import es.urjc.repository.PlayerRepository;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.*;

@Path("/players")
public class PlayerResource {

    private final PlayerRepository playerRepository;
    private final CarRepository carRepository;

    @Inject
    public PlayerResource(PlayerRepository playerRepository, CarRepository carRepository) {
        this.playerRepository = playerRepository;
        this.carRepository = carRepository;
    }

    @GET
    @Path("/")
    public Multi<PlayerBasicInformation> getAllPlayer() {

        return this.playerRepository.listAll()
                .map(playerList -> playerList.stream()
                        .map(player -> new PlayerBasicInformation(player.getId(), player.getName(), player.getGoals())))
                .onItem().transformToMulti(playerStream -> Multi.createFrom().items(playerStream));

    }

    @GET
    @Path("/{name}")
    public Uni<Response> getPlayer(@PathParam("name") String name) {
        return this.playerRepository.findByName(name)
                .onItem().ifNotNull().transform(player -> new PlayerBasicInformation(player.getId(), player.getName(), player.getGoals()))
                .onItem().ifNotNull().transform(playerBasicInformation -> ok(playerBasicInformation).build())
                .onItem().ifNull().failWith(this::failWithNotFoundPlayerException);

    }

    @POST
    @Path("/")
    @ReactiveTransactional
    public Uni<Response> savePlayer(Player newPlayer) {

        return this.playerRepository.findByName(newPlayer.getName())
                .onItem().ifNotNull().failWith(this::failWithBadRequestExistingPlayerException)
                .onItem().ifNull().continueWith(newPlayer)
                .flatMap(this.playerRepository::persist)
                .map(Player::getName)
                .flatMap(this::getPlayer);
    }

    @PUT
    @Path("/update-method/{playerId}")
    @ReactiveTransactional
    public Uni<Response> updatePlayerByUpdateMethod(@PathParam("playerId") long playerId, Player updatePlayer) {

        return this.playerRepository.update("name = ?1, goals = ?2 where id = ?3", updatePlayer.getName(), updatePlayer.getGoals(), playerId)
                .chain(numberOfPlayerUpdated -> {
                    if (numberOfPlayerUpdated == 1) {
                        return getPlayer(updatePlayer.getName());
                    }
                    throw failWithNotFoundPlayerException();
                });
    }

    @PUT
    @Path("/persist-method/{playerId}")
    @ReactiveTransactional
    public Uni<Response> updatePlayerByPersistMethod(@PathParam("playerId") long playerId, Player updatePlayer) {

        return this.playerRepository.findById(playerId)
                .map(Player.class::cast)
                .onItem().ifNotNull().transform(player -> {
                    player.setName(updatePlayer.getName());
                    player.setGoals(updatePlayer.getGoals());
                    return player;
                })
                .onItem().ifNull().failWith(this::failWithNotFoundPlayerException)
                .chain(this.playerRepository::persist)
                .chain(updatedPlayer -> {
                    if (this.playerRepository.isPersistent(updatedPlayer)) {
                        return getPlayer(updatedPlayer.getName());
                    }
                    throw new InternalServerErrorException();
                });
    }

    @DELETE
    @Path("/{playerId}")
    @ReactiveTransactional
    public Uni<Response> deletePlayer(@PathParam("playerId") long playerId) {

        return getPlayer(playerId)
                .invoke(player -> {
                    Mutiny.fetch(player.getTeams())
                            .map(teams -> teams.isEmpty() ? player : null)
                            .onItem().ifNull().failWith(this::failWithPlayerHasAlreadyTeamException);
                })
                .flatMap(player -> this.playerRepository.deleteById(playerId))
                .flatMap(isDeleted -> isDeleted
                        ? Uni.createFrom().item(noContent().build())
                        : Uni.createFrom().nullItem())
                .onItem().ifNull().failWith(this::failWithNotFoundPlayerException);
    }

    @GET
    @Path("/details")
    public Multi<PlayerFullInformation> getAllPlayerDetails() {

        return this.playerRepository.listAll()
                .map(Collection::stream)
                .onItem().transformToMulti(players -> Multi.createFrom().items(players))
                .call(player -> Mutiny.fetch(player.getTeams()))
                .call(player -> Mutiny.fetch(player.getCars()))
                .map(player -> {
                    List<TeamBasicInformation> teams = getTeamsBasicInformation(player);
                    List<CarBasicInformation> cars = getCarsBasicInformation(player);
                    return new PlayerFullInformation(player.getId(), player.getName(), player.getGoals(), teams, cars);
                });
    }

    @GET
    @Path("/{name}/details")
    public Uni<Response> getPlayerDetails(@PathParam("name") String name) {

        return this.playerRepository.findByName(name)
                .onItem().ifNull().failWith(this::failWithNotFoundPlayerException)
                .call(player -> Mutiny.fetch(player.getTeams()))
                .call(player -> Mutiny.fetch(player.getCars()))
                .map(player -> {
                    List<TeamBasicInformation> teams = getTeamsBasicInformation(player);
                    List<CarBasicInformation> cars = getCarsBasicInformation(player);
                    return new PlayerFullInformation(player.getId(), player.getName(), player.getGoals(), teams, cars);
                })
                .map(playerFullInformation -> ok(playerFullInformation).build());
    }

    @POST
    @Path("/{playerId}/cars/{carId}")
    @ReactiveTransactional
    public Uni<Response> addCarToPlayer(@PathParam("playerId") long playerId,
                                        @PathParam("carId") long carId) {

        return getCar(carId)
                .flatMap(car -> {
                    if (isNull(car.getPlayer())) {
                        return Uni.createFrom().item(car);
                    }
                    throw failWithCarIsAssociatedToAnotherPlayerException();
                })
                .flatMap(car -> getPlayer(playerId)
                        .flatMap(player -> {
                            car.setPlayer(player);
                            return this.carRepository.persistAndFlush(car).replaceWith(player);
                        })
                        .chain(player -> getPlayerDetails(player.getName())));

    }

    @DELETE
    @Path("/{playerId}/cars/{carId}")
    @ReactiveTransactional
    public Uni<Response> deleteCarToPlayer(@PathParam("playerId") long playerId,
                                           @PathParam("carId") long carId) {

        return getCar(carId)
                .flatMap(car -> {
                    if (isNull(car.getPlayer())) {
                        throw failWithCarIsNotAssociatedToAnotherPlayerException();
                    }
                    return Uni.createFrom().item(car);
                })
                .flatMap(car -> getPlayer(playerId)
                        .flatMap(player -> {
                            car.setPlayer(null);
                            return this.carRepository.persistAndFlush(car).replaceWith(player);
                        })
                        .chain(player -> getPlayerDetails(player.getName())));

    }

    private Uni<Player> getPlayer(long playerId) {
        return this.playerRepository.findById(playerId)
                .onItem().ifNull().failWith(this::failWithNotFoundPlayerException);
    }

    private Uni<Car> getCar(long carId) {
        return this.carRepository.findById(carId)
                .onItem().ifNull().failWith(this::failWithNotFoundCarException);
    }

    private List<TeamBasicInformation> getTeamsBasicInformation(Player player) {
        return player.getTeams().stream()
                .map(team -> new TeamBasicInformation(team.getId(), team.getName(), team.getRanking()))
                .collect(Collectors.toList());
    }

    private List<CarBasicInformation> getCarsBasicInformation(Player player) {
        return player.getCars().stream()
                .map(car -> new CarBasicInformation(
                        car.getId(), car.getLicencePlate(),
                        car.getBrand(), car.getModel(), car.getPrice()))
                .collect(Collectors.toList());
    }

    private NotFoundException failWithNotFoundPlayerException() {
        Response notFoundCarResponse = status(NOT_FOUND).entity(new ErrorInformation("Not found player")).build();
        return new NotFoundException(notFoundCarResponse);
    }

    private BadRequestException failWithBadRequestExistingPlayerException() {
        ErrorInformation errorInformation = new ErrorInformation("Already exists player");
        Response badRequestResponse = status(BAD_REQUEST).entity(errorInformation).build();
        return new BadRequestException(badRequestResponse);
    }

    private BadRequestException failWithPlayerHasAlreadyTeamException() {
        ErrorInformation errorInformation = new ErrorInformation("Car is associated to player yet");
        Response badRequestResponse = status(BAD_REQUEST).entity(errorInformation).build();
        throw new BadRequestException(badRequestResponse);
    }

    private BadRequestException failWithCarIsNotAssociatedToAnotherPlayerException() {
        ErrorInformation errorInformation = new ErrorInformation("Car isn't associated to player");
        Response badRequestResponse = status(BAD_REQUEST).entity(errorInformation).build();
        throw new BadRequestException(badRequestResponse);
    }

    private BadRequestException failWithCarIsAssociatedToAnotherPlayerException() {
        ErrorInformation errorInformation = new ErrorInformation("Car is already associated to another player");
        Response badRequestResponse = status(BAD_REQUEST).entity(errorInformation).build();
        throw new BadRequestException(badRequestResponse);
    }

    private NotFoundException failWithNotFoundCarException() {
        Response notFoundCarResponse = status(NOT_FOUND).entity(new ErrorInformation("Not found car")).build();
        return new NotFoundException(notFoundCarResponse);
    }
}
*/
