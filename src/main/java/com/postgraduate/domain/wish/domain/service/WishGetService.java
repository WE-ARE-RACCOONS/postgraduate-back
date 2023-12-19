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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishGetService {
    private static final int ADMIN_PAGE_SIZE = 15;

    private final WishRepository wishRepository;

    public Wish byWishId(Long wishId) {
        return wishRepository.findById(wishId).orElseThrow(WishNotFoundException::new);
    }

    public Optional<Wish> byUser(User user) {
        return wishRepository.findByUser(user);
    }

    public Page<Wish> all(Integer page) {
        if (page == null) {
            page = 1;
        }
        Pageable pageable = PageRequest.of(page - 1, ADMIN_PAGE_SIZE);
        return wishRepository.findAllByUser_IsDelete(false, pageable);
    }

    public Long count() {
        return wishRepository.count();
    }
}
