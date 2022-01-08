package es.urjc.resource;

import es.urjc.Helper;
import es.urjc.dto.request.MovieRequest;
import es.urjc.dto.response.MovieResponse;
import es.urjc.entity.Movie;
import es.urjc.repository.MovieRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponents;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static es.urjc.Helper.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController("/movies")
public class MovieController {

    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int PAGE_SIZE = 20;

    private final MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieResponse> getMovieById(@PathParam("movieId") String movieId) {
        return verifyId(movieId)
                .map(id -> movieRepository.findById(id)
                        .map(Helper::convertToDto)
                        .map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build()))
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/")
    public List<MovieResponse> getMoviesOrderByRating(@RequestParam("page") Optional<Integer> page) {
        Sort sortByImdbRating = Sort.by(Sort.Direction.DESC, "imdb.rating");
        PageRequest paging = PageRequest.of(page.orElse(DEFAULT_PAGE_NUMBER), PAGE_SIZE, sortByImdbRating);
        return movieRepository.findAll(paging).stream()
                .map(Helper::convertToDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/")
    public ResponseEntity<UriComponents> saveMovie(@Valid @RequestBody MovieRequest movieRequest) {

        Movie movie = convertToEntity(movieRequest);
        Movie savedMovie = movieRepository.save(movie);

        UriBuilder builder = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(savedMovie.getId().toHexString());

        return ResponseEntity.created(builder.build()).build();
    }

    @PatchMapping("/{movieId}")
    public ResponseEntity<UriComponents> updateMovie(@PathParam("movieId") String movieId,
                                                     @RequestBody MovieRequest movieRequest) {

        return verifyId(movieId)
                .map(objectId -> movieRepository.findById(objectId)
                        .map(movie -> updateMovieData(movie, movieRequest))
                        .map(movie -> {
                            Movie savedMovie = movieRepository.save(movie);
                            return savedMovie.getId();
                        })
                        .map(id -> ServletUriComponentsBuilder.fromCurrentContextPath().path(id.toHexString()).build())
                        .map(ResponseEntity::ok)
                        .orElse(ResponseEntity.status(NOT_FOUND).build()))
                .orElse(ResponseEntity.status(BAD_REQUEST).build());
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<Object> deleteMovie(@PathParam("movieId") String movieId) {

        return verifyId(movieId)
                .map(objectId -> {
                    movieRepository.deleteById(objectId);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.badRequest().build());
    }

}