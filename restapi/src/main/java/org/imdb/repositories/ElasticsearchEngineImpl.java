package org.imdb.repositories;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.analysis.Analyzer;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import co.elastic.clients.elasticsearch.indices.IndexState;
import co.elastic.clients.json.DelegatingDeserializer;
import co.elastic.clients.json.ObjectDeserializer;
import org.imdb.configuration.ElasticSearchConfig;
import org.imdb.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class ElasticsearchEngineImpl implements  ElasticsearchEngine{
    private final ElasticSearchConfig elasticSearchConfig;
    private static final String INDEX = "imdb";


    @Autowired
    public ElasticsearchEngineImpl(ElasticSearchConfig elasticSearchConfig) {
        this.elasticSearchConfig = elasticSearchConfig;
    }

    /**
     * Created the index specifying its mapping
     * @param input Mapping
     */
    @Override
    public void createIndex(InputStream input, InputStream settings) {
        try {

            elasticSearchConfig.getElasticClient().indices().create(i -> i.index(INDEX));
            elasticSearchConfig.getElasticClient().indices().close(c -> c.index(INDEX));
            ObjectDeserializer unwrapped = (ObjectDeserializer) DelegatingDeserializer.unwrap(Analyzer._DESERIALIZER );
            unwrapped.setTypeProperty("type", "standard");
            elasticSearchConfig.getElasticClient().indices().putSettings(s -> s.index(INDEX).withJson(settings));
            elasticSearchConfig.getElasticClient().indices().open(o -> o.index(INDEX));
            elasticSearchConfig.getElasticClient().indices().putMapping(m -> m.index(INDEX).withJson(input));

        }catch(IOException e){
            System.out.println("error");
        }
    }

    private int counter = 0;

    /**
     * Index a list of movies into elasticsearch
     * @param movies
     */
    @Override
    public void indexDocuments(List<Movie> movies){
        if(!movies.isEmpty()){
            try {
                BulkRequest.Builder br = new BulkRequest.Builder();
                for (Movie m : movies) {
                    br.operations(op -> op
                            .index(idx -> idx
                                    .index(INDEX)
                                    .id(m.getTconst())
                                    .document(m)
                            )
                    );
                    counter++;
                }
                BulkResponse result = elasticSearchConfig.getElasticClient()
                        .bulk(br.build());
                if(result.errors()){
                    System.out.println("Bulk has errors");
                }
                System.out.println(counter);
            }catch(IOException e){
                System.out.println("Couldn't index");
            }
        }
    }

    /**
     * Returns the documents indexed (default 10 documents)
     * @return
     */
    @Override
    public List<Movie> getDocuments() {
        SearchRequest searchRequest =  SearchRequest.of(s -> s.index
                (INDEX));

        List<Movie> movies = new ArrayList<>();
        try {
            SearchResponse searchResponse = elasticSearchConfig
                    .getElasticClient().search(searchRequest,
                    Movie.class);
            List<Hit> hits = searchResponse.hits().hits();

            for (Hit object : hits) {
                System.out.print(((Movie) object.source()));
                movies.add((Movie) object.source());
            }
            return movies;
        }catch(IOException e){
            System.out.println("Couldn't be found");
        }
        return movies;
    }

    /**
     * Deletes the index specified by its name
     * @param indexName Index name
     */
    @Override
    public void deleteIndex(String indexName) {
        try{

            DeleteIndexResponse deleteIndexResponse =
                    elasticSearchConfig.getElasticClient().indices().delete(c ->
                            c.index(indexName));

            if(deleteIndexResponse.acknowledged()){
                System.out.println(("deleted"));
            }else{
                System.out.println("not deleted");
            }
        }catch(IOException e){
            System.out.println("cannot be deleted");
        }
    }

    /**
     * Returns the information about the index
     *
     * @return
     */
    @Override
    public GetIndexResponse getIndexes() {
        try {
            GetIndexResponse request =
                    elasticSearchConfig.getElasticClient().indices().get(c ->
                            c.index(INDEX));
            IndexState is = request.get(INDEX);
            return request;
        }catch(IOException e){
            System.out.println("There is no index");
        }
        return null;
    }

    /**
     * Returns the list of movies found by the query
     *
     * @param size of the list
     * @param query that will search for the movies
     * @return List of movies
     * @throws IOException
     */
    @Override
    public List<Movie> getQueryResult(int size, Query query) throws IOException {
       SortOptions sort1 = new SortOptions.Builder().field(f -> f.field(
               "numVotes").order(SortOrder.Desc)).build();
       SortOptions sort2 = new SortOptions.Builder().field(f -> f.field(
               "avgRating").order(SortOrder.Desc)).build();

        SearchResponse searchResponse =
                elasticSearchConfig.getElasticClient().search(i -> i
                                .index(INDEX)
                                .query(query)
                                .size(size)
                                .sort(sort1)
                                .sort(sort2)
                                ,
                        Movie.class);

        List<Hit<Movie>> hits = searchResponse.hits().hits();
        List<Movie> movies = new ArrayList<>();

        for(Hit<Movie> hit : hits){
            movies.add(hit.source());
        }
        return movies;
    }

    /**
     * Returns the list of movies found aggregated by a field
     *
     * @param size of the list
     * @param query that will search for the movies
     * @param aggregations
     * @return List of movies
     * @throws IOException
     */
    @Override
    public List<Movie> getQueryResult(int size, Query query, HashMap<String,
            Aggregation> aggregations)
    throws IOException {


        SearchResponse searchResponse =
                elasticSearchConfig.getElasticClient().search(i -> i
                                .index(INDEX)
                                .query(query)
                                .size(size)
                                .aggregations("genres", aggregations.get(
                                        "genres")),
                        Movie.class);

        List<Hit<Movie>> hits = searchResponse.hits().hits();
        List<Movie> movies = new ArrayList<>();

        for(Hit<Movie> hit : hits){
            movies.add(hit.source());
        }
        return movies;
    }
}
