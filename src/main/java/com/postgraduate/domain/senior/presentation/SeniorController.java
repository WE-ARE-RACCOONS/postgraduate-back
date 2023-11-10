package com.postgraduate.domain.senior.presentation;

import com.postgraduate.domain.senior.application.dto.req.SeniorCertificationRequest;
import com.postgraduate.domain.senior.application.dto.req.SeniorProfileAndAccountRequest;
import com.postgraduate.domain.senior.application.dto.req.SeniorProfileRequest;
import com.postgraduate.domain.senior.application.dto.req.SeniorSignUpRequest;
import com.postgraduate.domain.senior.application.dto.res.SeniorInfoResponse;
import com.postgraduate.domain.senior.application.dto.res.SeniorProfileResponse;
import com.postgraduate.domain.senior.application.usecase.SeniorMyPageUseCase;
import com.postgraduate.domain.senior.application.usecase.SeniorSignUpUseCase;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.postgraduate.domain.senior.presentation.constant.SeniorResponseCode.*;
import static com.postgraduate.domain.senior.presentation.constant.SeniorResponseMessage.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/senior")
@Tag(name = "SENIOR Controller")
public class SeniorController {
    private final SeniorSignUpUseCase signUpUseCase;
    private final SeniorMyPageUseCase myPageUseCase;

    @PostMapping("/signup")
    @Operation(summary = "대학원생 가입 - 필수 과정만", description = "대학원생 회원가입 - 필수 과정만")
    public ResponseDto singUpSenior(@AuthenticationPrincipal User user,
                                    @RequestBody SeniorSignUpRequest signUpRequest) {
        signUpUseCase.signUp(user, signUpRequest);
        return ResponseDto.create(SENIOR_CREATE.getCode(), CREATE_SENIOR.getMessage());
    }

    @PatchMapping("/profile")
    @Operation(summary = "대학원생 프로필 등록")
    public ResponseDto singUpSenior(@AuthenticationPrincipal User user,
                                    @RequestBody SeniorProfileRequest profileRequest) {
        signUpUseCase.updateProfile(user, profileRequest);
        return ResponseDto.create(SENIOR_UPDATE.getCode(), UPDATE_PROFILE.getMessage());
    }

    @PatchMapping("/certification")
    @Operation(summary = "대학원생 인증", description = "이미지 업로드 이후 url 담아서 요청")
    public ResponseDto updateCertification(@AuthenticationPrincipal User user,
                                           @RequestBody SeniorCertificationRequest certificationRequest) {
        myPageUseCase.updateCertification(user, certificationRequest);
        return ResponseDto.create(SENIOR_UPDATE.getCode(), UPDATE_CERTIFICATION.getMessage());
    }

    @GetMapping("/me")
    @Operation(summary = "대학원생 마이페이지 기본 정보", description = "닉네임, 프로필 사진, 인증 여부")
    public ResponseDto<SeniorInfoResponse> getSeniorInfo(@AuthenticationPrincipal User user) {
        SeniorInfoResponse seniorInfoResponse = myPageUseCase.seniorInfo(user);
        return ResponseDto.create(SENIOR_FIND.getCode(), GET_SENIOR_INFO.getMessage(), seniorInfoResponse);
    }

    @GetMapping("/me/profile")
    @Operation(summary = "대학원생 마이페이지 프로필 보기")
    public ResponseDto<SeniorProfileResponse> getSeniorProfile(@AuthenticationPrincipal User user) {
        SeniorProfileResponse seniorProfile = myPageUseCase.getSeniorProfile(user);
        return ResponseDto.create(SENIOR_FIND.getCode(), GET_SENIOR_PROFILE.getMessage(), seniorProfile);
    }

    @PatchMapping("/me/profile")
    @Operation(summary = "대학원생 프로필 수정")
    public ResponseDto updateProfile(@AuthenticationPrincipal User user,
                                    @RequestBody SeniorProfileAndAccountRequest profileAndAccountPageRequest) {
        myPageUseCase.updateProfile(user, profileAndAccountPageRequest);
        return ResponseDto.create(SENIOR_UPDATE.getCode(), UPDATE_PROFILE.getMessage());
    }

}
