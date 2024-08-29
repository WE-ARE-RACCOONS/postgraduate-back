package com.postgraduate.admin.domain.service;

import com.postgraduate.admin.domain.repository.AdminUserRepository;
import com.postgraduate.domain.member.user.domain.entity.User;
import com.postgraduate.domain.member.user.exception.UserNotFoundException;
import com.postgraduate.domain.member.user.domain.entity.Wish;
import com.postgraduate.domain.member.user.exception.WishNotFoundException;
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

    public void updateWishDone(Long wishId) {
        User user = adminUserRepository.findUserByWishId(wishId)
                        .orElseThrow(WishNotFoundException::new);
        user.updateWishDone();
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
