package com.postgraduate.domain.auth.presentation;

import com.postgraduate.domain.auth.application.dto.AuthUserResponse;
import com.postgraduate.domain.auth.application.dto.req.SignUpRequest;
import com.postgraduate.domain.auth.application.usecase.jwt.JwtUseCase;
import com.postgraduate.domain.auth.application.usecase.kakao.KakaoSignInUseCase;
import com.postgraduate.domain.auth.application.dto.JwtTokenResponse;
import com.postgraduate.domain.auth.application.dto.req.KakaoLoginRequest;
import com.postgraduate.global.auth.AuthDetails;
import com.postgraduate.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.postgraduate.domain.auth.presentation.contant.AuthResponseMessage.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "AUTH Controller")
public class AuthController {
    private final KakaoSignInUseCase kakaoSignInUseCase;
    private final JwtUseCase jwtUseCase;

    @PostMapping("/login")
    @Operation(description = "카카오 로그인입니다. 회원인 경우 JWT를, 회원이 아닌 경우 socialId를 반환합니다(회원가입은 진행하지 않습니다).")
    public ResponseDto<?> getUserDetails(@RequestBody KakaoLoginRequest request) {
        AuthUserResponse authUser = kakaoSignInUseCase.getUser(request.getAccessToken());
        if (authUser.getSocialId() != null) {
            return ResponseDto.create(NOT_FOUND.value(), NOT_REGISTERED_USER_MESSAGE.getMessage(), authUser);
        }
        JwtTokenResponse jwtToken = jwtUseCase.signIn(authUser.getUser());
        return ResponseDto.create(OK.value(), SUCCESS_AUTH_MESSAGE.getMessage(), jwtToken);
    }

    @PostMapping("/signup")
    @Operation(description = "회원가입입니다. 로그인 API에서 반환한 socialId와 닉네임을 함께 보내주세요.")
    public ResponseDto<JwtTokenResponse> getUserDetails(@RequestBody SignUpRequest request) {
        AuthUserResponse authUser = kakaoSignInUseCase.signUp(request);
        JwtTokenResponse jwtToken = jwtUseCase.signIn(authUser.getUser());
        return ResponseDto.create(OK.value(), SUCCESS_AUTH_MESSAGE.getMessage(), jwtToken);
    }

    @PostMapping("/refresh")
    @Operation(description = "refreshToken 으로 accessToken 재발급")
    public ResponseDto<JwtTokenResponse> refresh(@AuthenticationPrincipal AuthDetails authDetails) {
        Long userId = authDetails.getUserId();
        String refreshToken = "";/*Redis에서 userId로 refreshToken 확인*/
        JwtTokenResponse jwtToken = jwtUseCase.regenerateToken(refreshToken);
        return ResponseDto.create(OK.value(), SUCCESS_REGENERATE_TOKEN_MESSAGE.getMessage(), jwtToken);
    }
}
