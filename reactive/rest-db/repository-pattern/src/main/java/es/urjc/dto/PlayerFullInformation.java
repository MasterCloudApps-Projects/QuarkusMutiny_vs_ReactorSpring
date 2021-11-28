package es.urjc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerFullInformation extends PlayerBasicInformation {

    private List<TeamBasicInformation> teams;
    private List<CarBasicInformation> cars;

    public PlayerFullInformation(long id, String name, int goals,
                                 List<TeamBasicInformation> teams, List<CarBasicInformation> cars) {
        super(id, name, goals);
        this.teams = teams;
        this.cars = cars;
    }

}
