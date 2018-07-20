package com.github.wyukawa.elasticsearch.unofficial.jdbc.driver;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.Assert.assertNotNull;

public class ElasticsearchPreparedStatementTest {

    @Test
    public void executeQuery() throws Exception {
//        DriverManager.registerDriver(new ElasticsearchDriver());

        Connection connection = DriverManager.getConnection("jdbc:es:localhost:9200");
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM library WHERE release_date < '2000-01-01'");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            System.out.println(resultSet.getString(1));
            System.out.println(resultSet.getString(2));
            System.out.println(resultSet.getString(3));
            System.out.println(resultSet.getString(4));
            System.out.println(resultSet.getString(5));
        }
        assertNotNull(connection);
    }

    @Test
    public void translate() throws Exception {
        ElasticsearchTranslateClient translateClient = new ElasticsearchTranslateClient("http://localhost:9200");
        System.out.println(translateClient.translate("SELECT * FROM library WHERE release_date < '2000-01-01'"));
    }
}