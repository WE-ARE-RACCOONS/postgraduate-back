package com.postgraduate.domain.wish.domain.service;

import com.postgraduate.domain.wish.application.mapper.WishMapper;
import com.postgraduate.domain.wish.application.mapper.dto.res.WishResponse;
import com.postgraduate.domain.wish.domain.entity.Wish;
import com.postgraduate.domain.wish.domain.repository.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishGetService {
    private final WishRepository wishRepository;
    public Wish byWishId(Long wishId) {
        return wishRepository.findById(wishId).orElseThrow();
    }
}
