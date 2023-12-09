package com.postgraduate.domain.wish.domain.service;

import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.wish.domain.entity.Wish;
import com.postgraduate.domain.wish.domain.repository.WishRepository;
import com.postgraduate.domain.wish.exception.WishNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishGetService {
    private final WishRepository wishRepository;
    public Wish byWishId(Long wishId) {
        return wishRepository.findById(wishId).orElseThrow(WishNotFoundException::new);
    }

    public Optional<Wish> byUser(User user) {
        return wishRepository.findByUser(user);
    }

    public List<Wish> all() {
        return wishRepository.findAll();
    }
}
