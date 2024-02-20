package com.postgraduate.domain.refuse.domain.service;

import com.postgraduate.domain.refuse.domain.entity.Refuse;
import com.postgraduate.domain.refuse.domain.repository.RefuseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefuseSaveService {
    private final RefuseRepository refuseRepository;

    public void save(Refuse refuse) {
        refuseRepository.save(refuse);
    }
}
