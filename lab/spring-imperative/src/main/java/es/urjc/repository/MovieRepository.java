package es.urjc.repository;

import es.urjc.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Modifying
    @Query("update Movie m set m.rating = :rating where m.id = :movieId")
    void update(@Param("rating") Double rating, @Param("movieId") Long movieId);

}
