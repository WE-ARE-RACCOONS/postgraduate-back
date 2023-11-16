package com.postgraduate.domain.auth.application.usecase;

import com.postgraduate.domain.auth.application.dto.req.SeniorChangeRequest;
import com.postgraduate.domain.auth.application.dto.req.SeniorSignUpRequest;
import com.postgraduate.domain.auth.application.dto.req.SignUpRequest;
import com.postgraduate.domain.senior.application.mapper.SeniorMapper;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorSaveService;
import com.postgraduate.domain.user.application.mapper.UserMapper;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.entity.constant.Role;
import com.postgraduate.domain.user.domain.service.UserGetService;
import com.postgraduate.domain.user.domain.service.UserSaveService;
import com.postgraduate.domain.user.domain.service.UserUpdateService;
import com.postgraduate.domain.wish.application.mapper.WishMapper;
import com.postgraduate.domain.wish.domain.entity.Wish;
import com.postgraduate.domain.wish.domain.service.WishSaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class SignUpUseCase {
    private final UserSaveService userSaveService;
    private final UserUpdateService userUpdateService;
    private final UserGetService userGetService;
    private final WishSaveService wishSaveService;
    private final SeniorSaveService seniorSaveService;

    public User userSignUp(SignUpRequest request) {
        User user = UserMapper.mapToUser(request);
        Wish wish = WishMapper.mapToWish(user, request);
        wishSaveService.saveWish(wish);
        userSaveService.saveUser(user);
        return user;
    }

    public User seniorSignUp(SeniorSignUpRequest request) {
        User user = UserMapper.mapToUser(request);
        userSaveService.saveUser(user);
        Senior senior = SeniorMapper.mapToSenior(user, request);
        seniorSaveService.saveSenior(senior);
        return senior.getUser();
    }


    public User changeSenior(User user, SeniorChangeRequest changeRequest) {
        Senior senior = SeniorMapper.mapToSenior(user, changeRequest); //todo : 예외 처리
        seniorSaveService.saveSenior(senior);
        userUpdateService.updateRole(user.getUserId(), Role.SENIOR);
        return userGetService.getUser(user.getUserId());
    }
}
