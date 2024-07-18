package com.postgraduate.domain.user.user.presentation;

import com.postgraduate.domain.user.user.application.dto.req.UserInfoRequest;
import com.postgraduate.domain.user.user.application.dto.res.UserInfoResponse;
import com.postgraduate.domain.user.user.application.dto.res.UserMyPageResponse;
import com.postgraduate.domain.user.user.application.dto.res.UserPossibleResponse;
import com.postgraduate.domain.user.user.application.usecase.UserManageUseCase;
import com.postgraduate.domain.user.user.application.usecase.UserMyPageUseCase;
import com.postgraduate.domain.user.user.domain.entity.User;
import com.postgraduate.domain.user.user.presentation.constant.UserResponseCode;
import com.postgraduate.domain.user.user.presentation.constant.UserResponseMessage;
import com.postgraduate.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.postgraduate.global.dto.ResponseDto.create;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "USER Controller")
public class UserController {
    private final UserMyPageUseCase myPageUseCase;
    private final UserManageUseCase manageUseCase;

    @GetMapping("/me")
    @Operation(summary = "사용자 마이페이지 정보 조회 | 토큰 필요", description = "닉네임, 프로필")
    public ResponseEntity<ResponseDto<UserMyPageResponse>> getUserInfo(@AuthenticationPrincipal User user) {
        UserMyPageResponse userInfo = myPageUseCase.getUserInfo(user);
        return ResponseEntity.ok(create(UserResponseCode.USER_FIND.getCode(), UserResponseMessage.GET_USER_INFO.getMessage(), userInfo));
    }

    @GetMapping("/me/info")
    @Operation(summary = "대학생 마이페이지 정보 수정 화면 기존 정보 | 토큰 필요", description = "프로필사진, 닉네임, 번호")
    public ResponseEntity<ResponseDto<UserInfoResponse>> getOriginUserInfo(@AuthenticationPrincipal User user) {
        UserInfoResponse originInfo = myPageUseCase.getUserOriginInfo(user);
        return ResponseEntity.ok(create(UserResponseCode.USER_FIND.getCode(), UserResponseMessage.GET_USER_INFO.getMessage(), originInfo));
    }

    @PatchMapping("/me/info")
    @Operation(summary = "대학생 마이페이지 정보 수정 | 토큰 필요", description = "프로필사진, 닉네임, 번호")
    public ResponseEntity<ResponseDto<Void>> updateInfo(@AuthenticationPrincipal User user,
                                  @RequestBody @Valid UserInfoRequest userInfoRequest) {
        manageUseCase.updateInfo(user, userInfoRequest);
        return ResponseEntity.ok(create(UserResponseCode.USER_UPDATE.getCode(), UserResponseMessage.UPDATE_USER_INFO.getMessage()));
    }

    @GetMapping("/me/role")
    @Operation(summary = "선배 전환시 가능 여부 확인 | 토큰 필요", description = "true-가능, false-불가능")
    public ResponseEntity<ResponseDto<UserPossibleResponse>> checkRole(@AuthenticationPrincipal User user) {
        UserPossibleResponse possibleResponse = myPageUseCase.checkSenior(user);
        return ResponseEntity.ok(create(UserResponseCode.USER_FIND.getCode(), UserResponseMessage.GET_SENIOR_CHECK.getMessage(), possibleResponse));
    }

    @GetMapping("/nickname")
    @Operation(summary = "사용자 닉네임 중복체크", description = "true-사용가능, false-사용불가능")
    public ResponseEntity<ResponseDto<Boolean>> duplicatedNickName(@RequestParam String nickName) {
        Boolean checkDup = manageUseCase.duplicatedNickName(nickName);
        return ResponseEntity.ok(create(UserResponseCode.USER_FIND.getCode(), UserResponseMessage.GET_NICKNAME_CHECK.getMessage(), checkDup));
    }
}
