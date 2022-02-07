package es.urjc.resource;

import es.urjc.dto.ErrorInformation;
import es.urjc.dto.StadiumFullInformation;
import es.urjc.dto.TeamBasicInformation;
import es.urjc.entity.Stadium;
import es.urjc.entity.Team;
import es.urjc.repository.StadiumRepository;
import es.urjc.repository.TeamRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

import static javax.ws.rs.core.Response.*;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Path("/stadiums")
public class StadiumResource {

    private final StadiumRepository stadiumRepository;
    private final TeamRepository teamRepository;

    @Inject
    public StadiumResource(StadiumRepository stadiumRepository, TeamRepository teamRepository) {
        this.stadiumRepository = stadiumRepository;
        this.teamRepository = teamRepository;
    }

    @GET
    @Path("/")
    public List<StadiumFullInformation> getAllStadiums() {
        return this.stadiumRepository.findAll().stream()
                .map(stadium -> {
                    TeamBasicInformation team = getTeamBasicInformation(stadium);
                    return new StadiumFullInformation(stadium.getId(), stadium.getName(), stadium.getCapacity(), team);
                })
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{name}")
    public Response getStadium(@PathParam("name") String name) {

        return this.stadiumRepository.findByName(name)
                .map(stadium -> {
                    TeamBasicInformation team = getTeamBasicInformation(stadium);
                    return new StadiumFullInformation(stadium.getId(), stadium.getName(), stadium.getCapacity(), team);
                })
                .map(Response::ok)
                .orElse(Response.status(NOT_FOUND).entity(new ErrorInformation("Not found stadium")))
                .build();
    }

    @POST
    @Path("/team/{teamId}")
    @Transactional
    public Response saveStadium(@PathParam("teamId") long teamId, Stadium stadium) {

        Team team = this.teamRepository.findById(teamId)
                .orElseThrow(()-> {
                    Response response = status(NOT_FOUND).entity(new ErrorInformation("Not found team")).build();
                    return new NotFoundException(response);
                });

        if (this.stadiumRepository.findByName(stadium.getName()).isEmpty()) {
            stadium.setTeam(team);
            this.stadiumRepository.save(stadium);
            return getStadium(stadium.getName());
        } else {
            return status(BAD_REQUEST).entity(new ErrorInformation("Stadium is already exists")).build();
        }

    }

    @PUT
    @Path("/update-method/{stadiumId}")
    @Transactional
    public Response updateStadiumByUpdateMethod(@PathParam("stadiumId") long stadiumId, Stadium updateStadium) {
        this.stadiumRepository.update(updateStadium.getName(), updateStadium.getCapacity(), stadiumId);
        return getStadium(updateStadium.getName());
    }

    @PUT
    @Path("/save-method/{stadiumId}")
    @Transactional
    public Response updateStadiumByPersistMethod(@PathParam("stadiumId") long stadiumId, Stadium updateStadium) {
        return this.stadiumRepository.findById(stadiumId)
                .map(stadium -> {
                    stadium.setName(updateStadium.getName());
                    stadium.setCapacity(updateStadium.getCapacity());
                    return stadium;
                })
                .map(updatedStadium -> {
                    this.stadiumRepository.save(updatedStadium);
                    return getStadium(updatedStadium.getName());
                })
                .orElse(Response.status(NOT_FOUND).entity(new ErrorInformation("Not found stadium")).build());
    }

    @DELETE
    @Path("/{stadiumId}")
    @Transactional
    public Response deleteTeam(@PathParam("stadiumId") long stadiumId) {
        return this.stadiumRepository.findById(stadiumId)
                .map(stadium -> {
                    this.stadiumRepository.deleteById(stadiumId);
                    return noContent();
                })
                .orElse(Response.status(NOT_FOUND).entity(new ErrorInformation("Not found stadium")))
                .build();

    }

    private TeamBasicInformation getTeamBasicInformation(Stadium stadium) {
        Team team = stadium.getTeam();
        return new TeamBasicInformation(team.getId(), team.getName(), team.getRanking());
    }

}