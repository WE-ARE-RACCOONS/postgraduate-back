package com.postgraduate.domain.senior.domain.repository;

import com.postgraduate.domain.salary.application.dto.SeniorAndAccount;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SeniorDslRepository {
    Page<Senior> findAllBySearchSenior(String search, String sort, Pageable pageable);
    Page<Senior> findAllByFieldSenior(String field, String postgradu, Pageable pageable);
    Page<Senior> findAllBySearchSeniorWithAdmin(String search, Pageable pageable);
    Optional<Senior> findBySeniorId(Long seniorId);
    Optional<Senior> findByUserWithAll(User user);
    List<SeniorAndAccount> findAllSeniorAndAccount();
    List<Senior> findAllSenior();
}
