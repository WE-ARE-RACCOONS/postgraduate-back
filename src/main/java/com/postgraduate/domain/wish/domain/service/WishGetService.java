package com.postgraduate.domain.wish.domain.service;

import com.postgraduate.domain.wish.domain.repository.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishGetService {
    private final WishRepository wishRepository;
}
