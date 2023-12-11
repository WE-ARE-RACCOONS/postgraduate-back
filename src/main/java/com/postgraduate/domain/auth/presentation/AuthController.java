package com.postgraduate.domain.auth.presentation;

import com.postgraduate.domain.auth.application.dto.req.CodeRequest;
import com.postgraduate.domain.auth.application.dto.req.SeniorChangeRequest;
import com.postgraduate.domain.auth.application.dto.req.SeniorSignUpRequest;
import com.postgraduate.domain.auth.application.dto.req.SignUpRequest;
import com.postgraduate.domain.auth.application.dto.res.AuthUserResponse;
import com.postgraduate.domain.auth.application.dto.res.JwtTokenResponse;
import com.postgraduate.domain.auth.application.usecase.oauth.SelectOauth;
import com.postgraduate.domain.auth.application.usecase.SignUpUseCase;
import com.postgraduate.domain.auth.application.usecase.jwt.JwtUseCase;
import com.postgraduate.domain.auth.application.usecase.SignInUseCase;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.postgraduate.domain.auth.presentation.contant.AuthResponseCode.*;
import static com.postgraduate.domain.auth.presentation.contant.AuthResponseMessage.*;
import static com.postgraduate.domain.senior.presentation.constant.SeniorResponseCode.SENIOR_CREATE;
import static com.postgraduate.domain.senior.presentation.constant.SeniorResponseMessage.CREATE_SENIOR;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "AUTH Controller")
public class AuthController {
    private final SelectOauth selectOauth;
    private final SignUpUseCase signUpUseCase;
    private final JwtUseCase jwtUseCase;

    @PostMapping("/login/{provider}")
    @Operation(summary = "소셜 로그인", description = "회원인 경우 JWT를, 회원이 아닌 경우 socialId를 반환합니다(회원가입은 진행하지 않습니다).")
    public ResponseDto<?> authLogin(@RequestBody @Valid CodeRequest request, @PathVariable String provider) {
        SignInUseCase signInUseCase = selectOauth.selectStrategy(provider);
        AuthUserResponse authUser = signInUseCase.getUser(request);
        if (authUser.getUser().isEmpty())
            return ResponseDto.create(AUTH_NONE.getCode(), NOT_REGISTERED_USER.getMessage(), authUser);
        JwtTokenResponse jwtToken = jwtUseCase.signIn(authUser.getUser().get());
        return ResponseDto.create(AUTH_ALREADY.getCode(), SUCCESS_AUTH.getMessage(), jwtToken);
    }

//    @PostMapping("/logout")
//    @Operation(summary = "로그아웃", description = "토큰 같이 보내주세요")
//    public ResponseDto logout() {
//        return ResponseDto.create(AUTH_CREATE.getCode(), SUCCESS_AUTH.getMessage());
//    }

    @PostMapping("/user/signup")
    @Operation(summary = "대학생 회원가입", description = "로그인 API에서 반환한 socialId, 닉네임, 번호, 마케팅 수신여부, 희망 학과, 희망 분야, 매칭 희망 여부")
    public ResponseDto<JwtTokenResponse> signUpUser(@RequestBody @Valid SignUpRequest request) {
        User user = signUpUseCase.userSignUp(request);
        JwtTokenResponse jwtToken = jwtUseCase.signIn(user);
        return ResponseDto.create(AUTH_CREATE.getCode(), SUCCESS_AUTH.getMessage(), jwtToken);
    }

    @PostMapping("/senior/signup")
    @Operation(summary = "대학원생 가입 - 필수 과정만", description = "대학원생 회원가입 - 필수 과정만")
    public ResponseDto<JwtTokenResponse> singUpSenior(@RequestBody @Valid SeniorSignUpRequest request) {
        User user = signUpUseCase.seniorSignUp(request);
        JwtTokenResponse jwtToken = jwtUseCase.signIn(user);
        return ResponseDto.create(SENIOR_CREATE.getCode(), CREATE_SENIOR.getMessage(), jwtToken);
    }

    @PostMapping("/senior/change")
    @Operation(summary = "선배로 업데이트 | 토큰 필요", description = "대학생 대학원생으로 변경")
    public ResponseDto<JwtTokenResponse> changeSenior(@AuthenticationPrincipal User user,
                                                      @RequestBody @Valid SeniorChangeRequest changeRequest) {
        User changeUser = signUpUseCase.changeSenior(user, changeRequest);
        JwtTokenResponse jwtToken = jwtUseCase.signIn(changeUser);
        return ResponseDto.create(SENIOR_CREATE.getCode(), CREATE_SENIOR.getMessage(), jwtToken);
    }


    @PostMapping("/refresh")
    @Operation(summary = "토큰 재발급 | 토큰 필요", description = "refreshToken 으로 토큰 재발급")
    public ResponseDto<JwtTokenResponse> refresh(@AuthenticationPrincipal User user, HttpServletRequest request) {
        JwtTokenResponse jwtToken = jwtUseCase.regenerateToken(user, request);
        return ResponseDto.create(AUTH_UPDATE.getCode(), SUCCESS_REGENERATE_TOKEN.getMessage(), jwtToken);
    }
}
