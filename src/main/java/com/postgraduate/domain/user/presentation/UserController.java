package com.postgraduate.domain.user.presentation;

import com.postgraduate.domain.user.application.dto.res.UserInfoResponse;
import com.postgraduate.domain.user.application.usecase.UserMyPageUseCase;
import com.postgraduate.domain.user.presentation.constant.UserResponseMessage;
import com.postgraduate.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.postgraduate.domain.user.presentation.constant.UserResponseMessage.GET_USER_INFO;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserMyPageUseCase myPageUseCase;

    @GetMapping("/me")
    public ResponseDto<UserInfoResponse> getUserInfo() {
        UserInfoResponse userInfo = myPageUseCase.getUserInfo();
        return ResponseDto.create(OK.value(), GET_USER_INFO.getMessage(), userInfo);
    }
}
