package es.urjc.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TeamFullInformation extends TeamBasicInformation {

    private List<PlayerBasicInformation> players;

    public TeamFullInformation(long id, String name, int ranking, List<PlayerBasicInformation> players) {
        super(id, name, ranking);
        this.players = players;
    }

    @Builder
    public TeamFullInformation(long id, String name, int ranking, StadiumBasicInformation stadium, List<PlayerBasicInformation> players) {
        super(id, name, ranking, stadium);
        this.players = players;
    }
}
