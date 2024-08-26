package com.postgraduate.domain.senior.available.domain.repository;

import com.postgraduate.domain.senior.available.domain.entity.Available;
import com.postgraduate.domain.senior.domain.entity.Senior;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvailableRepository extends JpaRepository<Available, Long>, AvailableDslRepository {
    void deleteAllBySenior(Senior senior);
}
