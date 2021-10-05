package es.urjc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StadiumBasicInformation {

    private long id;
    private String name;
    private Integer capacity;

}
