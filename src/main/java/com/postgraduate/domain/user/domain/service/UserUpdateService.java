package com.postgraduate.domain.user.domain.service;

import com.postgraduate.domain.senior.application.dto.req.SeniorMyPageUserAccountRequest;
import com.postgraduate.domain.user.application.dto.req.UserInfoRequest;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.entity.constant.Role;
import com.postgraduate.domain.user.domain.repository.UserRepository;
import com.postgraduate.domain.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserUpdateService {
    private final UserRepository userRepository;

    public void updateRole(Long userId, Role role) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.updateRole(role);
    }

    public void updateInfo(Long userId, UserInfoRequest userInfoRequest) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.updateInfo(
                userInfoRequest.getProfile(),
                userInfoRequest.getNickName(),
                userInfoRequest.getPhoneNumber()
        );
    }

    public void updateSeniorUserAccount(Long userId, SeniorMyPageUserAccountRequest myPageUserAccountRequest) {
        User user = userRepository.findById(userId).orElseThrow();
        user.updateInfo(
                myPageUserAccountRequest.profile(),
                myPageUserAccountRequest.nickName(),
                myPageUserAccountRequest.phoneNumber()
        );
    }
}
