package com.postgraduate.domain.senior.domain.repository;

import com.postgraduate.domain.senior.domain.entity.Senior;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SeniorDslRepository {
    Page<Senior> findAllBySearchSenior(String search, String sort, Pageable pageable);
    Page<Senior> findAllByFieldSenior(String field, String postgradu, Pageable pageable);
    Page<Senior> findAllBySearchSeniorWithAdmin(String search, Pageable pageable);
    Optional<Senior> findBySeniorId(Long seniorId);
}
