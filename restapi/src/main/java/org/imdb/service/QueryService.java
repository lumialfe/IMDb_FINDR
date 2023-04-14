package org.imdb.service;

import org.imdb.model.Movie;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public interface QueryService {
    List<Movie> getRangedMovies(int from, int size) throws IOException;

    List<Movie> getMoviesByTitle(String title, String type) throws IOException;

    List<Movie> getRecommended(int year, int size) throws IOException;

    List<Movie> getMoviesFiltered(int minYear, int maxYear, int maxRuntimeMin, int minRuntimeMin, double minAvgRating, double maxAvgRating, String type, String[] genres) throws IOException;

    List<Movie> getNotToWatchMovies() throws IOException;

    List<Movie> getAllTimesRecommended() throws IOException;

}
