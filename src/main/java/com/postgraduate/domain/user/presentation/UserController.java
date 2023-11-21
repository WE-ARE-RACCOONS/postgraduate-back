package com.postgraduate.domain.user.presentation;

import com.postgraduate.domain.user.application.dto.req.UserInfoRequest;
import com.postgraduate.domain.user.application.dto.res.UserInfoResponse;
import com.postgraduate.domain.user.application.dto.res.UserMyPageResponse;
import com.postgraduate.domain.user.application.usecase.UserManageUseCase;
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
    private final UserManageUseCase manageUseCase;

    @GetMapping("/me")
    @Operation(description = "사용자 마이페이지 정보 조회 - 닉네임, 프로필")
    public ResponseDto<UserMyPageResponse> getUserInfo(@AuthenticationPrincipal User user) {
        UserMyPageResponse userInfo = myPageUseCase.getUserInfo(user);
        return ResponseDto.create(USER_FIND.getCode(), GET_USER_INFO.getMessage(), userInfo);
    }

    @GetMapping("/me/info")
    @Operation(description = "대학생 마이페이지 정보 수정시 기존 정보 조회")
    public ResponseDto<UserInfoResponse> getOriginUserInfo(@AuthenticationPrincipal User user) {
        UserInfoResponse originInfo = myPageUseCase.getUserOriginInfo(user);
        return ResponseDto.create(USER_FIND.getCode(), GET_USER_INFO.getMessage(), originInfo);
    }

    @PatchMapping("/me/info")
    @Operation(description = "대학생 마이페이지 정보 수정 - 프로필사진, 닉네임, 번호")
    public ResponseDto updateInfo(@AuthenticationPrincipal User user, @RequestBody UserInfoRequest userInfoRequest) {
        manageUseCase.updateInfo(user, userInfoRequest);
        return ResponseDto.create(USER_UPDATE.getCode(), UPDATE_USER_INFO.getMessage());
    }

    @GetMapping("/me/role")
    @Operation(description = "선배 전환시 가능 여부 확인 - true,false")
    public ResponseDto<Boolean> checkRole(@AuthenticationPrincipal User user) {
        boolean isOk = myPageUseCase.checkSenior(user);
        return ResponseDto.create(USER_FIND.getCode(), GET_SENIOR_CHECK.getMessage(), isOk);
    }

    @GetMapping("/nickname")
    @Operation(description = "사용자 닉네임 중복체크")
    public ResponseDto<Boolean> duplicatedNickName(@RequestParam String nickName) {
        boolean checkDup = manageUseCase.duplicatedNickName(nickName);
        return ResponseDto.create(USER_FIND.getCode(), GET_NICKNAME_CHECK.getMessage(), checkDup);
    }
}
