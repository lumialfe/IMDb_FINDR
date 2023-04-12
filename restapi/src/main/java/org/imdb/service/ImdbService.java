package org.imdb.service;

import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import org.imdb.model.Movie;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface ImdbService {
    void uploadFiles(MultipartFile basics, MultipartFile akas,
                     MultipartFile ratings, MultipartFile crew,
                     MultipartFile participants) throws IOException;

    void createIndex(InputStream input);

    void indexDocument(Movie movie);


    List<Movie> getDocuments();

    void deleteIndex(String indexName);


    GetIndexResponse getIndixes();

    List<Movie> getRangedMovies(int from, int size);

    List<Movie> getMoviesByTitle(String title, String type);

    List<Movie> getRecommended(int year, int size);

    List<Movie> getMoviesFiltered(int minYear,
                                  int maxYear, int maxRuntimeMin,
                                  int minRuntimeMin, double minAvgRating,
                                  double maxAvgRating, String[] type,
                                  String[] genres);
}
