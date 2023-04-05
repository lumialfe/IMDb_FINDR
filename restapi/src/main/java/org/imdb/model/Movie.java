package org.imdb.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Movie {
    private String tconst;
    private String titleType;
    private String primaryTitle;
    private String originalTitle;
    private boolean isAdult;
    private int startYear;
    private int endYear;
    private int runtimeMinutes;

    private String[] genres;
    double avgRating;
    int numVotes;
    List<Akas> akas;
    List<Directors> directors;
    List<Starring> starrings;


}
