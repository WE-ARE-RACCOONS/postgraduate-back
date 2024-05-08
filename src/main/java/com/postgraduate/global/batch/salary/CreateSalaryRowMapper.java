package com.postgraduate.global.batch.salary;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CreateSalaryRowMapper implements RowMapper<CreateSalary> {
    @Override
    public CreateSalary mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new CreateSalary(
                rs.getLong("senior_id"),
                rs.getLong("account_id"),
                rs.getString("bank"),
                rs.getString("account_number"),
                rs.getString("account_holder")
        );
    }
}
