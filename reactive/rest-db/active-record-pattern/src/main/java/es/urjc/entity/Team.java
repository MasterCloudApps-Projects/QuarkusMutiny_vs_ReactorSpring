package es.urjc.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.smallrye.mutiny.Uni;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Team extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    private int ranking;

    @OneToOne(cascade = CascadeType.REMOVE, mappedBy = "team")
    private Stadium stadium;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "teams")
    private List<Player> players = new ArrayList<>();

    public void addPlayer(Player player) {
        this.getPlayers().add(player);
    }

    public void removePlayer(Player player) {
        this.getPlayers().remove(player);
    }

    public static Uni<Team> findByName(String name) {
        return find("name", name).firstResult();
    }

}
