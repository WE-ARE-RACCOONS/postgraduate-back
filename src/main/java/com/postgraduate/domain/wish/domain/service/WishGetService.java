package com.postgraduate.domain.wish.domain.service;

import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.wish.domain.entity.Wish;
import com.postgraduate.domain.wish.domain.repository.WishRepository;
import com.postgraduate.domain.wish.exception.WishNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishGetService {
    private static final int ADMIN_PAGE_SIZE = 15;

    private final WishRepository wishRepository;

    public Wish byWishId(Long wishId) {
        return wishRepository.findByWishIdAndMatchingReceiveIsTrue(wishId).orElseThrow(WishNotFoundException::new);
    }

    public Optional<Wish> byUser(User user) {
        return wishRepository.findByUser(user);
    }

    public Wish byUserId(Long userId) {
        return wishRepository.findByMatchingReceiveIsTrueAndUser_UserId(userId)
                .orElseThrow(WishNotFoundException::new);
    }

    public Page<Wish> all(Integer page, String search) {
        page = page == null ? 1 : page;
        Pageable pageable = PageRequest.of(page - 1, ADMIN_PAGE_SIZE);
        return wishRepository.findAllBySearchWish(search, pageable);
    }

    public List<Wish> all() {
        return wishRepository.findAllWish();
    }
}
