package com.postgraduate.batch.salary;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.postgraduate.domain.senior.salary.util.SalaryUtil.getSalaryDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateSalaryRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final String INSERT_SALARY = "insert into salary " +
            "(salary_date, status, total_amount, senior_senior_id, account_holder, account_number, bank) " +
            "values (:salaryDate, false, 0, :seniorId, :accountHolder, :accountNumber, :bank)";


    public void insertAllSalary(List<CreateSalary> createSalaries) {
        jdbcTemplate.batchUpdate(INSERT_SALARY, generateParameterSource(createSalaries));
    }

    private SqlParameterSource[] generateParameterSource(List<CreateSalary> createSalaries) {
        return createSalaries.stream()
                .map(createSalary -> new MapSqlParameterSource(generateEntityParams(createSalary)))
                .toArray(SqlParameterSource[]::new);
    }

    private Map<String, Object> generateEntityParams(CreateSalary createSalary) {
        log.info("create salary for seniorId : {}", createSalary.seniorId());
        HashMap<String, Object> parameter = new HashMap<>();
        parameter.put("salaryDate", getSalaryDate().plusDays(7));
        parameter.put("seniorId", createSalary.seniorId());
        parameter.put("accountHolder", createSalary.accountHolder());
        parameter.put("accountNumber", createSalary.accountNumber());
        parameter.put("bank", createSalary.bank());
        return parameter;
    }
}
