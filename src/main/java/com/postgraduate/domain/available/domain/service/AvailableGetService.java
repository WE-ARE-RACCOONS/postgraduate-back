package com.postgraduate.domain.available.domain.service;

import com.postgraduate.domain.available.domain.entity.Available;
import com.postgraduate.domain.available.domain.repository.AvailableRepository;
import com.postgraduate.domain.senior.domain.entity.Senior;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvailableGetService {
    private final AvailableRepository availableRepository;

    public List<Available> byMine(Senior senior) {
        return availableRepository.findAllByMine(senior);
    }

    public List<Available> bySenior(Senior senior) {
        return availableRepository.findAllBySenior(senior);
    }
}
