package es.urjc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerBasicInformation {

    private long id;
    private String name;
    private int goals;

}
