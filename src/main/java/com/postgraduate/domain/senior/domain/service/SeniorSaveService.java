package com.postgraduate.domain.senior.domain.service;

import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.entity.repository.SeniorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeniorSaveService {
    private final SeniorRepository seniorRepository;

    public Senior saveSenior(Senior senior) {
        return seniorRepository.save(senior);
    }
}
