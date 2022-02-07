package es.urjc.repository;

import es.urjc.entity.Movie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface MovieRepository extends ReactiveCrudRepository<Movie, Long> {

    Flux<Movie> findAllBy(Pageable pageable);

    @Modifying
    @Query("update movies m set m.rating = :rating where m.id = :movieId")
    Mono<Void> update(@Param("rating") Double rating, @Param("movieId") Long movieId);

}
