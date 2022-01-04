package es.urjc.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieRequest {

    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message = "Plot is required")
    private String plot;
    private String fullPlot;
    private String genres;
    private Integer runtime;
    private Integer year;
    private String type;
    private LocalDate released;
    private String directors;
    private String rated;
    private String cast;
    private String countries;
    private AwardsRequest awards;
    private ImdbRequest imdb;

}
