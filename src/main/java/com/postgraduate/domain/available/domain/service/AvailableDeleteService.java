package com.postgraduate.domain.available.domain.service;

import com.postgraduate.domain.available.domain.repository.AvailableRepository;
import com.postgraduate.domain.senior.domain.entity.Senior;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvailableDeleteService {
    private final AvailableRepository availableRepository;

    public void delete(Senior senior) {
        availableRepository.deleteAllBySenior(senior);
    }
}
