package com.postgraduate.domain.user.domain.service;

import com.postgraduate.domain.senior.application.dto.req.SeniorMyPageProfileRequest;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.entity.constant.Role;
import com.postgraduate.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserUpdateService {
    private final UserRepository userRepository;

    public void updateNickName(Long userId, String nickName) {
        User user = userRepository.findById(userId).get();
        user.updateNickName(nickName);
    }

    public void updateRole(Long userId, Role role) {
        User user = userRepository.findById(userId).get();
        user.updateRole(role);
    }

    public void updateProfile(Long userId, String profile) {
        User user = userRepository.findById(userId).get();
        user.updateProfile(profile);
    }

    public void updateSeniorMyPage(Long userId, SeniorMyPageProfileRequest myPageProfileRequest) {
        User user = userRepository.findById(userId).get();
        user.updateSeniorMyPage(myPageProfileRequest);
    }
}
