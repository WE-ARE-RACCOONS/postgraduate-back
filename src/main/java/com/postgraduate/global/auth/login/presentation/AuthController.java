package com.postgraduate.global.auth.login.presentation;

import com.postgraduate.global.auth.login.application.dto.res.AuthResponse;
import com.postgraduate.global.auth.login.application.dto.res.AuthUserResponse;
import com.postgraduate.global.auth.login.application.dto.res.JwtTokenResponse;
import com.postgraduate.global.auth.login.application.usecase.oauth.SelectOauth;
import com.postgraduate.global.auth.login.application.usecase.oauth.SignOutUseCase;
import com.postgraduate.global.auth.login.application.usecase.oauth.SignUpUseCase;
import com.postgraduate.global.auth.login.application.usecase.jwt.JwtUseCase;
import com.postgraduate.global.auth.login.application.usecase.oauth.SignInUseCase;
import com.postgraduate.global.auth.login.presentation.constant.Provider;
import com.postgraduate.domain.member.user.application.usecase.UserManageUseCase;
import com.postgraduate.domain.member.user.domain.entity.User;
import com.postgraduate.global.auth.login.application.dto.req.*;
import com.postgraduate.global.auth.login.presentation.constant.AuthResponseCode;
import com.postgraduate.global.auth.login.presentation.constant.AuthResponseMessage;
import com.postgraduate.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.postgraduate.domain.member.senior.presentation.constant.SeniorResponseCode.SENIOR_CREATE;
import static com.postgraduate.domain.member.senior.presentation.constant.SeniorResponseMessage.CREATE_SENIOR;
import static com.postgraduate.global.dto.ResponseDto.create;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "AUTH Controller")
public class AuthController {
    private final SelectOauth selectOauth;
    private final SignUpUseCase signUpUseCase;
    private final JwtUseCase jwtUseCase;
    private final UserManageUseCase userManageUseCase;

    @PostMapping("/login/token/{provider}")
    @Operation(summary = "소셜 로그인", description = "회원인 경우 JWT를, 회원이 아닌 경우 socialId를 반환합니다(회원가입은 진행하지 않습니다). 탈퇴 회원인 경우 isDelete = true 가 응답됩니다")
    public ResponseEntity<ResponseDto<AuthResponse>> authLoginWithToken(@RequestBody @Valid TokenRequest request, @PathVariable Provider provider) {
        SignInUseCase signInUseCase = selectOauth.selectSignIn(provider);
        AuthUserResponse authUser = signInUseCase.getUserWithToken(request);
        if (authUser.user() == null)
            return ResponseEntity.ok(create(AuthResponseCode.AUTH_NONE.getCode(), AuthResponseMessage.NOT_REGISTERED_USER.getMessage(), authUser));
        JwtTokenResponse jwtToken = jwtUseCase.signIn(authUser.user());
        return ResponseEntity.ok(create(AuthResponseCode.AUTH_ALREADY.getCode(), AuthResponseMessage.SUCCESS_AUTH.getMessage(), jwtToken));
    }

    @PostMapping("/login/{provider}")
    @Operation(summary = "소셜 로그인", description = "회원인 경우 JWT를, 회원이 아닌 경우 socialId를 반환합니다(회원가입은 진행하지 않습니다). 탈퇴 회원인 경우 isDelete = true 가 응답됩니다")
    public ResponseEntity<ResponseDto<AuthResponse>> authLogin(@RequestBody @Valid CodeRequest request, @PathVariable Provider provider) {
        SignInUseCase signInUseCase = selectOauth.selectSignIn(provider);
        AuthUserResponse authUser = signInUseCase.getUser(request);
        if (authUser.user() == null)
            return ResponseEntity.ok(create(AuthResponseCode.AUTH_NONE.getCode(), AuthResponseMessage.NOT_REGISTERED_USER.getMessage(), authUser));
        JwtTokenResponse jwtToken = jwtUseCase.signIn(authUser.user());
        return ResponseEntity.ok(create(AuthResponseCode.AUTH_ALREADY.getCode(), AuthResponseMessage.SUCCESS_AUTH.getMessage(), jwtToken));
    }

    @PostMapping("/dev/login/{provider}")
    @Operation(summary = "개발용 소셜 로그인", description = "회원인 경우 JWT를, 회원이 아닌 경우 socialId를 반환합니다(회원가입은 진행하지 않습니다). 탈퇴 회원인 경우 isDelete = true 가 응답됩니다")
    public ResponseEntity<ResponseDto<AuthResponse>> authDevLogin(@RequestBody @Valid CodeRequest request, @PathVariable Provider provider) {
        SignInUseCase signInUseCase = selectOauth.selectSignIn(provider);
        AuthUserResponse authUser = signInUseCase.getDevUser(request);
        if (authUser.user() == null)
            return ResponseEntity.ok(create(AuthResponseCode.AUTH_NONE.getCode(), AuthResponseMessage.NOT_REGISTERED_USER.getMessage(), authUser));
        JwtTokenResponse jwtToken = jwtUseCase.signIn(authUser.user());
        return ResponseEntity.ok(create(AuthResponseCode.AUTH_ALREADY.getCode(), AuthResponseMessage.SUCCESS_AUTH.getMessage(), jwtToken));
    }

    @PatchMapping("/rejoin/{provider}")
    @Operation(summary = "탈퇴 사용자 재가입", description = "복구를 희망하는 경우 rejoin = true 희망하지 않는 경우 false 를 넣어주세요. 복구시 기존 로그인과 동일한 응답")
    public ResponseEntity<ResponseDto<AuthResponse>> reJoin(@PathVariable Provider provider, @RequestBody RejoinRequest request) {
        User user = userManageUseCase.updateRejoin(provider, request);
        JwtTokenResponse jwtToken = jwtUseCase.signIn(user);
        return ResponseEntity.ok(create(AuthResponseCode.AUTH_ALREADY.getCode(), AuthResponseMessage.SUCCESS_AUTH.getMessage(), jwtToken));
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "토큰 같이 보내주세요")
    public ResponseEntity<ResponseDto<Void>> logout(@AuthenticationPrincipal User user) {
        jwtUseCase.logout(user);
        return ResponseEntity.ok(create(AuthResponseCode.AUTH_DELETE.getCode(), AuthResponseMessage.LOGOUT_USER.getMessage()));
    }

    @PostMapping("/user/signup")
    @Operation(summary = "대학생 회원가입", description = "로그인 API에서 반환한 socialId, 닉네임, 번호, 마케팅 수신여부, 희망 학과, 희망 분야, 매칭 희망 여부")
    public ResponseEntity<ResponseDto<JwtTokenResponse>> signUpUser(@RequestBody @Valid SignUpRequest request) {
        User user = signUpUseCase.userSignUp(request);
        JwtTokenResponse jwtToken = jwtUseCase.signIn(user);
        return ResponseEntity.ok(create(AuthResponseCode.AUTH_CREATE.getCode(), AuthResponseMessage.SUCCESS_AUTH.getMessage(), jwtToken));
    }

    @PostMapping("/user/token")
    @Operation(summary = "후배로 변경 | 토큰 필요", description = "후배로 변경 가능한 경우 후배 토큰 발급")
    public ResponseEntity<ResponseDto<JwtTokenResponse>> changeUserToken(@AuthenticationPrincipal User user) {
        JwtTokenResponse jwtToken = jwtUseCase.changeUser(user);
        return ResponseEntity.ok(create(AuthResponseCode.AUTH_CREATE.getCode(), AuthResponseMessage.SUCCESS_AUTH.getMessage(), jwtToken));
    }

    @PostMapping("/user/change")
    @Operation(summary = "후배로 추가 가입 | 토큰 필요", description = "대학원생 대학생으로 변경 추가 가입")
    public ResponseEntity<ResponseDto<JwtTokenResponse>> changeUser(@AuthenticationPrincipal User user,
                                                                    @RequestBody @Valid UserChangeRequest changeRequest) {
        signUpUseCase.changeUser(user, changeRequest);
        JwtTokenResponse jwtToken = jwtUseCase.changeUser(user);
        return ResponseEntity.ok(create(AuthResponseCode.AUTH_CREATE.getCode(), AuthResponseMessage.SUCCESS_AUTH.getMessage(), jwtToken));
    }

    @PostMapping("/senior/signup")
    @Operation(summary = "대학원생 가입 - 필수 과정만", description = "대학원생 회원가입 - 필수 과정만")
    public ResponseEntity<ResponseDto<JwtTokenResponse>> singUpSenior(@RequestBody @Valid SeniorSignUpRequest request) {
        User user = signUpUseCase.seniorSignUp(request);
        JwtTokenResponse jwtToken = jwtUseCase.signIn(user);
        return ResponseEntity.ok(create(SENIOR_CREATE.getCode(), CREATE_SENIOR.getMessage(), jwtToken));
    }

    @PostMapping("/senior/change")
    @Operation(summary = "선배로 추가 가입 | 토큰 필요", description = "대학생 대학원생으로 변경 추가 가입")
    public ResponseEntity<ResponseDto<JwtTokenResponse>> changeSenior(@AuthenticationPrincipal User user,
                                                                      @RequestBody @Valid SeniorChangeRequest changeRequest) {
        User changeUser = signUpUseCase.changeSenior(user, changeRequest);
        JwtTokenResponse jwtToken = jwtUseCase.changeSenior(changeUser);
        return ResponseEntity.ok(create(SENIOR_CREATE.getCode(), CREATE_SENIOR.getMessage(), jwtToken));
    }

    @PostMapping("/senior/token")
    @Operation(summary = "선배로 변경 | 토큰 필요", description = "선배로 변경 가능한 경우 선배 토큰 발급")
    public ResponseEntity<ResponseDto<JwtTokenResponse>> changeSeniorToken(@AuthenticationPrincipal User user) {
        JwtTokenResponse jwtToken = jwtUseCase.changeSenior(user);
        return ResponseEntity.ok(create(AuthResponseCode.AUTH_CREATE.getCode(), AuthResponseMessage.SUCCESS_AUTH.getMessage(), jwtToken));
    }

    @PostMapping("/refresh")
    @Operation(summary = "토큰 재발급 | 토큰 필요", description = "refreshToken 으로 토큰 재발급")
    public ResponseEntity<ResponseDto<JwtTokenResponse>> refresh(@AuthenticationPrincipal User user, HttpServletRequest request) {
        JwtTokenResponse jwtToken = jwtUseCase.regenerateToken(user, request);
        return ResponseEntity.ok(create(AuthResponseCode.AUTH_UPDATE.getCode(), AuthResponseMessage.SUCCESS_REGENERATE_TOKEN.getMessage(), jwtToken));
    }

    @PostMapping("/signout/{provider}")
    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴 진행")
    public ResponseEntity<ResponseDto<Void>> signOut(@AuthenticationPrincipal User user, @RequestBody SignOutRequest signOutRequest, @PathVariable Provider provider) {
        SignOutUseCase signOutUseCase = selectOauth.selectSignOut(provider);
        signOutUseCase.signOut(user, signOutRequest);
        return ResponseEntity.ok(create(AuthResponseCode.AUTH_DELETE.getCode(), AuthResponseMessage.SIGNOUT_USER.getMessage()));
    }
}