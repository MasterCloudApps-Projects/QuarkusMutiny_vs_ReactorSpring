package es.urjc.resource;

import es.urjc.dto.MovieRequest;
import es.urjc.dto.MovieResponse;
import es.urjc.dto.RatingRequest;
import es.urjc.entity.Movie;
import es.urjc.repository.MovieRepository;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Uni;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Path("/movies")
public class MovieResource {

    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int PAGE_SIZE = 20;

    private final MovieRepository movieRepository;

    public MovieResource(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GET
    @Path("/{movieId}")
    public Uni<Response> getMovieById(@PathParam("movieId") Long movieId) {

        return movieRepository.findById(movieId).onItem().ifNull().failWith(NotFoundException::new).onItem().castTo(Movie.class).map(this::convertToResponse).map(Response::ok).onFailure(NotFoundException.class).recoverWithItem(Response.status(NOT_FOUND)).map(Response.ResponseBuilder::build);
    }

    @GET
    public Uni<List<MovieResponse>> getAllMoviesByPage(@QueryParam("page") Optional<Integer> pageNumber) {

        Page page = Page.of(pageNumber.orElse(DEFAULT_PAGE_NUMBER), PAGE_SIZE);

        return movieRepository.findByPage(page).map(movies -> movies.stream().map(this::convertToResponse).collect(Collectors.toList()));
    }


    @POST
    public Uni<MovieResponse> saveMovie(MovieRequest movieRequest) {

        return Uni.createFrom().item(convertToEntity(movieRequest)).call(movieRepository::persistAndFlush).map(this::convertToResponse);

    }

    @PATCH
    @Path("/{movieId}/rating")
    @ReactiveTransactional
    public Uni<Response> updateRating(@PathParam("movieId") Long movieId, RatingRequest ratingRequest) {

        return movieRepository.update("rating = ?1 where id = ?2", ratingRequest.getValue(), movieId).replaceWith(getMovieById(movieId));

    }


    @PUT
    @Path("/{movieId}")
    @ReactiveTransactional
    public Uni<Response> updateMovie(@PathParam("movieId") Long movieId, MovieRequest movieRequest) {

        return movieRepository.findById(movieId).onItem().ifNull().failWith(NotFoundException::new).onItem().castTo(Movie.class).invoke(movie -> updateData(movie, movieRequest)).call(movieRepository::persistAndFlush).map(this::convertToResponse).map(Response::ok).onFailure(NotFoundException.class).recoverWithItem(Response.status(NOT_FOUND)).map(Response.ResponseBuilder::build);
    }


    @DELETE
    @Path("/{movieId}")
    @ReactiveTransactional
    public Uni<Response> deleteMovie(@PathParam("movieId") Long movieId) {

        return movieRepository.deleteById(movieId).map(isDeleted -> isDeleted ? Response.noContent() : Response.status(NOT_FOUND)).map(Response.ResponseBuilder::build);
    }

    private MovieResponse convertToResponse(Movie movie) {
        return MovieResponse.builder().id(movie.getId()).poster(movie.getPoster()).title(movie.getTitle()).releasedYear(movie.getReleasedYear()).certificate(movie.getCertificate()).runtime(movie.getRuntime()).genre(movie.getGenre()).rating(movie.getRating()).overview(movie.getOverview()).director(movie.getDirector()).build();
    }

    private Movie convertToEntity(MovieRequest movieRequest) {

        return Movie.builder().poster(movieRequest.getPoster()).title(movieRequest.getTitle()).releasedYear(movieRequest.getReleasedYear()).certificate(movieRequest.getCertificate()).runtime(movieRequest.getRuntime()).genre(movieRequest.getGenre()).rating(movieRequest.getRating()).overview(movieRequest.getOverview()).director(movieRequest.getDirector()).build();

    }

    private void updateData(Movie movie, MovieRequest movieRequest) {

        movie.setTitle(isNull(movieRequest.getTitle()) ? movie.getTitle() : movieRequest.getTitle());
        movie.setPoster(isNull(movieRequest.getPoster()) ? movie.getPoster() : movieRequest.getPoster());
        movie.setReleasedYear(isNull(movieRequest.getReleasedYear()) ? movie.getReleasedYear() : movieRequest.getReleasedYear());
        movie.setCertificate(isNull(movieRequest.getCertificate()) ? movie.getCertificate() : movieRequest.getCertificate());
        movie.setRuntime(isNull(movieRequest.getRuntime()) ? movie.getRuntime() : movieRequest.getRuntime());
        movie.setGenre(isNull(movieRequest.getGenre()) ? movie.getGenre() : movieRequest.getGenre());
        movie.setRating(isNull(movieRequest.getRating()) ? movie.getRating() : movieRequest.getRating());
        movie.setOverview(isNull(movieRequest.getOverview()) ? movie.getOverview() : movieRequest.getOverview());
        movie.setDirector(isNull(movieRequest.getDirector()) ? movie.getDirector() : movieRequest.getDirector());

    }
}