package com.postgraduate.admin.domain.service;

import com.postgraduate.admin.domain.repository.AdminUserRepository;
import com.postgraduate.domain.wish.domain.entity.Wish;
import com.postgraduate.domain.wish.exception.WishNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserService {
    private final AdminUserRepository adminUserRepository;

    public List<Wish> allJunior() {
        return adminUserRepository.findAllJunior();
    }

    public Wish byUserId(long userId) {
        return adminUserRepository.findByUserId(userId)
                .orElseThrow(WishNotFoundException::new);
    }

    public void updateWishDone(Long wishId) {
        Wish wish = adminUserRepository.findByWishId(wishId)
                        .orElseThrow(WishNotFoundException::new);
        wish.updateDone();
    }
}
