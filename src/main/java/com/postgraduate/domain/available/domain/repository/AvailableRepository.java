package com.postgraduate.domain.available.domain.repository;

import com.postgraduate.domain.available.domain.entity.Available;
import com.postgraduate.domain.senior.domain.entity.Senior;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvailableRepository extends JpaRepository<Available, Long> {
    List<Available> findAllBySenior(Senior senior);
    void deleteAllBySenior(Senior senior);
}
