package es.urjc.resource;

import es.urjc.dto.*;
import es.urjc.entity.Player;
import es.urjc.entity.Stadium;
import es.urjc.entity.Team;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static javax.ws.rs.core.Response.*;
import static javax.ws.rs.core.Response.Status.*;

@Path("/teams")
public class TeamResource {

    @GET
    @Path("")
    public List<TeamFullInformation> getAllTeams() {
        return Team.listAll().stream()
                .map(Team.class::cast)
                .map(team -> {

                    TeamFullInformation.TeamFullInformationBuilder teamFullInformationBuilder =
                            TeamFullInformation.builder()
                                    .id(team.getId())
                                    .name(team.getName())
                                    .ranking(team.getRanking())
                                    .players(getPlayersBasicInformation(team));

                    if (nonNull(team.getStadium())) {
                        teamFullInformationBuilder.stadium(getStadiumBasicInformation(team));
                    }

                    return teamFullInformationBuilder.build();
                })
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{name}")
    public Response getTeam(@PathParam("name") String name) {

        return Team.findByName(name)
                .map(team -> {
                    TeamFullInformation.TeamFullInformationBuilder teamFullInformationBuilder =
                            TeamFullInformation.builder()
                                    .id(team.getId())
                                    .name(team.getName())
                                    .ranking(team.getRanking())
                                    .players(getPlayersBasicInformation(team));

                    if (nonNull(team.getStadium())) {
                        teamFullInformationBuilder.stadium(getStadiumBasicInformation(team));
                    }

                    return teamFullInformationBuilder.build();

                })
                .map(Response::ok)
                .orElse(Response.status(NOT_FOUND).entity(new ErrorInformation("Not found team")))
                .build();
    }

    @POST
    @Path("")
    @Transactional
    public Response saveTeam(Team newTeam) {

        if (Team.findByName(newTeam.getName()).isEmpty()) {
            newTeam.persist();
            return getTeam(newTeam.getName());
        } else {
            return status(BAD_REQUEST).entity(new ErrorInformation("Already exists team")).build();
        }

    }

    @POST
    @Path("/{teamId}/players/{playerId}")
    @Transactional
    public Response addPlayerToTeam(@PathParam("teamId") long teamId,
                                    @PathParam("playerId") long playerId) {

        Team team = getTeam(teamId);
        Player player = getPlayer(playerId);

        team.addPlayer(player);
        player.addTeam(team);

        team.persist();

        List<PlayerBasicInformation> players = getPlayersBasicInformation(team);
        TeamFullInformation teamFullInformation = new TeamFullInformation(team.getId(), team.getName(), team.getRanking(), players);

        return ok(teamFullInformation).build();

    }

    @DELETE
    @Path("/{teamId}/players/{playerId}")
    @Transactional
    public Response deletePlayerToTeam(@PathParam("teamId") long teamId,
                                       @PathParam("playerId") long playerId) {

        Team team = getTeam(teamId);
        Player player = getPlayer(playerId);

        team.removePlayer(player);
        player.removeTeam(team);

        team.persist();

        List<PlayerBasicInformation> players = getPlayersBasicInformation(team);
        TeamFullInformation teamFullInformation = new TeamFullInformation(team.getId(), team.getName(), team.getRanking(), players);

        return ok(teamFullInformation).build();

    }

    @PUT
    @Path("/update-method/{teamId}")
    @Transactional
    public Response updateTeamByUpdateMethod(@PathParam("teamId") long teamId, Team updateTeam) {
        Team.update("name = ?1, ranking = ?2 where id = ?3", updateTeam.getName(), updateTeam.getRanking(), teamId);
        return getTeam(updateTeam.getName());
    }

    @PUT
    @Path("/persist-method/{teamId}")
    @Transactional
    public Response updateTeamByPersistMethod(@PathParam("teamId") long teamId, Team updateTeam) {
        return Team.findByIdOptional(teamId)
                .map(Team.class::cast)
                .map(team -> {
                    team.setName(updateTeam.getName());
                    team.setRanking(updateTeam.getRanking());
                    return team;
                })
                .map(updatedTeam -> {
                    updatedTeam.persist();
                    return updatedTeam.isPersistent() ? getTeam(updatedTeam.getName()) : serverError().build();
                })
                .orElse(Response.status(NOT_FOUND).entity(new ErrorInformation("Not found team")).build());
    }

    @DELETE
    @Path("/{teamId}")
    @Transactional
    public Response deleteTeam(@PathParam("teamId") long teamId) {

        Team team = Team.findByIdOptional(teamId)
                .map(Team.class::cast)
                .orElseThrow(NotFoundException::new);

        if (team.getPlayers().isEmpty() && isNull(team.getStadium())) {
            boolean isDeleted = Team.deleteById(teamId);
            ResponseBuilder responseBuilder = isDeleted
                    ? noContent()
                    : status(NOT_FOUND).entity(new ErrorInformation("Not found player"));
            return responseBuilder.build();
        } else {
            return status(BAD_REQUEST).build();
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

    private Team getTeam(long teamId) {
        return Team.findByIdOptional(teamId)
                .map(Team.class::cast)
                .orElseThrow(() -> {
                    Response response = status(NOT_FOUND).entity(new ErrorInformation("Not found team")).build();
                    return new NotFoundException(response);
                });
    }

    private List<PlayerBasicInformation> getPlayersBasicInformation(Team team) {
        return team.getPlayers().stream()
                .map(player -> new PlayerBasicInformation(player.getId(), player.getName(), player.getGoals()))
                .collect(Collectors.toList());
    }

    private StadiumBasicInformation getStadiumBasicInformation(Team team) {
        Stadium stadium = team.getStadium();
        return new StadiumBasicInformation(stadium.getId(), stadium.getName(), stadium.getCapacity());
    }

}