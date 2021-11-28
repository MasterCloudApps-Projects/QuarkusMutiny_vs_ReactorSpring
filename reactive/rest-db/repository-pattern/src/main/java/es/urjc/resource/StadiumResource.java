package es.urjc.resource;

import es.urjc.dto.ErrorInformation;
import es.urjc.dto.StadiumFullInformation;
import es.urjc.dto.TeamBasicInformation;
import es.urjc.entity.Stadium;
import es.urjc.entity.Team;
import es.urjc.repository.StadiumRepository;
import es.urjc.repository.TeamRepository;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.*;

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
    public Multi<StadiumFullInformation> getAllStadiums() {

        return this.stadiumRepository.listAll()
                .map(stadiumList -> stadiumList.stream()
                        .map(stadium -> {
                            TeamBasicInformation team = getTeamBasicInformation(stadium);
                            return new StadiumFullInformation(stadium.getId(), stadium.getName(), stadium.getCapacity(), team);
                        }))
                .onItem().transformToMulti(stadiumStream -> Multi.createFrom().items(stadiumStream));
    }

    @GET
    @Path("/{name}")
    public Uni<Response> getStadium(@PathParam("name") String name) {

        return this.stadiumRepository.findByName(name)
                .onItem().ifNotNull().transform(stadium -> {
                    TeamBasicInformation team = getTeamBasicInformation(stadium);
                    return new StadiumFullInformation(stadium.getId(), stadium.getName(), stadium.getCapacity(), team);
                })
                .onItem().ifNotNull().transform(stadiumFullInformation -> ok(stadiumFullInformation).build())
                .onItem().ifNull().failWith(this::failWithNotFoundStadiumException);

    }

    @POST
    @Path("/team/{teamId}")
    @ReactiveTransactional
    public Uni<Response> saveStadium(@PathParam("teamId") long teamId, Stadium newStadium) {

        return this.stadiumRepository.findByName(newStadium.getName())
                .onItem().ifNotNull().failWith(this::failWithBadRequestExistingStadiumException)
                .onItem().ifNull().continueWith(newStadium)
                .flatMap(stadium ->
                        this.teamRepository.findById(teamId)
                                .onItem().castTo(Team.class)
                                .onItem().ifNull().failWith(this::failWithNotFoundTeamException)
                                .onItem().ifNotNull().transformToUni(team -> {
                                    stadium.setTeam(team);
                                    return this.stadiumRepository.persist(stadium);
                                })
                                .chain(() -> getStadium(newStadium.getName())));
    }

    @PUT
    @Path("/update-method/{stadiumId}")
    @ReactiveTransactional
    public Uni<Response> updateStadiumByUpdateMethod(@PathParam("stadiumId") long stadiumId, Stadium updateStadium) {

        return this.stadiumRepository.update("name = ?1, capacity = ?2 where id = ?3", updateStadium.getName(),
                        updateStadium.getCapacity(), stadiumId)
                .chain(numberOfStadiumUpdated -> {
                    if (numberOfStadiumUpdated == 1) {
                        return getStadium(updateStadium.getName());
                    }
                    throw failWithNotFoundStadiumException();
                });
    }

    @PUT
    @Path("/persist-method/{stadiumId}")
    @ReactiveTransactional
    public Uni<Response> updateStadiumByPersistMethod(@PathParam("stadiumId") long stadiumId, Stadium updateStadium) {

        return this.stadiumRepository.findById(stadiumId)
                .onItem().ifNotNull().transform(stadium -> {
                    stadium.setName(updateStadium.getName());
                    stadium.setCapacity(updateStadium.getCapacity());
                    return stadium;
                })
                .onItem().ifNull().failWith(this::failWithNotFoundStadiumException)
                .chain(this.stadiumRepository::persist)
                .chain(updatedStadium -> {
                    if (this.stadiumRepository.isPersistent(updatedStadium)) {
                        return getStadium(updatedStadium.getName());
                    }
                    throw new InternalServerErrorException();
                });
    }

    @DELETE
    @Path("/{stadiumId}")
    @ReactiveTransactional
    public Uni<Response> deleteTeam(@PathParam("stadiumId") long stadiumId) {

        return this.stadiumRepository.findById(stadiumId)
                .flatMap(stadium -> this.stadiumRepository.deleteById(stadiumId))
                .flatMap(isDeleted -> isDeleted
                        ? Uni.createFrom().item(noContent().build())
                        : Uni.createFrom().nullItem())
                .onItem().ifNull().failWith(this::failWithNotFoundStadiumException);
    }

    private TeamBasicInformation getTeamBasicInformation(Stadium stadium) {
        Team team = stadium.getTeam();
        return new TeamBasicInformation(team.getId(), team.getName(), team.getRanking());
    }

    private NotFoundException failWithNotFoundStadiumException() {
        ErrorInformation errorInformation = new ErrorInformation("Not found stadium");
        Response notFoundRequestResponse = status(NOT_FOUND).entity(errorInformation).build();
        return new NotFoundException(notFoundRequestResponse);
    }

    private BadRequestException failWithBadRequestExistingStadiumException() {
        ErrorInformation errorInformation = new ErrorInformation("Stadium is already exists");
        Response badRequestResponse = status(BAD_REQUEST).entity(errorInformation).build();
        return new BadRequestException(badRequestResponse);
    }

    private NotFoundException failWithNotFoundTeamException() {
        ErrorInformation errorInformation = new ErrorInformation("Not found team");
        Response notFoundRequestResponse = status(NOT_FOUND).entity(errorInformation).build();
        return new NotFoundException(notFoundRequestResponse);
    }
}