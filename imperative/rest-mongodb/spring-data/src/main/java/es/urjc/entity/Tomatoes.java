package es.urjc.entity;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Tomatoes {

    private Viewer viewer;
    private LocalDate lastUpdated;

}
