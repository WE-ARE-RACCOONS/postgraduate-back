package com.postgraduate.admin.domain.service;

import com.postgraduate.admin.domain.repository.AdminUserRepository;
import com.postgraduate.domain.member.user.domain.entity.MemberRole;
import com.postgraduate.domain.member.user.domain.entity.User;
import com.postgraduate.domain.member.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserService {
    private final AdminUserRepository adminUserRepository;
    private static final int JUNIOR_PAGE_SIZE = 10;
    public Page<MemberRole> allJunior(Integer page) {
        if (page == null)
            page = 1;
        Pageable pageable = PageRequest.of(page-1, JUNIOR_PAGE_SIZE);
        return adminUserRepository.findAllJunior(pageable);
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
