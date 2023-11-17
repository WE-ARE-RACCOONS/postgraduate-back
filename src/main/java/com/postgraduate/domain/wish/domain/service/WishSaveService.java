package com.postgraduate.domain.wish.domain.service;

import com.postgraduate.domain.wish.domain.entity.Wish;
import com.postgraduate.domain.wish.domain.repository.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishSaveService {
    private final WishRepository wishRepository;

    public void saveWish(Wish wish) {
        wishRepository.save(wish);
    }
}
