package com.postgraduate.global.auth.quit.domain.service;

import com.postgraduate.global.auth.quit.domain.entity.Quit;
import com.postgraduate.global.auth.quit.domain.repository.QuitRepository;
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
