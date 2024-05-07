package com.postgraduate.global.batch.repository;

import com.postgraduate.global.batch.cancel.CancelMentoring;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Component
@RequiredArgsConstructor
public class BatchRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final String UPDATE = "update mentoring set status = 'CANCEL' where mentoring_id = :mentoringId";

    public void updateAll(List<CancelMentoring> cancelMentorings) {
        jdbcTemplate.batchUpdate(UPDATE, generateParameterSource(cancelMentorings));
    }

    private SqlParameterSource[] generateParameterSource(List<CancelMentoring> cancelMentorings) {
        return cancelMentorings.stream()
                .map(cancelMentoring -> new MapSqlParameterSource(generateEntityParams(cancelMentoring)))
                .toArray(SqlParameterSource[]::new);
    }

    private Map<String, Object> generateEntityParams(CancelMentoring cancelMentoring) {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("mentoringId", cancelMentoring.mentoringId());
        return parameter;
    }
}
