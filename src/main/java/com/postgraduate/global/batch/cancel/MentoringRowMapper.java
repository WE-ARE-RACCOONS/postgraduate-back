package com.postgraduate.global.batch.cancel;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MentoringRowMapper implements RowMapper<Mentoring> {
    @Override
    public Mentoring mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Mentoring.builder()
                .mentoringId(rs.getLong("mentoring_id"))
                .build();

//        return new CancelMentoring(
//                rs.getLong("mentoring_id"),
//                rs.getLong("user_user_id"),
//                rs.getLong("senior_senior_id"),
//                rs.getLong("payment_payment_id")
//        );
    }
}
