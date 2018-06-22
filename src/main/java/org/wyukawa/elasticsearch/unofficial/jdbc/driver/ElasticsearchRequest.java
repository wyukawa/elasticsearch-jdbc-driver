package org.wyukawa.elasticsearch.unofficial.jdbc.driver;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ElasticsearchRequest {
    private final String query;

    public ElasticsearchRequest(@JsonProperty("query") String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
