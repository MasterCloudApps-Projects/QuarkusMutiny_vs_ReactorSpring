package es.urjc.repository;

import es.urjc.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findByName(String name);

    @Modifying
    @Query("update Team t " +
            "set t.name = :name, t.ranking = :ranking " +
            "where t.id = :teamId")
    void update(@Param("name") String name, @Param("ranking") Integer ranking, @Param("teamId") Long teamId);

}
