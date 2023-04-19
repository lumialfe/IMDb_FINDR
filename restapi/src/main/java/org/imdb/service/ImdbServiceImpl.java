package org.imdb.service;

import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import org.imdb.model.Movie;
import org.imdb.repositories.ElasticsearchEngine;
import org.imdb.util.ImbdReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class ImdbServiceImpl implements ImdbService{
    private ImbdReader reader;
    private final ElasticsearchEngine elasticsearchEngine;

    private final int NUM_MOVIES = 50000;

    @Autowired
    public ImdbServiceImpl(ElasticsearchEngine elasticsearchEngine) {
        this.elasticsearchEngine = elasticsearchEngine;
    }

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
    @Async
    @Override
    public void uploadFiles(MultipartFile basics, MultipartFile akas,
                            MultipartFile ratings, MultipartFile crew,
                            MultipartFile participants) throws IOException {
        try{
            if(basics.isEmpty() || akas.isEmpty() || ratings.isEmpty() ||
                    crew.isEmpty() || participants.isEmpty() ){
                throw new IOException("File is empty");
            }

            createIndex();

            List<Movie> movies = new ArrayList<>();
            reader = new ImbdReader(basics, akas, ratings, crew, participants);
            int counter = 0;

            while(reader.lines) {
                reader.getLines();
                Movie movie = reader.getMovie();

                if (movie != null) {
                    movies.add(movie);
                    counter++;
                }

                if(counter == NUM_MOVIES){
                    elasticsearchEngine.indexDocuments(movies);
                    counter = 0;
                    movies.clear();
                }
            }
            elasticsearchEngine.indexDocuments(movies);

        }catch(IOException exception){
            throw exception;
        }
    }

    /**
     * Creates the index with its corresponding mapping
     *
     * @throws IOException
     */
    @Override
    public void createIndex() throws IOException {
        InputStream input = new ClassPathResource("static" +
                "/movieMapping.json").getInputStream();
        InputStream settings = new ClassPathResource("static"+
                "/custom_analyzer.json").getInputStream();
        elasticsearchEngine.createIndex(input, settings);
    }

    /**
     * Returns the documents indexed (default 10 documents)
     * @return
     */
    @Override
    public List<Movie> getDocuments() {
        return elasticsearchEngine.getDocuments();
    }

    /**
     * Deletes the index specified by its name
     * @param indexName Index name
     */
    @Override
    public void deleteIndex(String indexName) {
        elasticsearchEngine.deleteIndex(indexName);
    }

    /**
     * Returns the information about the index
     *
     * @return
     */
    @Override
    public GetIndexResponse getIndixes() {
        return elasticsearchEngine.getIndexes();
    }

}
