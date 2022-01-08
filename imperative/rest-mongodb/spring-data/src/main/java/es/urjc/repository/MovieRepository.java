package es.urjc.repository;

import es.urjc.entity.Movie;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends MongoRepository<Movie, ObjectId> {

//    @Query(sort = "{ 'imdb.rating' : 1 }")
//    Page<Movie> findAll(Pageable pageable);

}
