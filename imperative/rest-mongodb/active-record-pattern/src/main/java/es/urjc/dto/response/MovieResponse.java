package es.urjc.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieResponse {

    private String id;
    private String title;
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
    private String awards;
    private ImdbResponse imdb;

}
