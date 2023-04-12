package org.imdb.repositories;

import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import co.elastic.clients.elasticsearch.indices.IndexState;
import org.imdb.configuration.ElasticSearchConfig;
import org.imdb.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class ElasticsearchEngineImpl implements  ElasticsearchEngine{
    private final ElasticSearchConfig elasticSearchConfig;
    private static final String INDEX = "imdb";


    @Autowired
    public ElasticsearchEngineImpl(ElasticSearchConfig elasticSearchConfig) {
        this.elasticSearchConfig = elasticSearchConfig;
    }

    @Override
    public void createIndex(InputStream input) {
        try {
            CreateIndexResponse cir =
                    elasticSearchConfig.getElasticClient().indices().create(b
                            -> b.index(
                    INDEX).withJson(input));
            if(cir.acknowledged()){
                System.out.println("indexed");
            }
        }catch(IOException e){
            System.out.println("error");
        }
    }

    private int counter = 0;

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
            System.out.println("error");
        }
        return movies;
    }

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

    @Override
    public List<Movie> getRangedMovies(int from, int size){
        return new QueryEngineImpl(elasticSearchConfig.getElasticClient())
                .getRangedMovies(from, size);
    }

    @Override
    public List<Movie> getMoviesByTitle(String title, String type){
        return new QueryEngineImpl(elasticSearchConfig.getElasticClient())
                .getMoviesByTitle(title, type);
    }

    @Override
    public List<Movie> getRecommended(int year, int size){
        return new QueryEngineImpl(elasticSearchConfig.getElasticClient())
                .getRecommended(year, size);
    }

    @Override
    public List<Movie> getMoviesFiltered(int minYear,
                                         int maxYear, int maxRuntimeMin,
                                         int minRuntimeMin, double minAvgRating,
                                         double maxAvgRating, String[] type,
                                         String[] genres) {
        return new QueryEngineImpl(elasticSearchConfig.getElasticClient())
                .getMoviesFiltered(minYear,
                        maxYear, maxRuntimeMin, minRuntimeMin, minAvgRating,
                        maxAvgRating, type, genres);
    }
}
