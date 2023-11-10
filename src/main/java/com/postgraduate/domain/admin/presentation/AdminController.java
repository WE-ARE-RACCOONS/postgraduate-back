package com.postgraduate.domain.admin.presentation;

import com.postgraduate.domain.admin.application.dto.res.CertificationDetailsResponse;
import com.postgraduate.domain.admin.application.dto.res.CertificationResponse;
import com.postgraduate.domain.admin.application.usecase.SeniorManageUseCase;
import com.postgraduate.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.postgraduate.domain.senior.presentation.constant.SeniorResponseCode.SENIOR_FIND;
import static com.postgraduate.domain.senior.presentation.constant.SeniorResponseMessage.GET_CERTIFICATION;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Tag(name = "ADMIN Controller")
public class AdminController {
    private final SeniorManageUseCase seniorManageUseCase;

    @GetMapping("/certification/{seniorId}")
    @Operation(summary = "[관리자] 선배 프로필 승인 요청 조회", description = "선배 신청 시 작성한 사전 작성정보 및 첨부사진을 조회합니다.")
    public ResponseDto<CertificationDetailsResponse> getCertificationDetails(@PathVariable Long seniorId) {
        CertificationDetailsResponse certification = seniorManageUseCase.getCertificationDetails(seniorId);
        return ResponseDto.create(SENIOR_FIND.getCode(), GET_CERTIFICATION.getMessage(), certification);
    }

    @GetMapping("/certification")
    @Operation(summary = "[관리자] 선배 프로필 승인 대기 목록", description = "선배 프로필 승인 신청한 유저 목록을 조회합니다.")
    public ResponseDto<List<CertificationResponse>> getCertifications() {
        List<CertificationResponse> certifications = seniorManageUseCase.getCertifications();
        return ResponseDto.create(SENIOR_FIND.getCode(), GET_CERTIFICATION.getMessage(), certifications); //TODO: 메시지, 코드 수정
    }

}
