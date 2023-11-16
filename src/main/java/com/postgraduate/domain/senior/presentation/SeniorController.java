package com.postgraduate.domain.senior.presentation;

import com.postgraduate.domain.senior.application.dto.req.SeniorCertificationRequest;
import com.postgraduate.domain.senior.application.dto.req.SeniorProfileRequest;
import com.postgraduate.domain.senior.application.dto.res.SeniorInfoResponse;
import com.postgraduate.domain.senior.application.usecase.SeniorMyPageUseCase;
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
    private final SeniorMyPageUseCase seniorMyPageUseCase;

    @PatchMapping("/me/certification")
    @Operation(summary = "대학원생 인증", description = "이미지 업로드 이후 url 담아서 요청")
    public ResponseDto updateCertification(@AuthenticationPrincipal User user,
                                           @RequestBody SeniorCertificationRequest certificationRequest) {
        seniorMyPageUseCase.updateCertification(user, certificationRequest);
        return ResponseDto.create(SENIOR_UPDATE.getCode(), UPDATE_CERTIFICATION.getMessage());
    }

    @PatchMapping("/me/profile")
    @Operation(summary = "대학원생 프로필 등록")
    public ResponseDto singUpSenior(@AuthenticationPrincipal User user,
                                    @RequestBody SeniorProfileRequest profileRequest) {
        seniorMyPageUseCase.updateProfile(user, profileRequest);
        return ResponseDto.create(SENIOR_UPDATE.getCode(), UPDATE_PROFILE.getMessage());
    }

    @GetMapping("/me")
    @Operation(summary = "대학원생 마이페이지 기본 정보", description = "닉네임, 프로필 사진, 인증 여부")
    public ResponseDto<SeniorInfoResponse> getSeniorInfo(@AuthenticationPrincipal User user) {
        SeniorInfoResponse seniorInfoResponse = seniorMyPageUseCase.seniorInfo(user);
        return ResponseDto.create(SENIOR_FIND.getCode(), GET_SENIOR_INFO.getMessage(), seniorInfoResponse);
    }

//    @GetMapping("/me/profile")
//    @Operation(summary = "대학원생 마이페이지 프로필 보기")
//    public ResponseDto<SeniorProfileResponse> getSeniorProfile(@AuthenticationPrincipal User user) {
//        SeniorProfileResponse seniorProfile = myPageUseCase.getSeniorProfile(user);
//        return ResponseDto.create(SENIOR_FIND.getCode(), GET_SENIOR_PROFILE.getMessage(), seniorProfile);
//    }
}
