package com.postgraduate.domain.member.user.application.usecase;

import com.postgraduate.domain.member.user.application.dto.res.UserMyPageResponse;
import com.postgraduate.domain.member.user.application.dto.res.UserInfoResponse;
import com.postgraduate.domain.member.user.application.dto.res.UserPossibleResponse;
import com.postgraduate.domain.member.user.application.mapper.UserMapper;
import com.postgraduate.domain.member.user.domain.entity.User;
import com.postgraduate.domain.member.user.domain.entity.constant.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMyPageUseCase {
    public UserMyPageResponse getUserInfo(User user) {
        return UserMapper.mapToMyPageInfo(user);
    }

    public UserInfoResponse getUserOriginInfo(User user) {
        return UserMapper.mapToInfo(user);
    }

    public UserPossibleResponse checkSenior(User user) {
        Role role = user.getRole();
        if (role.equals(Role.SENIOR))
            return new UserPossibleResponse(true, user.getSocialId());
        return new UserPossibleResponse(false, user.getSocialId());
    }
}
