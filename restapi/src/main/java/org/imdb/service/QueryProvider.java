package org.imdb.service;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;

import java.util.Arrays;
import java.util.HashMap;

public class QueryProvider {

    public QueryProvider() {
    }

    /**
     * Performs a range query that will search within a range of values of a
     * field
     * @param field
     * @param value1
     * @param value2
     * @return Query
     */
    public Query getRangedQuery(String field,
                                double value1, double value2) {
        return RangeQuery.of(r -> r.field(field)
                .gte(JsonData.of(value1)).lte(
                        JsonData.of(value2)))._toQuery();
    }

    /**
     * Performs a range query that will search within a range of a value of a
     * field
     * @param field
     * @param value
     * @return Query
     */
    public Query getRangedQuery(String field, double value){
        return RangeQuery.of(r -> r.field(field).gte(JsonData.of(value)))
                ._toQuery();
    }

    /**
     * Performs a multi match query that will search in two fields for the
     * same value
     * @param field1
     * @param field2
     * @param query
     * @return Query
     */
    public Query getMultiMatchQuery(String field1, String field2,
                                     String query){
        return MultiMatchQuery.of(m -> m.fields(field1, field2).query(query))
                ._toQuery();
    }

    /**
     * Performs a match query that will search in a specific field for a value
     * @param field
     * @param query
     * @return Query
     */
    public Query getMatchQuery(String field, String query){
        return MatchQuery.of(m -> m.field(field).query(query))._toQuery();
    }

    /**
     * Performs a range query that will search for minimum votes of a movie
     * @param votes
     * @return Query
     */
    public Query getMinNumOfVotes(int votes) {
        Query votesQuery = RangeQuery.of(r -> r.field("numVotes")
                .gte(JsonData.of(votes)))._toQuery();
        return votesQuery;
    }

    /**
     * Performs a match phrase prefix query that will search for the
     * specified title of a movie
     * @param title
     * @return Query
     */
    public Query getTitle(String title){
        return MatchPhrasePrefixQuery.of(m -> m.field("primaryTitle").field(
                        "originalTitle")
                .query(title))._toQuery();
    }

    /**
     * Performs the aggregation of genres
     * @return HashMap
     */
    public HashMap<String, Aggregation> getAggregations(){
        HashMap<String, Aggregation> result = new HashMap<>();
        result.put("genres",
                Aggregation.of(a -> a.terms(t -> t.field("genres"))));
        return result;
    }

    /**
     * Performs a term query that will search in a field for the specified
     * values
     * @param field
     * @param values
     * @return Query
     */
    public Query getTermQuery(String field, String[] values) {
        TermsQueryField terms = new TermsQueryField.Builder()
                .value(Arrays.asList(values).stream().map(FieldValue::of).toList())
                .build();

        return TermsQuery.of(t -> t.field(field).terms(terms)
        )._toQuery();
    }
}
