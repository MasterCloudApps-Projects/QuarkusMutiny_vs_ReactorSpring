package es.urjc.repository;

import es.urjc.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByName(String name);

    @Modifying
    @Query("update Player p " +
            "set p.name = :name, p.goals = :goals " +
            "where p.id = :playerId")
    void update(@Param("name") String name, @Param("goals") Integer goals, @Param("playerId") Long playerId);

}
