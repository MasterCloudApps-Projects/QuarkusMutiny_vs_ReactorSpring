package es.urjc.resource;

import es.urjc.dto.ErrorInformation;
import es.urjc.dto.PlayerBasicInformation;
import es.urjc.dto.StadiumBasicInformation;
import es.urjc.dto.TeamFullInformation;
import es.urjc.entity.Player;
import es.urjc.entity.Team;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.*;

@Path("/teams")
public class TeamResource {

    @GET
    @Path("")
    public Multi<TeamFullInformation> getAllTeams() {

        return Team.listAll()
                .map(teamList -> teamList.stream().map(panacheEntity -> (Team) panacheEntity))
                .onItem().transformToMulti(teamFullInformationStream -> Multi.createFrom().items(teamFullInformationStream))
                .map(team ->
                        getPlayersBasicInformation(team).map(playerBasicInformation ->
                                        TeamFullInformation.builder()
                                                .id(team.getId())
                                                .name(team.getName())
                                                .ranking(team.getRanking())
                                                .players(playerBasicInformation))
                                .flatMap(teamFullInformationBuilder ->
                                        getStadiumBasicInformation(team).map(teamFullInformationBuilder::stadium))
                                .map(TeamFullInformation.TeamFullInformationBuilder::build))
                .flatMap(Uni::toMulti);
    }

    @GET
    @Path("/{name}")
    public Uni<Response> getTeam(@PathParam("name") String name) {

        return Team.findByName(name)
                .onItem().ifNull().failWith(this::failWithNotFoundTeamException)
                .onItem().ifNotNull().transformToUni(team ->
                        getPlayersBasicInformation(team)
                                .map(playerBasicInformation -> TeamFullInformation.builder()
                                        .id(team.getId())
                                        .name(team.getName())
                                        .ranking(team.getRanking())
                                        .players(playerBasicInformation))
                                .flatMap(teamFullInformationBuilder ->
                                        getStadiumBasicInformation(team).map(teamFullInformationBuilder::stadium))
                                .map(TeamFullInformation.TeamFullInformationBuilder::build))
                .map(teamFullInformation -> ok(teamFullInformation).build());
    }

    @POST
    @Path("")
    @ReactiveTransactional
    public Uni<Response> saveTeam(Team newTeam) {

        return Team.findByName(newTeam.getName())
                .onItem().ifNotNull().failWith(this::failWithBadRequestExistingTeamException)
                .onItem().ifNull().continueWith(newTeam)
                .flatMap(team -> team.persist())
                .onItem().castTo(Team.class)
                .map(Team::getName)
                .chain(this::getTeam);
    }

    @POST
    @Path("/{teamId}/players/{playerId}")
    @ReactiveTransactional
    public Uni<Response> addPlayerToTeam(@PathParam("teamId") long teamId,
                                         @PathParam("playerId") long playerId) {

        return getTeam(teamId)
                .call(team -> Mutiny.fetch(team.getPlayers()))
                .call(team -> getPlayer(playerId)
                        .call(player -> Mutiny.fetch(player.getTeams()))
                        .chain(player -> {
                            team.addPlayer(player);
                            player.addTeam(team);
                            return team.persist();
                        }))
                .flatMap(team -> getPlayersBasicInformation(team)
                        .map(playerBasicInformation -> new TeamFullInformation(
                                team.getId(), team.getName(),
                                team.getRanking(), playerBasicInformation)))
                .map(Response::ok)
                .map(ResponseBuilder::build);
    }

    @DELETE
    @Path("/{teamId}/players/{playerId}")
    @ReactiveTransactional
    public Uni<Response> deletePlayerToTeam(@PathParam("teamId") long teamId,
                                            @PathParam("playerId") long playerId) {

        return getTeam(teamId)
                .call(team -> Mutiny.fetch(team.getPlayers()))
                .call(team -> getPlayer(playerId)
                        .call(player -> Mutiny.fetch(player.getTeams()))
                        .chain(player -> {
                            team.removePlayer(player);
                            player.removeTeam(team);
                            return team.persist();
                        }))
                .flatMap(team -> getPlayersBasicInformation(team)
                        .map(playerBasicInformation -> new TeamFullInformation(
                                team.getId(), team.getName(),
                                team.getRanking(), playerBasicInformation)))
                .map(Response::ok)
                .map(ResponseBuilder::build);
    }


    @PUT
    @Path("/update-method/{teamId}")
    @ReactiveTransactional
    public Uni<Response> updateTeamByUpdateMethod(@PathParam("teamId") long teamId, Team updateTeam) {

        return Team.update("name = ?1, ranking = ?2 where id = ?3", updateTeam.getName(), updateTeam.getRanking(), teamId)
                .chain(numberOfTeamUpdated -> {
                    if (numberOfTeamUpdated == 1) {
                        return getTeam(updateTeam.getName());
                    }
                    throw failWithNotFoundTeamException();
                });
    }


    @PUT
    @Path("/persist-method/{teamId}")
    @ReactiveTransactional
    public Uni<Response> updateTeamByPersistMethod(@PathParam("teamId") long teamId, Team updateTeam) {

        return Team.findById(teamId)
                .map(Team.class::cast)
                .onItem().ifNotNull().transform(team -> {
                    team.setName(updateTeam.getName());
                    team.setRanking(updateTeam.getRanking());
                    return team;
                })
                .onItem().ifNull().failWith(this::failWithNotFoundTeamException)
                .chain(updatedTeam -> updatedTeam.persist())
                .onItem().castTo(Team.class)
                .chain(updatedTeam -> {
                    if (updatedTeam.isPersistent()) {
                        return getTeam(updatedTeam.getName());
                    }
                    throw new InternalServerErrorException();
                });
    }


    @DELETE
    @Path("/{teamId}")
    @ReactiveTransactional
    public Uni<Response> deleteTeam(@PathParam("teamId") long teamId) {

        return getTeam(teamId)
                .call(team -> Mutiny.fetch(team.getPlayers())
                        .invoke(players -> {
                            if (!players.isEmpty()) {
                                throw failWithTeamHasAssociatedToPlayersYetException();
                            }
                        }))
                .call(team -> Mutiny.fetch(team.getStadium())
                        .onItem().ifNotNull().failWith(this::failWithTeamHasAssociatedToStadiumYetException))
                .flatMap(team -> Team.deleteById(teamId))
                .flatMap(isDeleted -> isDeleted
                        ? Uni.createFrom().item(noContent().build())
                        : Uni.createFrom().nullItem())
                .onItem().ifNull().failWith(this::failWithNotFoundTeamException);

    }

    private Uni<Player> getPlayer(long playerId) {
        return Player.findById(playerId)
                .onItem().ifNotNull().transform(panacheEntity -> (Player) panacheEntity)
                .onItem().ifNull().failWith(this::failWithNotFoundPlayerException);
    }

    private Uni<Team> getTeam(long teamId) {
        return Team.findById(teamId)
                .onItem().ifNotNull().transform(panacheEntity -> (Team) panacheEntity)
                .onItem().ifNull().failWith(this::failWithNotFoundTeamException);
    }

    private Uni<List<PlayerBasicInformation>> getPlayersBasicInformation(Team team) {

        return Mutiny.fetch(team.getPlayers())
                .map(players -> players.stream()
                        .map(player -> new PlayerBasicInformation(player.getId(), player.getName(), player.getGoals()))
                        .collect(Collectors.toList()));
    }

    private Uni<StadiumBasicInformation> getStadiumBasicInformation(Team team) {

        return Mutiny.fetch(team.getStadium())
                .onItem().ifNotNull()
                .transform(stadium -> new StadiumBasicInformation(stadium.getId(), stadium.getName(), stadium.getCapacity()));
    }

    private NotFoundException failWithNotFoundTeamException() {
        ErrorInformation errorInformation = new ErrorInformation("Not found team");
        Response notFoundRequestResponse = status(NOT_FOUND).entity(errorInformation).build();
        return new NotFoundException(notFoundRequestResponse);
    }

    private BadRequestException failWithBadRequestExistingTeamException() {
        ErrorInformation errorInformation = new ErrorInformation("Already exists team");
        Response badRequestResponse = status(BAD_REQUEST).entity(errorInformation).build();
        return new BadRequestException(badRequestResponse);
    }

    private NotFoundException failWithNotFoundPlayerException() {
        Response notFoundCarResponse = status(NOT_FOUND).entity(new ErrorInformation("Not found player")).build();
        return new NotFoundException(notFoundCarResponse);
    }

    private BadRequestException failWithTeamHasAssociatedToPlayersYetException() {
        ErrorInformation errorInformation = new ErrorInformation("Team has associated to players yet");
        Response badRequestResponse = status(BAD_REQUEST).entity(errorInformation).build();
        throw new BadRequestException(badRequestResponse);
    }

    private BadRequestException failWithTeamHasAssociatedToStadiumYetException() {
        ErrorInformation errorInformation = new ErrorInformation("Team has associated to stadium yet");
        Response badRequestResponse = status(BAD_REQUEST).entity(errorInformation).build();
        throw new BadRequestException(badRequestResponse);
    }
}
