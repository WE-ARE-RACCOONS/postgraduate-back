package com.postgraduate.domain.user.wish.domain.service;

import com.postgraduate.domain.user.wish.domain.repository.WishRepository;
import com.postgraduate.domain.user.wish.domain.entity.Wish;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishSaveService {
    private final WishRepository wishRepository;

    public void save(Wish wish) {
        wishRepository.save(wish);
    }
}
