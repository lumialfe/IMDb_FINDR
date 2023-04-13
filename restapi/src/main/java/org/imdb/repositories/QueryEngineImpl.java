package org.imdb.repositories;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import org.imdb.model.Movie;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Component
public class QueryEngineImpl {
    private ElasticsearchClient elasticsearchClient;
    private static final String INDEX = "imdb";
    private static final String NOT_MATCH_MOVIES = " tvEpisode, video, " +
            "videoGame, tvPilot";
    private static final String MOVIES = "short, movie, tvMovie, tvShort";
    private static final String EPISODE = "tvSeries, tvMiniSeries, tvSpecial";

    public QueryEngineImpl(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    public List<Movie> getMoviesByTitle(String title, String type) {
        List<Movie> movies = new ArrayList<>();

            List<Query> queries = new ArrayList<>();

            MultiMatchQuery multiMatchQuery = MultiMatchQuery.of(m -> m.fields(
                    "primaryTitle", "originalTitle").query(title));

            queries.add(multiMatchQuery._toQuery());

            Query queryType = filterByType(type);
            if(queryType != null){
                queries.add(queryType);
            }

            Query ratingQuery = RangeQuery.of(r -> r.field("avgRating")
                    .gte(JsonData.of(3.0)))._toQuery();

        Query votesQuery = getMinNumOfVotes(50000);

        queries.add(ratingQuery);
        queries.add(votesQuery);



            Query query =
                    BoolQuery.of(q -> q.must(queries).mustNot(getMustNotType()))._toQuery();




        try {
            movies = getQueryResult(40, query);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return movies;
    }

    private static Query getMinNumOfVotes(int votes) {
        Query votesQuery = RangeQuery.of(r -> r.field("numVotes")
                .gte(JsonData.of(votes)))._toQuery();
        return votesQuery;
    }

    private Query filterByType(String type) {
        switch (type){
            case "MOVIE":
                return MatchQuery.of(m -> m.field("titleType")
                        .query(MOVIES))._toQuery();

            case "EPISODE":
                return MatchQuery.of(m -> m.field("titleType")
                        .query(EPISODE))._toQuery();

        }
        return null;
    }

    private Query getMustNotType() {
        return MatchQuery.of(m -> m.field("titleType")
                .query(NOT_MATCH_MOVIES))._toQuery();
    }

    public List<Movie> getRangedMovies(int from, int size){
        List<Movie> movies = new ArrayList<>();
        try {
            Query query =
                    RangeQuery.of(r -> r.field("startYear").gte(
                            JsonData.of(from)))._toQuery();

            movies = getQueryResult(size, query);


        }catch(IOException e){
            System.out.println("error");
        }
        return movies;
    }

    public List<Movie> getRecommended(int year, int size){
        List<Movie> movies = new ArrayList<>();
        try {
            List<Query> queries = new ArrayList<>();

            Query titleTypeQuery = MatchQuery.of(m -> m.query(MOVIES)
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


            movies = getQueryResult(size, query);

        }catch(IOException e){
            System.out.println("error");
        }
        return movies;
    }

    private List<Movie> getQueryResult(int size, Query query) throws IOException {


        SearchResponse searchResponse =
                elasticsearchClient.search(i -> i
                                .index(INDEX)
                                .query(query)
                                .size(size),
                                Movie.class);

        List<Hit<Movie>> hits = searchResponse.hits().hits();
        List<Movie> movies = new ArrayList<>();

        for(Hit<Movie> hit : hits){
            movies.add(hit.source());
        }
        return movies;
    }

    public List<Movie> getMoviesFiltered(int minYear,
                                         int maxYear, int maxRuntimeMin,
                                         int minRuntimeMin, double minAvgRating,
                                         double maxAvgRating, String type,
                                         String[] genres) {
        List<Movie> movies = new ArrayList<>();
        List<Query> queries = new ArrayList<>();

        if(minYear != 0 && maxYear != 0){
            queries.add(getRangedYear("startYear", "endYear",minYear, maxYear));
        }

        if(minRuntimeMin > 0 && maxRuntimeMin != 0){
            queries.add(getRanged("runtimeMinutes", minRuntimeMin,
                    maxRuntimeMin));
        }
        if(minAvgRating >= 0 && maxAvgRating != 0){
            queries.add(getRanged("avgRating", minAvgRating,
                    maxAvgRating));
        }
        if(filterByType(type) != null){
            queries.add(filterByType(type));
        }
        if (genres != null) {
            queries.add(getTypeAndGenreMovie("genres",genres));

        }

        Query query =
                BoolQuery.of(q -> q.filter(queries))._toQuery();


        try{
            movies = getQueryResult(5, query);

        }catch(IOException e){

        }

        return movies;
    }


    private Query getTypeAndGenreMovie(String field, String[] type) {
        String query = "";
        for(int i = 0; i < type.length; i++){
            if(i != (type.length - 1)){
                query += type[i] + ", ";
            }else{
                query += type[i];
            }
        }
        String finalQ = query;
        return MatchQuery.of(m -> m.field(field).query(finalQ))._toQuery();

    }

    private Query getRangedYear(String field1, String field2, int value1,
                            int value2) {
        return RangeQuery.of(r -> r.field(field1)
                .gte(JsonData.of(value1)).field(field2).lte(
                        JsonData.of(value2)))._toQuery();
    }

    private Query getRanged(String field1,double value1,
                                double value2) {
        return RangeQuery.of(r -> r.field(field1)
                .gte(JsonData.of(value1)).lte(
                        JsonData.of(value2)))._toQuery();
    }

    public List<Movie> getNotToWatchMovies() {
        List<Movie> movies = new ArrayList<>();

        Query query =
                BoolQuery.of(b -> b.must(MatchQuery.of(m -> m.field(
                        "starrings.name.nconst").query("nm0782213"))._toQuery(),
                                MatchQuery.of(m -> m.field(
                                        "titleType").query("movie"))._toQuery())
                        )._toQuery();

        try {
            movies = getQueryResult(25, query);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return movies;
    }

    public List<Movie> getAllTimesRecommended(){
        List<Movie> movies = new ArrayList<>();
        Query query =
                BoolQuery.of(b -> b.must(MatchQuery.of(m -> m.field(
                        "titleType").query("movie"))._toQuery(),
                        getMinNumOfVotes(1000000)))._toQuery();

        try{
            movies = getQueryResult(60, query);
        }catch(IOException e){
            System.out.println("Something went wrong");
        }
        return movies;
    }
}
