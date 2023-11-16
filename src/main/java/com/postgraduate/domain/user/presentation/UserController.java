package com.postgraduate.domain.user.presentation;

import com.postgraduate.domain.user.application.dto.req.UserNickNameRequest;
import com.postgraduate.domain.user.application.dto.req.UserProfileRequest;
import com.postgraduate.domain.user.application.dto.res.UserInfoResponse;
import com.postgraduate.domain.user.application.usecase.UserMyPageUseCase;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.postgraduate.domain.user.presentation.constant.UserResponseCode.USER_FIND;
import static com.postgraduate.domain.user.presentation.constant.UserResponseCode.USER_UPDATE;
import static com.postgraduate.domain.user.presentation.constant.UserResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "USER Controller")
public class UserController {
    private final UserMyPageUseCase myPageUseCase;

    @GetMapping("/me")
    @Operation(description = "사용자 기본 정보 조회 - 닉네임, 프로필")
    public ResponseDto<UserInfoResponse> getUserInfo(@AuthenticationPrincipal User user) {
        UserInfoResponse userInfo = myPageUseCase.getUserInfo(user);
        return ResponseDto.create(USER_FIND.getCode(), GET_USER_INFO.getMessage(), userInfo);
    }

    @PatchMapping("/nickname")
    @Operation(description = "사용자 닉네임 변경 및 업데이트")
    public ResponseDto updateNickName(@AuthenticationPrincipal User user, @RequestBody UserNickNameRequest userNickNameRequest) {
        myPageUseCase.updateNickName(user, userNickNameRequest.getNickName());
        return ResponseDto.create(USER_UPDATE.getCode(), UPDATE_USER_INFO.getMessage());
    }

    @GetMapping("/nickname")
    @Operation(description = "사용자 닉네임 중복체크")
    public ResponseDto<Boolean> duplicatedNickName(@RequestParam String nickName) {
        boolean checkDup = myPageUseCase.duplicatedNickName(nickName);
        return ResponseDto.create(USER_FIND.getCode(), GET_NICKNAME_CHECK.getMessage(), checkDup);
    }

    @PatchMapping("/profile")
    @Operation(description = "사용자 프로필 사진 업데이트 - url을 주세요")
    public ResponseDto updateProfile(@AuthenticationPrincipal User user, @RequestBody UserProfileRequest userProfileRequest) {
        myPageUseCase.updateProfile(user, userProfileRequest.getProfile());
        return ResponseDto.create(USER_UPDATE.getCode(), UPDATE_USER_INFO.getMessage());
    }
}
