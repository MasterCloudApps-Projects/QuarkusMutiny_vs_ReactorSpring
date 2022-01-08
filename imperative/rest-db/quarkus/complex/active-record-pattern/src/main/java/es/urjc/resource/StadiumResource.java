package es.urjc.resource;

import es.urjc.dto.ErrorInformation;
import es.urjc.dto.StadiumFullInformation;
import es.urjc.dto.TeamBasicInformation;
import es.urjc.entity.Stadium;
import es.urjc.entity.Team;

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

    @GET
    @Path("/")
    public List<StadiumFullInformation> getAllStadiums() {
        return Stadium.listAll().stream()
                .map(Stadium.class::cast)
                .map(stadium -> {
                    TeamBasicInformation team = getTeamBasicInformation(stadium);
                    return new StadiumFullInformation(stadium.getId(), stadium.getName(), stadium.getCapacity(), team);
                })
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{name}")
    public Response getStadium(@PathParam("name") String name) {

        return Stadium.findByName(name)
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

        Team team = Team.findByIdOptional(teamId)
                .map(Team.class::cast)
                .orElseThrow(()-> {
                    Response response = status(NOT_FOUND).entity(new ErrorInformation("Not found team")).build();
                    return new NotFoundException(response);
                });

        if (Stadium.findByName(stadium.getName()).isEmpty()) {
            stadium.setTeam(team);
            stadium.persist();
            return getStadium(stadium.getName());
        } else {
            return status(BAD_REQUEST).entity(new ErrorInformation("Stadium is already exists")).build();
        }

    }

    @PUT
    @Path("/update-method/{stadiumId}")
    @Transactional
    public Response updateStadiumByUpdateMethod(@PathParam("stadiumId") long stadiumId, Stadium updateStadium) {

        Stadium.update("name = ?1, capacity = ?2 where id = ?3", updateStadium.getName(),
                updateStadium.getCapacity(), stadiumId);

        return getStadium(updateStadium.getName());
    }

    @PUT
    @Path("/persist-method/{stadiumId}")
    @Transactional
    public Response updateStadiumByPersistMethod(@PathParam("stadiumId") long stadiumId, Stadium updateStadium) {
        return Stadium.findByIdOptional(stadiumId)
                .map(Stadium.class::cast)
                .map(stadium -> {
                    stadium.setName(updateStadium.getName());
                    stadium.setCapacity(updateStadium.getCapacity());
                    return stadium;
                })
                .map(updatedStadium -> {
                    updatedStadium.persist();
                    return updatedStadium.isPersistent() ? getStadium(updatedStadium.getName()) : serverError().build();
                })
                .orElse(Response.status(NOT_FOUND).entity(new ErrorInformation("Not found stadium")).build());
    }

    @DELETE
    @Path("/{stadiumId}")
    @Transactional
    public Response deleteTeam(@PathParam("stadiumId") long stadiumId) {

        return Stadium.findByIdOptional(stadiumId)
                .map(Stadium.class::cast)
                .map(stadium -> {
                    boolean isDeleted = Stadium.deleteById(stadiumId);
                    return isDeleted ? noContent() : status(NOT_FOUND);
                })
                .orElse(Response.status(NOT_FOUND).entity(new ErrorInformation("Not found stadium")))
                .build();

    }

    private TeamBasicInformation getTeamBasicInformation(Stadium stadium) {
        Team team = stadium.getTeam();
        return new TeamBasicInformation(team.getId(), team.getName(), team.getRanking());
    }

}