package com.postgraduate.admin.domain.service;

import com.postgraduate.admin.domain.repository.AdminWishRepository;
import com.postgraduate.domain.member.user.exception.WishNotFoundException;
import com.postgraduate.domain.wish.domain.entity.Wish;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminWishService {
    private final AdminWishRepository adminWishRepository;

    public List<Wish> findAllWaiting() {
        return adminWishRepository.findAllWaitingWish();
    }

    public List<Wish> findAllMatching() {
        return adminWishRepository.findAllMatchingWish();
    }

    public Wish matchFin(Long wishId) {
        Wish wish = adminWishRepository.findByWishId(wishId)
                .orElseThrow(WishNotFoundException::new);
        wish.updateDone();
        return wish;
    }
}
