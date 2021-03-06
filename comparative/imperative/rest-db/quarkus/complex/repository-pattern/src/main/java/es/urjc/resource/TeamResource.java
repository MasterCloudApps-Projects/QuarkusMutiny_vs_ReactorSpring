package es.urjc.resource;

import es.urjc.dto.*;
import es.urjc.entity.Player;
import es.urjc.entity.Stadium;
import es.urjc.entity.Team;
import es.urjc.repository.PlayerRepository;
import es.urjc.repository.TeamRepository;

import javax.inject.Inject;
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

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    @Inject
    public TeamResource(TeamRepository teamRepository, PlayerRepository playerRepository) {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
    }

    @GET
    @Path("/")
    public List<TeamFullInformation> getAllTeams() {
        return this.teamRepository.listAll().stream()
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

        return this.teamRepository.findByName(name)
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
    @Path("/")
    @Transactional
    public Response saveTeams(Team newTeam) {

        if (this.teamRepository.findByName(newTeam.getName()).isEmpty()) {
            this.teamRepository.persist(newTeam);
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

        this.teamRepository.persist(team);

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

        this.teamRepository.persist(team);

        List<PlayerBasicInformation> players = getPlayersBasicInformation(team);
        TeamFullInformation teamFullInformation = new TeamFullInformation(team.getId(), team.getName(), team.getRanking(), players);

        return ok(teamFullInformation).build();

    }

    @PUT
    @Path("/update-method/{teamId}")
    @Transactional
    public Response updateTeamByUpdateMethod(@PathParam("teamId") long teamId, Team updateTeam) {

        this.teamRepository.update("name = ?1, ranking = ?2 where id = ?3",
                updateTeam.getName(), updateTeam.getRanking(), teamId);

        return getTeam(updateTeam.getName());
    }

    @PUT
    @Path("/persist-method/{teamId}")
    @Transactional
    public Response updateTeamByPersistMethod(@PathParam("teamId") long teamId, Team updateTeam) {
        return this.teamRepository.findByIdOptional(teamId)
                .map(team -> {
                    team.setName(updateTeam.getName());
                    team.setRanking(updateTeam.getRanking());
                    return team;
                })
                .map(updatedTeam -> {
                    this.teamRepository.persist(updatedTeam);
                    return this.teamRepository.isPersistent(updatedTeam)
                            ? getTeam(updatedTeam.getName())
                            : serverError().build();
                })
                .orElse(Response.status(NOT_FOUND).entity(new ErrorInformation("Not found team")).build());
    }

    @DELETE
    @Path("/{teamId}")
    @Transactional
    public Response deleteTeam(@PathParam("teamId") long teamId) {

        Team team = getTeam(teamId);

        if (team.getPlayers().isEmpty() && isNull(team.getStadium())) {
            boolean isDeleted = this.teamRepository.deleteById(teamId);
            ResponseBuilder responseBuilder = isDeleted
                    ? noContent()
                    : status(NOT_FOUND).entity(new ErrorInformation("Not found player"));
            return responseBuilder.build();
        } else {
            return status(BAD_REQUEST).build();
        }

    }

    private Player getPlayer(long playerId) {
        return this.playerRepository.findByIdOptional(playerId)
                .orElseThrow(() -> {
                    Response response = status(NOT_FOUND).entity(new ErrorInformation("Not found player")).build();
                    return new NotFoundException(response);
                });
    }

    private Team getTeam(long teamId) {
        return this.teamRepository.findByIdOptional(teamId)
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