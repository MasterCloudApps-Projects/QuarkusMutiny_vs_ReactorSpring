package es.urjc.resource;

import es.urjc.Helper;
import es.urjc.dto.request.MovieRequest;
import es.urjc.dto.response.MovieResponse;
import es.urjc.entity.Movie;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static es.urjc.Helper.*;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Path("/movies")
public class MovieResource {

    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int PAGE_SIZE = 20;

    @GET
    @Path("/{movieId}")
    public Response getMovieById(@PathParam("movieId") String movieId) {
        return verifyId(movieId)
                .map(id -> Movie.findByIdOptional(id)
                        .map(Movie.class::cast)
                        .map(Helper::convertToDto)
                        .map(Response::ok)
                        .orElse(Response.status(NOT_FOUND)))
                .orElse(Response.status(BAD_REQUEST))
                .build();
    }

    @GET
    @Path("/")
    public List<MovieResponse> getMoviesOrderByRating(@QueryParam("page") Optional<Integer> page) {
        return Movie.findByPage(page.orElse(DEFAULT_PAGE_NUMBER), PAGE_SIZE).stream()
                .map(Helper::convertToDto)
                .collect(Collectors.toList());
    }

    @POST
    @Path("/")
    public Response saveMovie(@Valid @RequestBody MovieRequest movieRequest,
                              @Context UriInfo uriInfo) {

        Movie movie = convertToEntity(movieRequest);
        Movie.persist(movie);

        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(movie.getId().toHexString());
        return Response.created(builder.build()).build();
    }

    @PATCH
    @Path("/{movieId}")
    public Response updateMovie(@PathParam("movieId") String movieId,
                                @RequestBody MovieRequest movieRequest,
                                @Context UriInfo uriInfo) {

        return verifyId(movieId)
                .map(objectId -> Movie.findByIdOptional(objectId)
                        .map(Movie.class::cast)
                        .map(movie -> updateMovieData(movie, movieRequest))
                        .map(movie -> {
                            Movie.update(movie);
                            return movie.getId();
                        })
                        .map(id -> uriInfo.getAbsolutePathBuilder().path(id.toHexString()))
                        .map(uriBuilder -> Response.ok(uriBuilder.build()))
                        .orElse(Response.status(NOT_FOUND)))
                .orElse(Response.status(BAD_REQUEST))
                .build();
    }

    @DELETE
    @Path("/{movieId}")
    public Response deleteMovie(@PathParam("movieId") String movieId,
                                @Context UriInfo uriInfo) {

        ObjectId objectId = verifyId(movieId).orElseThrow(BadRequestException::new);
        boolean isDeleted = Movie.deleteById(objectId);

        return (isDeleted) ? Response.noContent().build() : Response.status(NOT_FOUND).build();
    }

}