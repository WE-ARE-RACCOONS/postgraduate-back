package com.postgraduate.global.batch;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.List;
import java.util.Map;

public abstract class BatchRepository<T> {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final String insertSql;
    private final String updateSql;

    protected BatchRepository(NamedParameterJdbcTemplate jdbcTemplate, String insertSql, String updateSql) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertSql = insertSql;
        this.updateSql = updateSql;
    }

    public void insertAll(List<T> entities) {
        jdbcTemplate.batchUpdate(insertSql, generateParameterSource(entities));
    }

    public void updateAll(List<T> entities) {
        jdbcTemplate.batchUpdate(updateSql, generateParameterSource(entities));
    }

    private SqlParameterSource[] generateParameterSource(List<T> entities) {
        return entities.stream()
                .map(entity -> new MapSqlParameterSource(generateEntityParams(entity)))
                .toArray(SqlParameterSource[]::new);
    }

    protected abstract Map<String, Object> generateEntityParams(T entity);
}
