package es.urjc.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StadiumFullInformation extends StadiumBasicInformation {

    private TeamBasicInformation team;

    public StadiumFullInformation(long id, String name, Integer capacity, TeamBasicInformation team) {
        super(id, name, capacity);
        this.team = team;
    }
}
