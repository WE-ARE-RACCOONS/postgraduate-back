package com.postgraduate.domain.user.application.usecase;

import com.postgraduate.domain.user.application.dto.res.UserInfoResponse;
import com.postgraduate.domain.user.application.dto.res.UserMyPageResponse;
import com.postgraduate.domain.user.application.dto.res.UserPossibleResponse;
import com.postgraduate.domain.user.application.mapper.UserMapper;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.entity.constant.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.postgraduate.domain.user.application.mapper.UserMapper.mapToInfo;
import static com.postgraduate.domain.user.domain.entity.constant.Role.SENIOR;

@Service
@Transactional
@RequiredArgsConstructor
public class UserMyPageUseCase {
    public UserMyPageResponse getUserInfo(User user) {
        return UserMapper.mapToMyPageInfo(user);
    }

    public UserInfoResponse getUserOriginInfo(User user) {
        return mapToInfo(user);
    }

    public UserPossibleResponse checkSenior(User user) {
        Role role = user.getRole();
        if (role.equals(SENIOR))
            return new UserPossibleResponse(true, user.getSocialId());
        return new UserPossibleResponse(false, user.getSocialId());
    }
}
