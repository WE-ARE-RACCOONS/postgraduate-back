package com.postgraduate.domain.auth.presentation;

import com.postgraduate.domain.auth.application.dto.AuthUserResponse;
import com.postgraduate.domain.auth.application.usecase.jwt.JwtUseCase;
import com.postgraduate.domain.auth.application.usecase.kakao.KakaoSignInUseCase;
import com.postgraduate.domain.auth.application.dto.JwtTokenResponse;
import com.postgraduate.domain.auth.application.dto.KakaoLoginRequest;
import com.postgraduate.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
    @Operation(description = "카카오 로그인")
    public ResponseDto<JwtTokenResponse> getUserDetails(@RequestBody KakaoLoginRequest request, HttpServletResponse res) {
        AuthUserResponse authUser = kakaoSignInUseCase.getUser(request.getAccessToken());
        JwtTokenResponse jwtToken = jwtUseCase.signIn(authUser.getUser(), authUser.isNew());
        Cookie cookie = new Cookie("refreshToken", jwtToken.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60 * 24 * 30);
        res.addCookie(cookie);
        return ResponseDto.create(OK.value(), SUCCESS_AUTH_MESSAGE.getMessage(), jwtToken);
    }

    @PostMapping("/refresh")
    @Operation(description = "refreshToken 으로 accessToken 재발급")
    public ResponseDto<JwtTokenResponse> refresh(@Parameter(hidden = true) @CookieValue("refreshToken") String tokenInput) {
        JwtTokenResponse jwtToken = jwtUseCase.regenerateToken(tokenInput);
        return ResponseDto.create(OK.value(), SUCCESS_REGENERATE_TOKEN_MESSAGE.getMessage(), jwtToken);
    }
}
