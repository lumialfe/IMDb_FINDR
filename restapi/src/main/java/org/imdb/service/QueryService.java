package org.imdb.service;

import org.imdb.model.Movie;

import java.io.IOException;
import java.util.List;

public interface QueryService {

    /**
     * Returns the movies in a range from the year specified
     *
     * @param from Year from it has to search
     * @param size Size of the result of list of movies
     * @return list of movies
     * @throws IOException
     */
    List<Movie> getRangedMovies(int from, int size) throws IOException;

    /**
     * Returns the movies that match with the title and the type specified.
     *
     * @param title
     * @param type
     * @return list of movies
     * @throws IOException
     */
    List<Movie> getMoviesByTitle(String title, String type) throws IOException;

    /**
     * Returns the recommended movies with a good score in the year
     * specified
     * @param year
     * @param size
     * @return list of movies
     * @throws IOException
     */
    List<Movie> getRecommended(int year, int size) throws IOException;

    /**
     * Returns the movies filtered by year, runtimeMin, avgRating, type and
     * genre
     * @param minYear
     * @param maxYear
     * @param maxRuntimeMin
     * @param minRuntimeMin
     * @param minAvgRating
     * @param maxAvgRating
     * @param type
     * @param genres
     * @return list of movies
     * @throws IOException
     */
    List<Movie> getMoviesFiltered(int minYear, int maxYear, int maxRuntimeMin, int minRuntimeMin, double minAvgRating, double maxAvgRating, String type, String[] genres) throws IOException;

    /**
     * Returns the NOT to watch movies
     * @return list of movies
     * @throws IOException
     */
    List<Movie> getNotToWatchMovies() throws IOException;

    /**
     * Returns the all times recommended movies
     * @return list of movies
     * @throws IOException
     */
    List<Movie> getAllTimesRecommended() throws IOException;

    /**
     * Returns the movies that contains the mustGenres and do not contain the
     * mustNotGenres
     *
     * @param mustGenres
     * @param mustNotGenres
     * @param excludedIds
     * @return List of movies
     * @throws IOException
     */
    List<Movie> getFilmsByGenres(String[] mustGenres, String[] mustNotGenres, String[] excludedIds) throws IOException;
}
