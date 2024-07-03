package com.postgraduate.domain.user.domain.service;

import com.postgraduate.domain.user.domain.entity.Quit;
import com.postgraduate.domain.user.domain.repository.QuitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuitSaveService {
    private final QuitRepository quitRepository;

    public void save(Quit quit) {
        quitRepository.save(quit);
    }
}
