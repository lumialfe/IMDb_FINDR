package org.imdb.service;

import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import org.imdb.model.Movie;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface ImdbService {

    /**
     * Uploads and process the corresponding files with the data sets of imdb
     *
     * @param basics
     * @param akas
     * @param ratings
     * @param crew
     * @param participants
     * @throws IOException
     */
    void uploadFiles(MultipartFile basics, MultipartFile akas,
                     MultipartFile ratings, MultipartFile crew,
                     MultipartFile participants) throws IOException;

    /**
     * Creates the index with its corresponding mapping
     *
     * @throws IOException
     */
    void createIndex() throws IOException;

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
    GetIndexResponse getIndixes();


}
