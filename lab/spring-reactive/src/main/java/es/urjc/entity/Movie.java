package es.urjc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "movies")
public class Movie {

    @Id
    private Long id;
    private String poster;
    private String title;
    @Column("releasedYear")
    private Integer releasedYear;
    private String certificate;
    private String runtime;
    private String genre;
    private Double rating;
    private String overview;
    private String director;

}