package org.imdb.repositories;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import org.imdb.model.Movie;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

public interface ElasticsearchEngine {

    /**
     * Created the index specifying its mapping
     * @param input Mapping
     */
    void createIndex(InputStream input);

    /**
     * Index a list of movies into elasticsearch
     * @param movies
     */
    void indexDocuments(List<Movie> movies);

    /**
     * Returns the documents indexed (default 10 documents)
     * @return
     */
    List<Movie> getDocuments();

    /**
     * Deletes the index specified by its name
     * @param indexName Index name
     */
    void deleteIndex(String indexName);

    /**
     * Returns the information about the index
     *
     * @return
     */
    GetIndexResponse getIndexes();

    /**
     * Returns the list of movies found by the query
     *
     * @param size of the list
     * @param query that will search for the movies
     * @return List of movies
     * @throws IOException
     */
    List<Movie> getQueryResult(int size, Query query) throws IOException;

    /**
     * Returns the list of movies found aggregated by a field
     *
     * @param size of the list
     * @param query that will search for the movies
     * @param aggregations
     * @return List of movies
     * @throws IOException
     */
    List<Movie> getQueryResult(int size, Query query, HashMap<String,
            Aggregation> aggregations)
    throws IOException;
}
