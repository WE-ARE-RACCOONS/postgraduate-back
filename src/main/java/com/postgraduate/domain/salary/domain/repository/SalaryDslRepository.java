package com.postgraduate.domain.salary.domain.repository;

import com.postgraduate.domain.salary.application.dto.SalaryDetails;
import com.postgraduate.domain.salary.application.dto.SeniorSalary;
import com.postgraduate.domain.senior.domain.entity.Senior;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface SalaryDslRepository {
    Page<SeniorSalary> findDistinctBySearchSenior(String search, Pageable pageable);

//    List<SalaryDetails> findAllDetailBySenior(Senior senior, Boolean status);
    //todo : 관리자 정산 목록 쿼리 조회 수정
}
