package com.postgraduate.domain.user.wish.domain.service;

import com.postgraduate.domain.user.user.domain.entity.User;
import com.postgraduate.domain.user.wish.domain.repository.WishRepository;
import com.postgraduate.domain.user.wish.exception.WishNotFoundException;
import com.postgraduate.domain.user.wish.domain.entity.Wish;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishGetService {
    private final WishRepository wishRepository;

    public Wish byWishId(Long wishId) {
        return wishRepository.findByWishIdAndMatchingReceiveIsTrueAndUser_IsDeleteIsFalse(wishId)
                .orElseThrow(WishNotFoundException::new);
    }

    public Optional<Wish> byUser(User user) {
        return wishRepository.findByUserAndUser_IsDeleteIsFalse(user);
    }
}
