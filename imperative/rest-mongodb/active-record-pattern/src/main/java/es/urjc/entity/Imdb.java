package es.urjc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Imdb {

    private Double rating;
    private Integer votes;
    private Integer id;

}
