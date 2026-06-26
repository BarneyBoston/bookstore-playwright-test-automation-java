package app.bookstore.db.utils;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class DBClient {

    private final ConnectionWrapper connection;
    private final ObjectMapper mapper;

    public DBClient(DBAccessDetail dbAccessDetail) {
        this.connection = new ConnectionWrapper(dbAccessDetail.getUser(), dbAccessDetail.getPassword(), dbAccessDetail.getConnString());
        mapper = configureMapper();
    }

    protected ObjectMapper configureMapper() {
        return JsonMapper.builder()
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .build();
    }

    public List<Record> getResultsForQuery(String sqlQuery) {
        return executeQueryToDataRowsList(sqlQuery);
    }

    public <T> List<T> getResultsForQuery(String query, Class<T> resultType) {
        return getResultsForQuery(query)
                .stream()
                .map(Record::getMap)
                .map(row -> mapper.convertValue(row, resultType))
                .collect(Collectors.toList());
    }

    public Record getResultForQuery(String query) {
        var results = getResultsForQuery(query);
        if (results.size() != 1) {
            throw new DatabaseException(String.format("Query returned %s rows, but should return 1, query: '%s'", results.size(), query));
        }

        return results.get(0);
    }

    public <T> T getResultForQuery(String query, Class<T> resultType) {
        var result = getResultForQuery(query);

        return mapper.convertValue(result.getMap(), resultType);
    }

    private synchronized List<Record> executeQueryToDataRowsList(String sqlQuery) {
        log.debug("execute: {}", sqlQuery);
        List<Record> rowsList = new ArrayList<>();
        try (var con = connection.openConnection()) {
            var resultSet = con.executeQuery(sqlQuery);
            ResultSetMetaData rsMetaData = resultSet.getMetaData();
            try {
                while (resultSet.next()) {
                    Record row = new Record();
                    for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
                        String col = rsMetaData.getColumnName(i);
                        row.set(col, resultSet.getString(col));
                    }
                    rowsList.add(row);
                }
            } catch (SQLException e) {
                log.error("Exception when iterating results resultSet: " + e);
                throw new DatabaseException("Exception when iterating over results result set", e);
            }
        } catch (SQLException e) {
            log.error("SQLTimeoutException when executing query: " + sqlQuery + "\n" + e);
            throw new DatabaseException("SQLTimeoutException when executing query: " + sqlQuery, e);
        }
        return rowsList;
    }
}
