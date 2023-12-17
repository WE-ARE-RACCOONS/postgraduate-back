package com.postgraduate.domain.available.domain.service;

import com.postgraduate.domain.available.domain.entity.Available;
import com.postgraduate.domain.available.domain.repository.AvailableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvailableSaveService {
    private final AvailableRepository availableRepository;

    public void save(Available available) {
        availableRepository.save(available);
    }
}
