package com.postgraduate.batch.cancel;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CancelMentoringRepository{
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final String AUTO_CANCEL = "자동취소";
    private static final String UPDATE_MENTORING = "update mentoring set status = 'CANCEL' where mentoring_id = :mentoringId";
    private static final String INSERT_REFUSE = "insert into refuse(" +
            "mentoring_mentoring_id, " +
            "reason" +
            ") values(" +
            ":mentoringId, :reason" +
            ")";

    public void insertAllRefuse(List<CancelMentoring> mentorings) {
        jdbcTemplate.batchUpdate(INSERT_REFUSE, generateParameterSource(mentorings));
    }

    public void updateAllMentoring(List<CancelMentoring> mentorings) {
        jdbcTemplate.batchUpdate(UPDATE_MENTORING, generateParameterSource(mentorings));
    }

    private SqlParameterSource[] generateParameterSource(List<CancelMentoring> mentorings) {
        return mentorings.stream()
                .map(mentoring -> new MapSqlParameterSource(generateEntityParams(mentoring)))
                .toArray(SqlParameterSource[]::new);
    }

    private Map<String, Object> generateEntityParams(CancelMentoring cancelMentoring) {
        HashMap<String, Object> parameter = new HashMap<>();
        parameter.put("mentoringId", cancelMentoring.mentoringId());
        parameter.put("reason", AUTO_CANCEL);
        return parameter;
    }
}
