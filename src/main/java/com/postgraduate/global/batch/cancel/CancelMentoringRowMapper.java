package com.postgraduate.global.batch.cancel;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CancelMentoringRowMapper implements RowMapper<CancelMentoring> {
    @Override
    public CancelMentoring mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new CancelMentoring(
                rs.getLong("mentoring_id"),
                rs.getLong("user_user_id"),
                rs.getLong("senior_senior_id"),
                rs.getLong("payment_payment_id")
        );
    }
}
