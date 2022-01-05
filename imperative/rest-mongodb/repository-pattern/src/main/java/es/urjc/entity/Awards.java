package es.urjc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Awards {

    private Integer wins;
    private Integer nominations;
    private String text;

}
