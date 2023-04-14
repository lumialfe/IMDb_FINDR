package org.imdb.repositories;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import org.imdb.model.Movie;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface ElasticsearchEngine {
    void createIndex(InputStream input);

    void indexDocuments(List<Movie> movies);

    List<Movie> getDocuments();

    void deleteIndex(String indexName);

    GetIndexResponse getIndexes();

    List<Movie> getQueryResult(int size, Query query) throws IOException;
}
