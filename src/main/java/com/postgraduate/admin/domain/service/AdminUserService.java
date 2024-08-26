package com.postgraduate.admin.domain.service;

import com.postgraduate.admin.domain.repository.AdminUserRepository;
import com.postgraduate.domain.user.user.domain.entity.User;
import com.postgraduate.domain.user.user.exception.UserNotFoundException;
import com.postgraduate.domain.user.wish.domain.entity.Wish;
import com.postgraduate.domain.user.wish.exception.WishNotFoundException;
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

    public Wish wishByUserId(long userId) {
        return adminUserRepository.findWishByUserId(userId)
                .orElseThrow(WishNotFoundException::new);
    }

    public void updateWishDone(Long wishId) {
        Wish wish = adminUserRepository.findWishByWishId(wishId)
                        .orElseThrow(WishNotFoundException::new);
        wish.updateDone();
    }

    public User userByUserId(Long userId) {
        return adminUserRepository.findUserByUserId(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    public User login(String id, String pw) {
        return adminUserRepository.login(id, pw)
                .orElseThrow(UserNotFoundException::new);
    }
}
