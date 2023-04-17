package org.imdb.service;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class QueryProvider {

    public QueryProvider() {
    }

    public Query getRangedQueryDoubleValue(String field,
                                           double value1, double value2) {
        return RangeQuery.of(r -> r.field(field)
                .gte(JsonData.of(value1)).lte(
                        JsonData.of(value2)))._toQuery();
    }

    public Query getRangedQueryDouble(String field1, String field2,
                                       int value1, int value2) {
        return RangeQuery.of(r -> r.field(field1)
                .gte(JsonData.of(value1)).field(field2).lte(
                        JsonData.of(value2)))._toQuery();
    }

    public Query getRangedQuery(String field, double value){
        return RangeQuery.of(r -> r.field(field).gte(JsonData.of(value)))
                ._toQuery();
    }

    public Query getMultiMatchQuery(String field1, String field2,
                                     String query){
        return MultiMatchQuery.of(m -> m.fields(field1, field2).query(query))
                ._toQuery();
    }

    public Query getMatchQuery(String field, String query){
        return MatchQuery.of(m -> m.field(field).query(query))._toQuery();
    }

    public Query getMinNumOfVotes(int votes) {
        Query votesQuery = RangeQuery.of(r -> r.field("numVotes")
                .gte(JsonData.of(votes)))._toQuery();
        return votesQuery;
    }

    public Query getTitle(String title){
        return MatchPhrasePrefixQuery.of(m -> m.field("primaryTitle").field(
                        "originalTitle")
                .query(title))._toQuery();
    }

    public HashMap<String, Aggregation> getAggregations(){
        HashMap<String, Aggregation> result = new HashMap<>();
        result.put("genres",
                Aggregation.of(a -> a.terms(t -> t.field("genres"))));
        return result;
    }

    public Query getTermQuery(String field, String[] values) {
        TermsQueryField terms = new TermsQueryField.Builder()
                .value(Arrays.asList(values).stream().map(FieldValue::of).toList())
                .build();
        return TermsQuery.of(t -> t.field(field).terms(terms)
        )._toQuery();
    }
}
