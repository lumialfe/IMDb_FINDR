package org.imdb.service;

import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;
import org.imdb.model.Movie;
import org.imdb.repositories.ElasticsearchEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class QueryServiceImpl implements QueryService{

    private final ElasticsearchEngine elasticsearchEngine;
    private QueryProvider queryProvider = new QueryProvider();

    private static final String NOT_MATCH_MOVIES = " tvEpisode, video, " +
            "videoGame, tvPilot";
    private static final String MOVIES = "short, movie, tvMovie, tvShort";
    private static final String EPISODE = "tvSeries, tvMiniSeries, tvSpecial";

    @Autowired
    public QueryServiceImpl(ElasticsearchEngine elasticsearchEngine) {
        this.elasticsearchEngine = elasticsearchEngine;
    }


    @Override
    public List<Movie> getRangedMovies(int from, int size) throws IOException {
        return elasticsearchEngine.getQueryResult(size, queryProvider.getRangedQuery(
                "startYear", from));
    }

    @Override
    public List<Movie> getMoviesByTitle(String title, String type) throws IOException {
        List<Query> queries = new ArrayList<>();
        //queries.add(queryProvider.getMultiMatchQuery("primaryTitle",
         //       "originalTitle", title));
        queries.add(queryProvider.getTitle(title));

        Query typeQ = checkType(type);
        if(typeQ != null){
            queries.add(typeQ);
        }
        queries.add(queryProvider.getRangedQuery("avgRating", 3.0));
        queries.add(queryProvider.getMinNumOfVotes(50000));

        Query query =
                BoolQuery.of(q -> q.must(queries).mustNot(queryProvider.getMatchQuery(
                        "titleType", NOT_MATCH_MOVIES)))._toQuery();

        return elasticsearchEngine.getQueryResult(40, query);
    }

    private Query checkType(String type) {
        switch(type){
            case "MOVIE":
                return queryProvider.getMatchQuery("titleType", MOVIES);
            case "EPISODE":
                return queryProvider.getMatchQuery("titleType", EPISODE);
        }
        return null;
    }

    @Override
    public List<Movie> getRecommended(int year, int size) throws IOException {
        List<Query> queries = new ArrayList<>();
        queries.add(queryProvider.getMatchQuery("titleType", MOVIES));
        queries.add(queryProvider.getMultiMatchQuery("startYear", "endYear",
                String.valueOf(year)));
        queries.add(queryProvider.getMinNumOfVotes(50000));

        Query query =
                BoolQuery.of(q -> q.filter(queries))._toQuery();
        return elasticsearchEngine.getQueryResult(size, query);
    }

    @Override
    public List<Movie> getMoviesFiltered(int minYear, int maxYear, int
            maxRuntimeMin, int minRuntimeMin, double minAvgRating,
                                         double maxAvgRating, String type,
                                         String[] genres) throws IOException {
        List<Query> queries = new ArrayList<>();

        if(minYear > 0 && maxYear > 0 && maxYear > minYear){
            queries.add(queryProvider.getRangedQueryDoubleValue("startYear",
                    minYear, maxYear));
        }

        if(minRuntimeMin > 0 && maxRuntimeMin > 0 && maxRuntimeMin > minRuntimeMin){
            queries.add(queryProvider.getRangedQueryDoubleValue("runtimeMinutes",
                    minRuntimeMin,
                    maxRuntimeMin));
        }
        if(minAvgRating >= 0 && maxAvgRating > 0 && maxAvgRating > minAvgRating){
            queries.add(queryProvider.getRangedQueryDoubleValue("avgRating", minAvgRating,
                    maxAvgRating));
        }

        Query typeQ = checkType(type);

        if(typeQ != null){
            queries.add(typeQ);
        }

        if (genres.length > 0) {
            queries.add(queryProvider.getTermQuery("genres", genres));

        }

        queries.add(queryProvider.getMinNumOfVotes(200000));

        Query query =
                BoolQuery.of(q -> q.filter(queries))._toQuery();

        return elasticsearchEngine.getQueryResult(20, query);
    }

    @Override
    public List<Movie> getNotToWatchMovies() throws IOException {
        Query query =
                BoolQuery.of(b -> b.must(queryProvider.getMatchQuery("starrings.name" +
                                ".nconst", "nm0782213"),queryProvider.getMatchQuery(
                                        "titleType", "movie"))
                )._toQuery();
        return elasticsearchEngine.getQueryResult(25, query);
    }

    @Override
    public List<Movie> getAllTimesRecommended() throws IOException {
        Query query =
                BoolQuery.of(b -> b.must(queryProvider.getMatchQuery("titleType",
                                "movie"),
                        queryProvider.getMinNumOfVotes(1000000)))._toQuery();
        return elasticsearchEngine.getQueryResult(20, query);
    }

}
