package com.postgraduate.batch.done;

import com.postgraduate.domain.mentoring.domain.entity.constant.TermUnit;
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
public class DoneMentoringRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final int CHARGE = TermUnit.SHORT.getCharge();
    private static final String UPDATE_MENTORING = "update mentoring set status = 'DONE', salary_salary_id = :salaryId where mentoring_id = :mentoringId";
    private static final String UPDATE_SALARY = "update salary set total_amount = total_amount + :amount where salary_id = :salaryId";

    public void updateAllSalary(List<DoneMentoring> mentorings) {
        jdbcTemplate.batchUpdate(UPDATE_SALARY, generateParameterSource(mentorings));
    }

    public void updateAllMentoring(List<DoneMentoring> mentorings) {
        jdbcTemplate.batchUpdate(UPDATE_MENTORING, generateParameterSource(mentorings));
    }

    private SqlParameterSource[] generateParameterSource(List<DoneMentoring> mentorings) {
        return mentorings.stream()
                .map(mentoring -> new MapSqlParameterSource(generateEntityParams(mentoring)))
                .toArray(SqlParameterSource[]::new);
    }

    private Map<String, Object> generateEntityParams(DoneMentoring doneMentoring) {
        HashMap<String, Object> parameter = new HashMap<>();
        parameter.put("mentoringId", doneMentoring.mentoringId());
        parameter.put("amount", doneMentoring.pay() - CHARGE);
        parameter.put("salaryId", doneMentoring.salaryId());
        return parameter;
    }
}
