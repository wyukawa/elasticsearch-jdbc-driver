package com.github.wyukawa.elasticsearch.unofficial.jdbc.driver;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;

public class ElasticsearchPreparedStatementTest {
    private static final MediaType APPLICATION_JSON = MediaType.parse("application/json");
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();

    private ElasticsearchContainer container;
    private HttpUrl baseUrl;

    @Before
    public void setUp() throws Exception {
        container = new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:6.4.1");
        container.start();

        baseUrl = HttpUrl.parse("http://" + container.getHttpHostAddress());

        deleteIndex("library");

        JSONObject content = new JSONObject()
                .put("age", 23)
                .put("message", "trying out Elasticsearch")
                .put("user", "kimchy");
        insertDocument("library", content);
    }

    @After
    public void tearDown() throws Exception {
        container.stop();
    }

    @Test
    public void testExecuteQuery() throws Exception {
        try (Connection connection = DriverManager.getConnection("jdbc:es://" + container.getHttpHostAddress());
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM library");
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                assertEquals(23, resultSet.getLong(1));
                assertEquals("trying out Elasticsearch", resultSet.getString(2));
                assertEquals("kimchy", resultSet.getString(3));
            }
        }
    }

    @Test
    public void testTranslate() throws Exception {
        ElasticsearchTranslateClient translateClient = new ElasticsearchTranslateClient("http://" + container.getHttpHostAddress());
        //language=JSON
        String expected = "{"
                          + "\"size\":1000,"
                          + "\"query\":{\"range\":{\"age\":{\"from\":null,\"to\":30,\"include_lower\":false,\"include_upper\":false,\"boost\":1.0}}},"
                          + "\"_source\":{\"includes\":[\"message\",\"user\"],\"excludes\":[]},"
                          + "\"docvalue_fields\":[{\"field\":\"age\",\"format\":\"use_field_mapping\"}],"
                          + "\"sort\":[{\"_doc\":{\"order\":\"asc\"}}]"
                          + "}";
        assertEquals(expected, translateClient.translate("SELECT * FROM library WHERE age < 30"));
    }

    private void deleteIndex(String indexName) throws IOException {
        HttpUrl url = baseUrl.newBuilder()
                             .addPathSegment(indexName)
                             .build();

        Builder builder = new Builder().url(url).delete();
        HTTP_CLIENT.newCall(builder.build()).execute();
    }

    private void insertDocument(String indexName, JSONObject content) throws IOException {
        HttpUrl url = baseUrl.newBuilder()
                             .addPathSegments(indexName)
                             .addPathSegment("_doc")
                             .build();

        RequestBody body = RequestBody.create(APPLICATION_JSON, content.toString());
        Builder builder = new Builder().url(url).post(body);
        HTTP_CLIENT.newCall(builder.build()).execute();
    }
}
