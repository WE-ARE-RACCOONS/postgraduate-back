package com.postgraduate.domain.available.domain.repository;

import com.postgraduate.domain.available.domain.entity.Available;

import java.util.List;

public interface AvailableDslRepository {
    List<Available> findAllBySenior(Long seniorId);
}
