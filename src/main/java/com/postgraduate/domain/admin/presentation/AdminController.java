package com.postgraduate.domain.admin.presentation;

import com.postgraduate.domain.admin.application.dto.req.SeniorStatusRequest;
import com.postgraduate.domain.admin.application.dto.res.*;
import com.postgraduate.domain.admin.application.usecase.MentoringManageByAdminUseCase;
import com.postgraduate.domain.admin.application.usecase.PaymentManageByAdminUseCase;
import com.postgraduate.domain.admin.application.usecase.SeniorManageByAdminUseCase;
import com.postgraduate.domain.admin.application.usecase.UserManageByAdminUseCase;
import com.postgraduate.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseCode.MENTORING_FIND;
import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseMessage.GET_MENTORING_LIST_INFO;
import static com.postgraduate.domain.senior.presentation.constant.SeniorResponseCode.SENIOR_FIND;
import static com.postgraduate.domain.senior.presentation.constant.SeniorResponseMessage.GET_CERTIFICATION;
import static com.postgraduate.domain.senior.presentation.constant.SeniorResponseMessage.GET_SENIOR_INFO;
import static com.postgraduate.domain.user.presentation.constant.UserResponseCode.USER_FIND;
import static com.postgraduate.domain.user.presentation.constant.UserResponseMessage.GET_USER_INFO;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Tag(name = "ADMIN Controller")
public class AdminController {
    private final SeniorManageByAdminUseCase seniorManageUseCase;
    private final UserManageByAdminUseCase userManageUseCase;
    private final MentoringManageByAdminUseCase mentoringManageUseCase;
    private final PaymentManageByAdminUseCase paymentManageUseCase;

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

    @PatchMapping("/certification/{seniorId}")
    @Operation(summary = "[관리자] 선배 프로필 승인 요청 응답", description = "선배 승인 신청한 유저를 승인 또는 거부합니다.")
    public ResponseDto updateSeniorStatus(@PathVariable Long seniorId, @RequestBody SeniorStatusRequest request) {
        seniorManageUseCase.updateSeniorStatus(seniorId, request);
        return ResponseDto.create(SENIOR_FIND.getCode(), GET_CERTIFICATION.getMessage()); //TODO: 메시지, 코드 수정
    }

    @GetMapping("/users")
    @Operation(summary = "[관리자] 후배 정보 목록", description = "대학생 후배 정보 목록을 조회합니다.")
    public ResponseDto<List<UserResponse>> getUsers() {
        List<UserResponse> users = userManageUseCase.getUsers();
        return ResponseDto.create(USER_FIND.getCode(), GET_USER_INFO.getMessage(), users);
    }

    @GetMapping("/seniors")
    @Operation(summary = "[관리자] 선배 정보 목록", description = "대학원생 선배 정보 목록을 조회합니다.")
    public ResponseDto<List<SeniorResponse>> getSeniors() {
        List<SeniorResponse> seniors = seniorManageUseCase.getSeniors();
        return ResponseDto.create(SENIOR_FIND.getCode(), GET_SENIOR_INFO.getMessage(), seniors);
    }

    @GetMapping("/mentorings")
    @Operation(summary = "[관리자] 매칭 정보 목록", description = "매칭 정보 목록을 조회합니다.")
    public ResponseDto<List<MentoringResponse>> getMentorings() {
        List<MentoringResponse> mentorings = mentoringManageUseCase.getMentorings();
        return ResponseDto.create(MENTORING_FIND.getCode(), GET_MENTORING_LIST_INFO.getMessage(), mentorings);
    }
}
