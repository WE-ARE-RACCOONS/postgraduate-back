package com.postgraduate.admin.domain.service;

import com.postgraduate.admin.domain.repository.AdminUserRepository;
import com.postgraduate.domain.member.user.domain.entity.User;
import com.postgraduate.domain.member.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserService {
    private final AdminUserRepository adminUserRepository;

    public List<User> allJunior() {
        return adminUserRepository.findAllJunior();
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
