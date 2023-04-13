package org.imdb.repositories;

import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import org.imdb.model.Movie;


import java.io.InputStream;
import java.util.List;

public interface ElasticsearchEngine {
    void createIndex(InputStream input);

    void indexDocuments(List<Movie> movies);

    List<Movie> getDocuments();

    void deleteIndex(String indexName);

    GetIndexResponse getIndexes();

    List<Movie> getRangedMovies(int from, int size);

    List<Movie> getMoviesByTitle(String title, String type);

    List<Movie> getRecommended(int year, int size);

    List<Movie> getMoviesFiltered(int minYear,
                                  int maxYear, int maxRuntimeMin,
                                  int minRuntimeMin, double minAvgRating,
                                  double maxAvgRating, String type,
                                  String[] genres);

    List<Movie> getNotToWatchMovies();

    List<Movie> getAllTimesRecommended();
}
