package es.urjc.repository;

import es.urjc.entity.Stadium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StadiumRepository extends JpaRepository<Stadium, Long> {

    Optional<Stadium> findByName(String name);

    @Modifying
    @Query("update Stadium s " +
            "set s.name = :name, s.capacity = :capacity " +
            "where s.id = :stadiumId")
    void update(@Param("name") String name, @Param("capacity") Integer capacity, @Param("stadiumId") Long stadiumId);

}
