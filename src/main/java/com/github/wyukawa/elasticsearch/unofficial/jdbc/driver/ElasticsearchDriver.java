package com.github.wyukawa.elasticsearch.unofficial.jdbc.driver;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Strings.nullToEmpty;
import static java.lang.Integer.parseInt;

public class ElasticsearchDriver implements Driver {

    private static final String DRIVER_NAME = "Elasticsearch JDBC Driver";

    private static final String DRIVER_VERSION;

    private static final int DRIVER_VERSION_MAJOR;

    private static final int DRIVER_VERSION_MINOR;

    static final String DRIVER_URL_START = "jdbc:es:";

    static {
        String version = nullToEmpty(ElasticsearchDriver.class.getPackage().getImplementationVersion());
        Matcher matcher = Pattern.compile("^(\\d+)\\.(\\d+)($|[.-])").matcher(version);
        if (!matcher.find()) {
            DRIVER_VERSION = "unknown";
            DRIVER_VERSION_MAJOR = 0;
            DRIVER_VERSION_MINOR = 0;
        } else {
            DRIVER_VERSION = version;
            DRIVER_VERSION_MAJOR = parseInt(matcher.group(1));
            DRIVER_VERSION_MINOR = parseInt(matcher.group(2));
        }

        try {
            DriverManager.registerDriver(new ElasticsearchDriver());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        return new ElasticsearchConnection(url, info);
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return url.startsWith(DRIVER_URL_START);
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return new DriverPropertyInfo[0];
    }

    @Override
    public int getMajorVersion() {
        return DRIVER_VERSION_MAJOR;
    }

    @Override
    public int getMinorVersion() {
        return DRIVER_VERSION_MINOR;
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException("getParentLogger");
    }
}
