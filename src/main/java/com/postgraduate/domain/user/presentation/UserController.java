package com.postgraduate.domain.user.presentation;

import com.postgraduate.domain.user.application.dto.req.UserNickNameRequest;
import com.postgraduate.domain.user.application.dto.res.UserInfoResponse;
import com.postgraduate.domain.user.application.usecase.UserMyPageUseCase;
import com.postgraduate.global.auth.AuthDetails;
import com.postgraduate.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.postgraduate.domain.user.presentation.constant.UserResponseMessage.GET_USER_INFO;
import static com.postgraduate.domain.user.presentation.constant.UserResponseMessage.UPDATE_USER_INFO;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "USER Controller")
public class UserController {
    private final UserMyPageUseCase myPageUseCase;

    @GetMapping("/me")
    @Operation(description = "사용자 기본 정보 조회 - 닉네임, 은행, 계좌, 포인트")
    public ResponseDto<UserInfoResponse> getUserInfo(@AuthenticationPrincipal AuthDetails authDetails) {
        UserInfoResponse userInfo = myPageUseCase.getUserInfo(authDetails);
        return ResponseDto.create(OK.value(), GET_USER_INFO.getMessage(), userInfo);
    }

    @PostMapping("/nickname")
    @Operation(description = "사용자 닉네임 변경 및 업데이트")
    public ResponseDto updateNickName(@AuthenticationPrincipal AuthDetails authDetails, @RequestBody UserNickNameRequest userNickNameRequest) {
        myPageUseCase.updateUser(authDetails, userNickNameRequest.getNickName());
        return ResponseDto.create(OK.value(), UPDATE_USER_INFO.getMessage());
    }
}
