package es.urjc.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImdbResponse {

    private Double rating;
    private Integer votes;

}
