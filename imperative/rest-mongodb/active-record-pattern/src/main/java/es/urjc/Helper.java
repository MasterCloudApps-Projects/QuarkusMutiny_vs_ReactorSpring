package es.urjc;

import es.urjc.dto.request.AwardsRequest;
import es.urjc.dto.request.ImdbRequest;
import es.urjc.dto.request.MovieRequest;
import es.urjc.dto.response.ImdbResponse;
import es.urjc.dto.response.MovieResponse;
import es.urjc.entity.Awards;
import es.urjc.entity.Imdb;
import es.urjc.entity.Movie;
import io.netty.util.internal.ObjectUtil;
import org.bson.types.ObjectId;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Helper {

    public static Optional<ObjectId> verifyId(String id) {
        return ObjectId.isValid(id) ? Optional.of(new ObjectId(id)) : Optional.empty();
    }

    public static String asText(List<String> values) {
        if (nonNull(values)) {
            return String.join(", ", values);
        }
        return null;
    }

    public static List<String> asList(String values) {
        if (nonNull(values)) {
            return Arrays.stream(values.split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
        }
        return null;
    }

    public static MovieResponse convertToDto(Movie movie) {

        MovieResponse.MovieResponseBuilder movieResponseBuilder = MovieResponse.builder()
                .id(movie.getId().toHexString())
                .title(movie.getTitle())
                .plot(movie.getPlot())
                .fullPlot(movie.getFullPlot())
                .genres(asText(movie.getGenres()))
                .runtime(movie.getRuntime())
                .year(movie.getYear())
                .type(movie.getType())
                .released(movie.getReleased())
                .directors(asText(movie.getDirectors()))
                .rated(movie.getRated())
                .cast(asText(movie.getCast()))
                .countries(asText(movie.getCountries()));

        if (nonNull(movie.getAwards())) {
            movieResponseBuilder.awards(movie.getAwards().getText());
        }

        if (nonNull(movie.getImdb())) {
            ImdbResponse imdb = new ImdbResponse(movie.getImdb().getRating(), movie.getImdb().getVotes());
            movieResponseBuilder.imdb(imdb);
        }

        return movieResponseBuilder.build();
    }

    public static Awards convertToEntity(AwardsRequest awardsRequest) {
        return new Awards(awardsRequest.getWins(), awardsRequest.getNominations(), awardsRequest.getText());
    }

    public static Imdb convertToEntity(ImdbRequest imdbRequest) {
        Integer imdbId = Math.abs(new Random().nextInt());
        return new Imdb(imdbRequest.getRating(), imdbRequest.getVotes(), imdbId);
    }

    public static Movie convertToEntity(MovieRequest movieRequest) {

        Movie.MovieBuilder movieBuilder = Movie.builder()
                .title(movieRequest.getTitle())
                .plot(movieRequest.getPlot())
                .fullPlot(movieRequest.getFullPlot())
                .genres(asList(movieRequest.getGenres()))
                .runtime(movieRequest.getRuntime())
                .year(movieRequest.getYear())
                .type(movieRequest.getType())
                .released(movieRequest.getReleased())
                .directors(asList(movieRequest.getDirectors()))
                .rated(movieRequest.getRated())
                .cast(asList(movieRequest.getCast()))
                .countries(asList(movieRequest.getCountries()));

        if (nonNull(movieRequest.getAwards())) {
            movieBuilder.awards(convertToEntity(movieRequest.getAwards()));
        }

        if (nonNull(movieRequest.getImdb())) {
            movieBuilder.imdb(convertToEntity(movieRequest.getImdb()));
        }

        return movieBuilder.build();

    }

    public static Movie updateMovieData(Movie movie, MovieRequest movieRequest) {

        Movie movieToUpdate = new Movie();

        movieToUpdate.setId(movie.getId());
        movieToUpdate.setTitle(isNull(movieRequest.getTitle()) ? movie.getTitle() : movieRequest.getTitle());
        movieToUpdate.setPlot(isNull(movieRequest.getPlot()) ? movie.getPlot() : movieRequest.getPlot());
        movieToUpdate.setFullPlot(isNull(movieRequest.getFullPlot()) ? null : movieRequest.getFullPlot());
        movieToUpdate.setGenres(isNull(movieRequest.getGenres()) ? null : asList(movieRequest.getGenres()));
        movieToUpdate.setRuntime(isNull(movieRequest.getRuntime()) ? null : movieRequest.getRuntime());
        movieToUpdate.setYear(isNull(movieRequest.getYear()) ? null : movieRequest.getYear());
        movieToUpdate.setType(isNull(movieRequest.getType()) ? null : movieRequest.getType());
        movieToUpdate.setReleased(isNull(movieRequest.getReleased()) ? null : movieRequest.getReleased());
        movieToUpdate.setDirectors(isNull(movieRequest.getDirectors()) ? null : asList(movieRequest.getDirectors()));
        movieToUpdate.setRated(isNull(movieRequest.getRated()) ? null : movieRequest.getRated());
        movieToUpdate.setCast(isNull(movieRequest.getCast()) ? null : asList(movieRequest.getCast()));
        movieToUpdate.setCountries(isNull(movieRequest.getCountries()) ? null : asList(movieRequest.getCountries()));
        movieToUpdate.setAwards(isNull(movieRequest.getAwards()) ? null : convertToEntity(movieRequest.getAwards()));
        movieToUpdate.setImdb(isNull(movieRequest.getImdb()) ? null : convertToEntity(movieRequest.getImdb()));

        return movieToUpdate;
    }

}
