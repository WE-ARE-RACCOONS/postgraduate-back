package com.postgraduate.domain.senior.presentation;

import com.postgraduate.domain.senior.application.dto.req.SeniorProfileRequest;
import com.postgraduate.domain.senior.application.dto.req.SeniorSignUpRequest;
import com.postgraduate.domain.senior.application.usecase.SeniorSignUpUseCase;
import com.postgraduate.domain.senior.application.usecase.SeniorUpdateUseCase;
import com.postgraduate.global.auth.AuthDetails;
import com.postgraduate.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.postgraduate.domain.senior.presentation.constant.SeniorResponseCode.SENIOR_CREATE;
import static com.postgraduate.domain.senior.presentation.constant.SeniorResponseCode.SENIOR_UPDATE;
import static com.postgraduate.domain.senior.presentation.constant.SeniorResponseMessage.SUCCESS_SENIOR_SIGN_UP_MESSAGE;
import static com.postgraduate.domain.senior.presentation.constant.SeniorResponseMessage.SUCCESS_UPDATE_PROFILE_MESSAGE;


@RestController
@RequiredArgsConstructor
@RequestMapping("/senior")
@Tag(name = "SENIOR Controller")
public class SeniorController {
    private final SeniorSignUpUseCase signUpUseCase;
    private final SeniorUpdateUseCase updateUseCase;

    @PostMapping("/signup")
    @Operation(summary = "대학원생 가입 - 필수 과정만", description = "대학원생 회원가입 - 필수 과정만")
    public ResponseDto singUpSenior(@AuthenticationPrincipal AuthDetails authDetails,
                                       @RequestBody SeniorSignUpRequest signUpRequest) {
        signUpUseCase.signUp(authDetails, signUpRequest);
        return ResponseDto.create(SENIOR_CREATE.getCode(), SUCCESS_SENIOR_SIGN_UP_MESSAGE.getMessage());
    }

    @PatchMapping("/profile")
    @Operation(summary = "대학원생 프로필 등록", description = "소개글, 추천대상, 오픈채팅방 링크, 가능 시간대, 소통시간")
    public ResponseDto singUpSenior(@AuthenticationPrincipal AuthDetails authDetails,
                                       @RequestBody SeniorProfileRequest profileRequest) {
        updateUseCase.updateProfile(authDetails, profileRequest);
        return ResponseDto.create(SENIOR_UPDATE.getCode(), SUCCESS_UPDATE_PROFILE_MESSAGE.getMessage());
    }
}
