package es.urjc.resource;

import es.urjc.dto.*;
import es.urjc.entity.Car;
import es.urjc.entity.Player;
import es.urjc.repository.CarRepository;
import es.urjc.repository.PlayerRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static javax.ws.rs.core.Response.*;
import static javax.ws.rs.core.Response.Status.*;

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
    public List<PlayerBasicInformation> getAllPlayer() {
        return this.playerRepository.findAll().stream()
                .map(player -> new PlayerBasicInformation(player.getId(), player.getName(), player.getGoals()))
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{name}")
    public Response getPlayer(@PathParam("name") String name) {
        return this.playerRepository.findByName(name)
                .map(player -> new PlayerBasicInformation(player.getId(), player.getName(), player.getGoals()))
                .map(Response::ok)
                .orElse(Response.status(NOT_FOUND).entity(new ErrorInformation("Not found player")))
                .build();
    }

    @POST
    @Path("/")
    @Transactional
    public Response savePlayer(Player newPlayer) {

        if (this.playerRepository.findByName(newPlayer.getName()).isEmpty()) {
            this.playerRepository.save(newPlayer);
            return getPlayer(newPlayer.getName());
        } else {
            return status(BAD_REQUEST).entity(new ErrorInformation("Already exists player")).build();
        }
    }

    @PUT
    @Path("/update-method/{playerId}")
    @Transactional
    public Response updatePlayerByUpdateMethod(@PathParam("playerId") long playerId, Player updatePlayer) {
        this.playerRepository.update(updatePlayer.getName(), updatePlayer.getGoals(), playerId);
        return getPlayer(updatePlayer.getName());
    }

    @PUT
    @Path("/save-method/{playerId}")
    @Transactional
    public Response updatePlayerByPersistMethod(@PathParam("playerId") long playerId, Player updatePlayer) {
        return this.playerRepository.findById(playerId)
                .map(player -> {
                    player.setName(updatePlayer.getName());
                    player.setGoals(updatePlayer.getGoals());
                    return player;
                })
                .map(updatedPlayer -> {
                    this.playerRepository.save(updatedPlayer);
                    return getPlayer(updatedPlayer.getName());
                })
                .orElse(Response.status(NOT_FOUND).entity(new ErrorInformation("Not found player")).build());
    }

    @DELETE
    @Path("/{playerId}")
    @Transactional
    public Response deletePlayer(@PathParam("playerId") long playerId) {

        Player player = getPlayer(playerId);

        if (player.getTeams().isEmpty()) {
            this.playerRepository.deleteById(playerId);
            return noContent().build();
        } else {
            return status(BAD_REQUEST).entity(new ErrorInformation("Player is associated to some team yet")).build();
        }

    }

    @GET
    @Path("/details")
    public List<PlayerFullInformation> getAllPlayerDetails() {
        return this.playerRepository.findAll().stream()
                .map(player -> {
                    List<TeamBasicInformation> teams = getTeamsBasicInformation(player);
                    List<CarBasicInformation> cars = getCarsBasicInformation(player);
                    return new PlayerFullInformation(player.getId(), player.getName(), player.getGoals(), teams, cars);
                })
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{name}/details")
    public Response getPlayerDetails(@PathParam("name") String name) {

        return this.playerRepository.findByName(name)
                .map(player -> {
                    List<TeamBasicInformation> teams = getTeamsBasicInformation(player);
                    List<CarBasicInformation> cars = getCarsBasicInformation(player);
                    return new PlayerFullInformation(player.getId(), player.getName(), player.getGoals(), teams, cars);
                })
                .map(Response::ok)
                .orElse(Response.status(NOT_FOUND).entity(new ErrorInformation("Not found player")))
                .build();
    }

    @POST
    @Path("/{playerId}/cars/{carId}")
    @Transactional
    public Response addCarToPlayer(@PathParam("playerId") long playerId,
                                   @PathParam("carId") long carId) {

        Player player = getPlayer(playerId);
        Car car = getCar(carId);

        if (isNull(car.getPlayer())) {
            car.setPlayer(player);
            this.carRepository.save(car);
            return getPlayerDetails(player.getName());
        } else {
            return status(BAD_REQUEST)
                    .entity(new ErrorInformation("Car is already associated to another player"))
                    .build();
        }

    }

    @DELETE
    @Path("/{playerId}/cars/{carId}")
    @Transactional
    public Response deleteCarToPlayer(@PathParam("playerId") long playerId,
                                      @PathParam("carId") long carId) {

        Player player = getPlayer(playerId);
        Car car = getCar(carId);

        if (isNull(car.getPlayer())) {
            return status(BAD_REQUEST)
                    .entity(new ErrorInformation("Car isn't associated to player"))
                    .build();
        } else {
            car.setPlayer(null);
            this.carRepository.save(car);
            return getPlayerDetails(player.getName());
        }

    }

    private Player getPlayer(long playerId) {
        return this.playerRepository.findById(playerId)
                .orElseThrow(() -> {
                    Response response = status(NOT_FOUND).entity(new ErrorInformation("Not found player")).build();
                    return new NotFoundException(response);
                });
    }

    private Car getCar(long carId) {
        return this.carRepository.findById(carId)
                .orElseThrow(() -> {
                    Response response = status(NOT_FOUND).entity(new ErrorInformation("Not found car")).build();
                    return new NotFoundException(response);
                });
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

}