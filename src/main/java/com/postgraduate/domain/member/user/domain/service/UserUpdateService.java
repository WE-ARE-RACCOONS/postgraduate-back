package com.postgraduate.domain.member.user.domain.service;

import com.postgraduate.domain.member.senior.application.dto.req.SeniorMyPageUserAccountRequest;
import com.postgraduate.domain.member.user.application.dto.req.UserInfoRequest;
import com.postgraduate.domain.member.user.domain.entity.MemberRole;
import com.postgraduate.global.auth.login.util.ProfileUtils;
import com.postgraduate.domain.member.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserUpdateService {
    private final ProfileUtils profileUtils;
    public void updateDelete(User user) {
        user.updateDelete();
    }

    public void addJuniorRole(User user, MemberRole memberRole) {
        user.updateRole(memberRole);
    }

    public void addSeniorRole(User user, int num, MemberRole memberRole) {
        if (user.isDefaultProfile(profileUtils.allProfiles()))
            user.updateProfile(profileUtils.seniorProfile(num));
        user.updateRole(memberRole);
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

    public void updateRestore(User user) {
        user.restoreDelete();
    }

    public void tutorialFin(User user) {
        user.tutorialFin();
    }
}
