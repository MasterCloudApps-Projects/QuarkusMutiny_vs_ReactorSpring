package es.urjc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "movies")
public class Movie {

    @Id
    private ObjectId id;
    private String title;
    private String plot;
    private String fullPlot;
    private List<String> genres;
    private Integer runtime;
    private Integer year;
    private String type;
    private LocalDate released;
    private List<String> directors;
    private String rated;
    private List<String> cast;
    private List<String> countries;
    private Integer numMflixComments;
    private LocalDate lastUpdated;

    @BsonProperty("awards")
    private Awards awards;

    @BsonProperty("imdb")
    private Imdb imdb;

    @BsonProperty("tomatoes")
    private Tomatoes tomatoes;

}
