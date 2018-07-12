package com.github.wyukawa.elasticsearch.unofficial.jdbc.driver;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class ElasticsearchResponse {
    private final List<ElasticsearchColumn> columns;
    private final List<List<String>> rows;

    public ElasticsearchResponse(@JsonProperty("columns") List<ElasticsearchColumn> columns, @JsonProperty("rows") List<List<String>> rows) {
        this.columns = columns;
        this.rows = rows;
    }

    public List<ElasticsearchColumn> getColumns() {
        return columns;
    }

    public List<List<String>> getRows() {
        return rows;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElasticsearchResponse that = (ElasticsearchResponse) o;
        return Objects.equals(columns, that.columns) &&
                Objects.equals(rows, that.rows);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columns, rows);
    }

    @Override
    public String toString() {
        return "ElasticsearchResponse{" +
                "columns=" + columns +
                ", rows=" + rows +
                '}';
    }

    public static class ElasticsearchColumn {
        private final String name;
        private final String type;

        public ElasticsearchColumn(@JsonProperty("name") String name, @JsonProperty("type") String type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ElasticsearchColumn uesColumn = (ElasticsearchColumn) o;
            return Objects.equals(name, uesColumn.name) &&
                    Objects.equals(type, uesColumn.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, type);
        }

        @Override
        public String toString() {
            return "ElasticsearchColumn{" +
                    "name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }
    }
}
