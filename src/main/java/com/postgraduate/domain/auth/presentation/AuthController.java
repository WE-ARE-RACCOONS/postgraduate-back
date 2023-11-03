package com.postgraduate.domain.auth.presentation;

import com.postgraduate.domain.auth.application.dto.req.KakaoLoginRequest;
import com.postgraduate.domain.auth.application.dto.req.SignUpRequest;
import com.postgraduate.domain.auth.application.dto.res.AuthUserResponse;
import com.postgraduate.domain.auth.application.dto.res.JwtTokenResponse;
import com.postgraduate.domain.auth.application.usecase.jwt.JwtUseCase;
import com.postgraduate.domain.auth.application.usecase.kakao.KakaoSignInUseCase;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.global.auth.AuthDetails;
import com.postgraduate.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.postgraduate.domain.auth.presentation.contant.AuthResponseCode.*;
import static com.postgraduate.domain.auth.presentation.contant.AuthResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "AUTH Controller")
public class AuthController {
    private final KakaoSignInUseCase kakaoSignInUseCase;
    private final JwtUseCase jwtUseCase;

    @PostMapping("/login")
    @Operation(summary = "카카오 로그인", description = "회원인 경우 JWT를, 회원이 아닌 경우 socialId를 반환합니다(회원가입은 진행하지 않습니다).")
    public ResponseDto<?> authLogin(@RequestBody KakaoLoginRequest request) {
        AuthUserResponse authUser = kakaoSignInUseCase.getUser(request.getAccessToken());
        if (authUser.getUser().isEmpty())
            return ResponseDto.create(AUTH_NONE.getCode(), NOT_REGISTERED_USER_MESSAGE.getMessage(), authUser);

        JwtTokenResponse jwtToken = jwtUseCase.signIn(authUser.getUser().get());
        return ResponseDto.create(AUTH_ALREADY.getCode(), SUCCESS_AUTH_MESSAGE.getMessage(), jwtToken);
    }

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "로그인 API에서 반환한 socialId와 닉네임을 함께 보내주세요.")
    public ResponseDto<JwtTokenResponse> signUpUser(@RequestBody SignUpRequest request) {
        User user = kakaoSignInUseCase.signUp(request);
        JwtTokenResponse jwtToken = jwtUseCase.signIn(user);
        return ResponseDto.create(AUTH_CREATE.getCode(), SUCCESS_AUTH_MESSAGE.getMessage(), jwtToken);
    }

    @PostMapping("/refresh")
    @Operation(summary = "토큰 재발급", description = "refreshToken 으로 토큰 재발급")
    public ResponseDto<JwtTokenResponse> refresh(@AuthenticationPrincipal AuthDetails authDetails, HttpServletRequest request) {
        JwtTokenResponse jwtToken = jwtUseCase.regenerateToken(authDetails, request);
        return ResponseDto.create(AUTH_UPDATE.getCode(), SUCCESS_REGENERATE_TOKEN_MESSAGE.getMessage(), jwtToken);
    }
}
