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
    public void updateDelete(User user) {
        user.updateDelete();
    }

    public void updateRole(User user, Role role) {
        user.updateRole(role);
    }

    public void updateInfo(User user, UserInfoRequest userInfoRequest) {
        user.updateInfo(
                userInfoRequest.getProfile(),
                userInfoRequest.getNickName(),
                userInfoRequest.getPhoneNumber()
        );
    }

    public void updateSeniorUserAccount(User user, SeniorMyPageUserAccountRequest myPageUserAccountRequest) {
        user.updateInfo(
                myPageUserAccountRequest.profile(),
                myPageUserAccountRequest.nickName(),
                myPageUserAccountRequest.phoneNumber()
        );
    }
}
