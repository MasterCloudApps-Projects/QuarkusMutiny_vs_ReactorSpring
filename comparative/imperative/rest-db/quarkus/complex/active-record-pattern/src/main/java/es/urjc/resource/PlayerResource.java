package es.urjc.resource;

import es.urjc.dto.*;
import es.urjc.entity.Car;
import es.urjc.entity.Player;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static javax.ws.rs.core.Response.*;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Path("/players")
public class PlayerResource {

    @GET
    @Path("")
    public List<PlayerBasicInformation> getAllPlayer() {
        return Player.listAll().stream()
                .map(Player.class::cast)
                .map(player -> new PlayerBasicInformation(player.getId(), player.getName(), player.getGoals()))
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{name}")
    public Response getPlayer(@PathParam("name") String name) {
        return Player.findByName(name)
                .map(player -> new PlayerBasicInformation(player.getId(), player.getName(), player.getGoals()))
                .map(Response::ok)
                .orElse(Response.status(NOT_FOUND).entity(new ErrorInformation("Not found player")))
                .build();
    }

    @POST
    @Path("")
    @Transactional
    public Response savePlayer(Player newPlayer) {

        if (Player.findByName(newPlayer.getName()).isEmpty()) {
            newPlayer.persist();
            return getPlayer(newPlayer.getName());
        } else {
            return status(BAD_REQUEST).entity(new ErrorInformation("Already exists player")).build();
        }

    }

    @PUT
    @Path("/update-method/{playerId}")
    @Transactional
    public Response updatePlayerByUpdateMethod(@PathParam("playerId") long playerId, Player updatePlayer) {
        Player.update("name = ?1, goals = ?2 where id = ?3", updatePlayer.getName(), updatePlayer.getGoals(), playerId);
        return getPlayer(updatePlayer.getName());
    }

    @PUT
    @Path("/persist-method/{playerId}")
    @Transactional
    public Response updatePlayerByPersistMethod(@PathParam("playerId") long playerId, Player updatePlayer) {
        return Player.findByIdOptional(playerId)
                .map(Player.class::cast)
                .map(player -> {
                    player.setName(updatePlayer.getName());
                    player.setGoals(updatePlayer.getGoals());
                    return player;
                })
                .map(updatedPlayer -> {
                    updatedPlayer.persist();
                    return updatedPlayer.isPersistent()
                            ? getPlayer(updatedPlayer.getName())
                            : serverError().build();
                })
                .orElse(Response.status(NOT_FOUND).entity(new ErrorInformation("Not found player")).build());

    }

    @DELETE
    @Path("/{playerId}")
    @Transactional
    public Response deletePlayer(@PathParam("playerId") long playerId) {

        Player player = getPlayer(playerId);

        if (player.getTeams().isEmpty()) {
            boolean isDeleted = Player.deleteById(playerId);
            ResponseBuilder responseBuilder = isDeleted
                    ? noContent()
                    : status(NOT_FOUND).entity(new ErrorInformation("Not found player"));
            return responseBuilder.build();
        } else {
            return status(BAD_REQUEST).entity(new ErrorInformation("Player is associated to some team yet")).build();
        }

    }

    @GET
    @Path("/details")
    public List<PlayerFullInformation> getAllPlayerDetails() {
        return Player.listAll().stream()
                .map(Player.class::cast)
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

        return Player.findByName(name)
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
            car.persist();
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
            car.persist();
            return getPlayerDetails(player.getName());
        }

    }

    private Player getPlayer(long playerId) {
        return Player.findByIdOptional(playerId)
                .map(Player.class::cast)
                .orElseThrow(() -> {
                    Response response = status(NOT_FOUND).entity(new ErrorInformation("Not found player")).build();
                    return new NotFoundException(response);
                });
    }

    private Car getCar(long carId) {
        return Car.findByIdOptional(carId)
                .map(Car.class::cast)
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