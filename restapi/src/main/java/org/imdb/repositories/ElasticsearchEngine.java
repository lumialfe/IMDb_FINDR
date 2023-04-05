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

    List<Movie> getMoviesByTitle(String title);

    List<Movie> getRecommended(int year, int size);
}
