package com.postgraduate.global.batch.done;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DoneMentoringRowMapper implements RowMapper<DoneMentoring> {
    @Override
    public DoneMentoring mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new DoneMentoring(
                rs.getLong("mentoring_id"),
                rs.getLong("senior_senior_id"),
                rs.getLong("salary_salary_id"),
                rs.getString("date"),
                rs.getInt("pay")
        );
    }
}
