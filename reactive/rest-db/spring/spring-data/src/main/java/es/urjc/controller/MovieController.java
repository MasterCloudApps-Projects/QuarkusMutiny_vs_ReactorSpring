package es.urjc.controller;

import es.urjc.dto.MovieRequest;
import es.urjc.dto.MovieResponse;
import es.urjc.dto.RatingRequest;
import es.urjc.entity.Movie;
import es.urjc.repository.MovieRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int PAGE_SIZE = 20;

    private final MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GetMapping("/{movieId}")
    public Mono<ResponseEntity<MovieResponse>> getMovieById(@PathVariable("movieId") Long movieId) {

        return movieRepository.findById(movieId)
                .map(this::convertToResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Mono<List<MovieResponse>> getAllMoviesByPage(@RequestParam("page") Optional<Integer> pageNumber) {

        Pageable page = PageRequest.of(pageNumber.orElse(DEFAULT_PAGE_NUMBER), PAGE_SIZE);

        return movieRepository.findAllBy(page)
                .map(this::convertToResponse)
                .collectList();
    }

    @Transactional
    @PostMapping
    public Mono<MovieResponse> saveMovie(@RequestBody MovieRequest movieRequest) {

        return Mono.just(convertToEntity(movieRequest))
                .flatMap(movieRepository::save)
                .map(this::convertToResponse);
    }


    @Transactional
    @PatchMapping("/{movieId}/rating")
    public Mono<ResponseEntity<MovieResponse>> updateRating(@PathVariable("movieId") Long movieId,
                                                            @RequestBody RatingRequest ratingRequest) {

        return movieRepository.update(ratingRequest.getValue(), movieId)
                .then(getMovieById(movieId));

    }


    @Transactional
    @PutMapping("/{movieId}")
    public Mono<ResponseEntity<MovieResponse>> updateMovie(@PathVariable("movieId") Long movieId,
                                                           @RequestBody MovieRequest movieRequest) {

        return movieRepository.findById(movieId)
                .doOnNext(movie -> updateData(movie, movieRequest))
                .flatMap(movieRepository::save)
                .map(this::convertToResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());

    }

    @Transactional
    @DeleteMapping("/{movieId}")
    public Mono<ResponseEntity<Void>> deleteMovie(@PathVariable("movieId") Long movieId) {

        return movieRepository.deleteById(movieId)
                .thenReturn(ResponseEntity.noContent().build());

    }

    private MovieResponse convertToResponse(Movie movie) {
        return MovieResponse.builder()
                .id(movie.getId())
                .poster(movie.getPoster())
                .title(movie.getTitle())
                .releasedYear(movie.getReleasedYear())
                .certificate(movie.getCertificate())
                .runtime(movie.getRuntime())
                .genre(movie.getGenre())
                .rating(movie.getRating())
                .overview(movie.getOverview())
                .director(movie.getDirector())
                .build();
    }

    private Movie convertToEntity(MovieRequest movieRequest) {

        return Movie.builder()
                .poster(movieRequest.getPoster())
                .title(movieRequest.getTitle())
                .releasedYear(movieRequest.getReleasedYear())
                .certificate(movieRequest.getCertificate())
                .runtime(movieRequest.getRuntime())
                .genre(movieRequest.getGenre())
                .rating(movieRequest.getRating())
                .overview(movieRequest.getOverview())
                .director(movieRequest.getDirector())
                .build();

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