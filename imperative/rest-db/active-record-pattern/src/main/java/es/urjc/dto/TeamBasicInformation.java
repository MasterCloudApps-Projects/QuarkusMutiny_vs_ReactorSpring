package es.urjc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamBasicInformation {

    private long id;
    private String name;
    private int ranking;
    private StadiumBasicInformation stadium;

    public TeamBasicInformation(long id, String name, int ranking) {
        this.id = id;
        this.name = name;
        this.ranking = ranking;
    }
}
