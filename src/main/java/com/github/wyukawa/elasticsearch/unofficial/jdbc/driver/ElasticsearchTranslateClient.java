package com.github.wyukawa.elasticsearch.unofficial.jdbc.driver;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.sql.SQLException;

public class ElasticsearchTranslateClient {

    public static final MediaType JSON
            = MediaType.parse("application/json");

    private final String httpUrl;

    private final OkHttpClient client;

    private final ObjectMapper objectMapper;

    // https://www.elastic.co/guide/en/elasticsearch/reference/6.3/sql-translate.html
    public ElasticsearchTranslateClient(String elasticsearchHttpUrl) {
        this.httpUrl = elasticsearchHttpUrl + "/_xpack/sql/translate";
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        this.client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public String translate(String sql) throws SQLException {
        ElasticsearchRequest elasticsearchRequest = new ElasticsearchRequest(sql);
        try {
            byte[] requestBody = objectMapper.writeValueAsBytes(elasticsearchRequest);
            Request request = new Request.Builder()
                    .url(httpUrl)
                    .header("Content-Type", "application/json")
                    .post(RequestBody.create(JSON, requestBody))
                    .build();
            Response response = this.client.newCall(request).execute();
            if (response.code() != 200) {
                throw new SQLException(String.format("ElasticSearch returns code=%d and message=%s", response.code(), response.message()));
            }
            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new SQLException("ElasticSearch returns null body");
            }
            return responseBody.string();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }
}
