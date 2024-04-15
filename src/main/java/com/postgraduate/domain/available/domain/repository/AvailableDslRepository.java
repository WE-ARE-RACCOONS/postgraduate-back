package com.postgraduate.domain.available.domain.repository;

import com.postgraduate.domain.available.domain.entity.Available;
import com.postgraduate.domain.senior.domain.entity.Senior;

import java.util.List;

public interface AvailableDslRepository {
    List<Available> findAllBySenior(Senior senior);
    List<Available> findAllByMine(Senior senior);

    /**
     * Case B
     */
    List<Available> findAllByAnySenior(Senior senior);
}
