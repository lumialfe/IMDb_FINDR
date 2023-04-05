package org.imdb.repositories;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import org.imdb.model.Movie;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Component
public class QueryEngineImpl {
    private ElasticsearchClient elasticsearchClient;
    private static final String INDEX = "imdb";

    public QueryEngineImpl(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    public List<Movie> getMoviesByTitle(String title) {
        List<Movie> movies = new ArrayList<>();
        try {
            MultiMatchQuery multiMatchQuery = MultiMatchQuery.of(m -> m.fields(
                    "primaryTitle", "originalTitle").query(title));
            Query query = multiMatchQuery._toQuery();

            SearchResponse searchResponse =
                    elasticsearchClient.search(i -> i
                                    .index(INDEX)
                                    .query(query),
                            Movie.class);
            List<Hit<Movie>> hits = searchResponse.hits().hits();
            for(Hit<Movie> movie : hits){
                movies.add(movie.source());
            }
            return movies;
        }catch(IOException e){
            System.out.println("error");
        }
        return movies;
    }

    public List<Movie> getRangedMovies(int from, int size){
        List<Movie> movies = new ArrayList<>();
        try {
            SearchResponse searchResponse =
                    elasticsearchClient.search(i -> i
                                    .index(INDEX)
                                    .from(from)
                                    .size(size),
                            Movie.class);
            List<Hit<Movie>> hits = searchResponse.hits().hits();

            for (Hit<Movie> object : hits) {
                movies.add(object.source());
            }
            return movies;
        }catch(IOException e){
            System.out.println("error");
        }
        return movies;
    }

    public List<Movie> getRecommended(int year, int size){
        List<Movie> movies = new ArrayList<>();
        try {
            SortOptions sort = new SortOptions.Builder().field(f -> f.field(
                    "avgRating").order(SortOrder.Desc)).build();

            List<Query> queries = new ArrayList<>();

            Query titleTypeQuery = MatchQuery.of(m -> m.query("movie")
                    .field("titleType"))._toQuery();
            Query yearQuery = MultiMatchQuery.of(m -> m.query(String.valueOf(year))
                    .fields("startYear", "endYear"))._toQuery();
            Query votesQuery = RangeQuery.of(r -> r.field("numVotes")
                    .gte(JsonData.of(50000)))._toQuery();

            queries.add(titleTypeQuery);
            queries.add(yearQuery);
            queries.add(votesQuery);

            Query query =
                    BoolQuery.of(q -> q.filter(queries))._toQuery();


            List<Hit<Movie>> hits = getQueryResult(size, sort, query);

            for (Hit<Movie> object : hits) {
                movies.add(object.source());
            }
            return movies;
        }catch(IOException e){
            System.out.println("error");
        }
        return movies;
    }

    private List<Hit<Movie>> getQueryResult(int size, SortOptions sort, Query query) throws IOException {
        SearchResponse searchResponse =
                elasticsearchClient.search(i -> i
                                .index(INDEX)
                                .query(query)
                                .size(size)
                                .sort(sort),
                                Movie.class);

        List<Hit<Movie>> hits = searchResponse.hits().hits();
        return hits;
    }
}
