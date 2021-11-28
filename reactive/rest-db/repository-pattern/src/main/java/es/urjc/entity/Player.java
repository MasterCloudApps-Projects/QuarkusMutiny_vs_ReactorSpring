package es.urjc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    private int goals;

    @OneToMany(mappedBy = "player")
    private List<Car> cars;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "player_team",
            joinColumns = {@JoinColumn(name = "player_id")},
            inverseJoinColumns = {@JoinColumn(name = "team_id")})
    private List<Team> teams = new ArrayList<>();

    public void addTeam(Team team) {
        this.getTeams().add(team);
    }

    public void removeTeam(Team team) {
        this.getTeams().remove(team);
    }

}
