package com.postgraduate.domain.user.domain.service;

import com.postgraduate.domain.senior.application.dto.req.SeniorMyPageUserAccountRequest;
import com.postgraduate.domain.user.application.dto.req.UserInfoRequest;
import com.postgraduate.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.postgraduate.domain.user.domain.entity.constant.Role.SENIOR;

@Service
@RequiredArgsConstructor
public class UserUpdateService {
    public void updateDelete(User user) {
        user.updateDelete();
    }

    public void userToSeniorRole(User user) {
        user.updateRole(SENIOR);
    }

    public void updateInfo(User user, UserInfoRequest userInfoRequest) {
        user.updateInfo(
                userInfoRequest.profile(),
                userInfoRequest.nickName(),
                userInfoRequest.phoneNumber()
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
