# elasticsearch-jdbc-driver

unofficial elasticsearch jdbc driver, based on elasticsearch 6.3+'s rest api.

develop based on https://github.com/tokuhirom/unofficial-elasticsearch-jdbc-driver

# Synopsis

    Connection connection = DriverManager.getConnection("jdbc:es:localhost:9200");
    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM library WHERE release_date < '2000-01-01' order by name");
    ResultSet resultSet = preparedStatement.executeQuery();
    while (resultSet.next()) {
        System.out.println(resultSet.getString("name"));
    }
