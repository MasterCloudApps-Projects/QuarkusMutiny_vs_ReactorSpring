package es.urjc.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class AwardsRequest {

    private Integer wins;
    private Integer nominations;

    @JsonIgnore
    public String getText() {
        return wins + " win. " + nominations + " nominations";
    }

}
