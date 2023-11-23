package com.postgraduate.domain.senior.domain.repository;

import com.postgraduate.domain.senior.domain.entity.Senior;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SeniorDslRepository {
    Page<Senior> findAllSenior(String search, String sort, Pageable pageable);
}
