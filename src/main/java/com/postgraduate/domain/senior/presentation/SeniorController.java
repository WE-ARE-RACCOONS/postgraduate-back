package com.postgraduate.domain.senior.presentation;

import com.postgraduate.domain.senior.application.dto.req.SeniorSignUpRequest;
import com.postgraduate.domain.senior.application.usecase.SeniorSignUpUseCase;
import com.postgraduate.domain.senior.presentation.constant.SeniorResponseMessage;
import com.postgraduate.global.auth.AuthDetails;
import com.postgraduate.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/senior")
@Tag(name = "SENIOR Controller")
public class SeniorController {
    private final SeniorSignUpUseCase signUpUseCase;

    @PostMapping("/signup")
    @Operation(summary = "대학원생 가입", description = "대학원생 회원가입")
    public ResponseDto singUpSenior(@AuthenticationPrincipal AuthDetails authDetails,
                                       @RequestBody SeniorSignUpRequest request) {
        signUpUseCase.signUp(authDetails, request);
        return ResponseDto.create(HttpStatus.OK.value(), SeniorResponseMessage.SUCCESS_SENIOR_SIGN_UP_MESSAGE.getMessage());
    }
}
