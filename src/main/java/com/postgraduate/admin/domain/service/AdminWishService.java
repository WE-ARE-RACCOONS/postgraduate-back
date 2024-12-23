package com.postgraduate.admin.domain.service;

import com.postgraduate.admin.domain.repository.AdminWishRepository;
import com.postgraduate.domain.member.user.exception.WishNotFoundException;
import com.postgraduate.domain.wish.domain.entity.Wish;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminWishService {
    private final AdminWishRepository adminWishRepository;
    private static final int WISH_PAGE_SIZE = 10;
    public Page<Wish> findAllWaiting(Integer page) {
        if (page == null)
            page = 1;
        Pageable pageable = PageRequest.of(page-1, WISH_PAGE_SIZE);
        return adminWishRepository.findAllWaitingWish(pageable);
    }

    public Page<Wish> findAllMatching(Integer page) {
        if (page == null)
            page = 1;
        Pageable pageable = PageRequest.of(page-1, WISH_PAGE_SIZE);
        return adminWishRepository.findAllMatchingWish(pageable);
    }

    public Wish matchFin(Long wishId) {
        Wish wish = adminWishRepository.findByWishId(wishId)
                .orElseThrow(WishNotFoundException::new);
        wish.updateDone();
        return wish;
    }
}
