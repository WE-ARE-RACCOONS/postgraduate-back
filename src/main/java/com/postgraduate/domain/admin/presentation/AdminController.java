package com.postgraduate.domain.admin.presentation;

import com.postgraduate.domain.admin.application.dto.req.SeniorStatusRequest;
import com.postgraduate.domain.admin.application.dto.res.*;
import com.postgraduate.domain.admin.application.usecase.*;
import com.postgraduate.domain.wish.application.mapper.dto.res.WishResponse;
import com.postgraduate.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.postgraduate.domain.admin.presentation.constant.AdminResponseCode.ADMIN_FIND;
import static com.postgraduate.domain.admin.presentation.constant.AdminResponseCode.ADMIN_UPDATE;
import static com.postgraduate.domain.admin.presentation.constant.AdminResponseMessage.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Tag(name = "ADMIN Controller", description = "관리자의 모든 API는 토큰이 필요합니다.")
public class AdminController {
    private final SeniorManageByAdminUseCase seniorManageUseCase;
    private final UserManageByAdminUseCase userManageUseCase;
    private final MentoringManageByAdminUseCase mentoringManageUseCase;
    private final PaymentManageByAdminUseCase paymentManageUseCase;
    private final SalaryManageByAdminUseCase salaryManageUseCase;

    @GetMapping("/certification/{seniorId}")
    @Operation(summary = "[관리자] 선배 프로필 승인 요청 조회", description = "선배 신청 시 작성한 사전 작성정보 및 첨부사진을 조회합니다.")
    public ResponseDto<CertificationDetailsResponse> getCertificationDetails(@PathVariable Long seniorId) {
        CertificationDetailsResponse certification = seniorManageUseCase.getCertificationDetails(seniorId);
        return ResponseDto.create(ADMIN_FIND.getCode(), GET_DETAILS.getMessage(), certification);
    }

//    @GetMapping("/certification")
//    @Operation(summary = "[관리자] 선배 프로필 승인 대기 목록", description = "선배 프로필 승인 신청한 유저 목록을 조회합니다.")
//    public ResponseDto<List<CertificationResponse>> getCertifications() {
//        List<CertificationResponse> certifications = seniorManageUseCase.getCertifications();
//        return ResponseDto.create(SENIOR_FIND.getCode(), GET_SENIOR_LIST_INFO.getMessage(), certifications);
//    }

    @PatchMapping("/certification/{seniorId}")
    @Operation(summary = "[관리자] 선배 프로필 승인 요청 응답", description = "선배 승인 신청한 유저를 승인 또는 거부합니다.")
    public ResponseDto updateSeniorStatus(@PathVariable Long seniorId,
                                          @RequestBody @Valid SeniorStatusRequest request) {
        seniorManageUseCase.updateSeniorStatus(seniorId, request);
        return ResponseDto.create(ADMIN_UPDATE.getCode(), UPDATE_SENIOR_STATUS.getMessage());
    }

    @GetMapping("/users")
    @Operation(summary = "[관리자] 후배 정보 목록", description = "대학생 후배 정보 목록을 조회합니다.")
    public ResponseDto<UserManageResponse> getUsers(@RequestParam(required = false) Integer page) {
        UserManageResponse users = userManageUseCase.getUsers(page);
        return ResponseDto.create(ADMIN_FIND.getCode(), GET_LIST.getMessage(), users);
    }

    @GetMapping("/wish/{wishId}")
    @Operation(summary = "[관리자] 후배 매칭 지원 정보", description = "대학생 후배 매칭 지원 정보를 상세 조회합니다.")
    public ResponseDto<WishResponse> getWish(@PathVariable Long wishId) {
        WishResponse wish = userManageUseCase.getWish(wishId);
        return ResponseDto.create(ADMIN_FIND.getCode(), GET_DETAILS.getMessage(), wish);
    }

    @GetMapping("/seniors")
    @Operation(summary = "[관리자] 선배 정보 목록", description = "대학원생 선배 정보 목록을 조회합니다.")
    public ResponseDto<SeniorManageResponse> getSeniors(@RequestParam(required = false) Integer page,
                                                        @RequestParam(required = false) String search) {
        SeniorManageResponse seniors = seniorManageUseCase.getSeniors(page, search);
        return ResponseDto.create(ADMIN_FIND.getCode(), GET_LIST.getMessage(), seniors);
    }

    @GetMapping("/salary")
    @Operation(summary = "[관리자] 정산 목록 조회", description = "한 달 기준으로 정산 목록을 조회합니다. 기준일은 [11일 ~ 내월 10일]입니다.")
    public ResponseDto<SalaryManageResponse> getSalaries(@RequestParam(required = false) Integer page) {
        SalaryManageResponse salaries = salaryManageUseCase.getSalaries(page);
        return ResponseDto.create(ADMIN_FIND.getCode(), GET_LIST.getMessage(), salaries);
    }

    @GetMapping("/salary/{seniorId}")
    @Operation(summary = "[관리자] 선배 정산 상세 정보", description = "대학원생 선배 정산 상세 정보를 조회합니다.")
    public ResponseDto<SalaryDetailsResponse> getSalary(@PathVariable Long seniorId) {
        SalaryDetailsResponse salary = salaryManageUseCase.getSalary(seniorId);
        return ResponseDto.create(ADMIN_FIND.getCode(), GET_DETAILS.getMessage(), salary);
    }

    @PatchMapping("/salary/{seniorId}")
    @Operation(summary = "[관리자] 정산 상태 변경", description = "대학원생 선배 정산 상태를 변경합니다.")
    public ResponseDto updateSalaryStatus(@PathVariable Long seniorId, @RequestParam Boolean status) {
        salaryManageUseCase.updateSalaryStatus(seniorId, status);
        return ResponseDto.create(ADMIN_UPDATE.getCode(), UPDATE_SALARY_STATUS.getMessage());
    }

    @GetMapping("/user/{userId}/mentoring")
    @Operation(summary = "[관리자] 후배 멘토링 조회", description = "후배의 멘토링 목록을 조회합니다.")
    public ResponseDto<MentoringManageResponse> getUserMentorings(@PathVariable Long userId) {
        MentoringManageResponse mentorings = mentoringManageUseCase.getUserMentorings(userId);
        return ResponseDto.create(ADMIN_FIND.getCode(), GET_LIST.getMessage(), mentorings);
    }

    @GetMapping("/senior/{seniorId}/mentoring")
    @Operation(summary = "[관리자] 선배 멘토링 조회", description = "선배의 멘토링 목록을 조회합니다.")
    public ResponseDto<MentoringManageResponse> getSeniorMentorings(@PathVariable Long seniorId) {
        MentoringManageResponse mentorings = mentoringManageUseCase.getSeniorMentorings(seniorId);
        return ResponseDto.create(ADMIN_FIND.getCode(), GET_LIST.getMessage(), mentorings);
    }

    @GetMapping("/payments")
    @Operation(summary = "[관리자] 결제 정보 목록", description = "결제 정보 목록을 조회합니다.")
    public ResponseDto<PaymentManageResponse> getPayments(@RequestParam(required = false) Integer page) {
        PaymentManageResponse payments = paymentManageUseCase.getPayments(page);
        return ResponseDto.create(ADMIN_FIND.getCode(), GET_LIST.getMessage(), payments);
    }

    @GetMapping("/payments/{mentoringId}")
    @Operation(summary = "[관리자] 결제된 멘토링 정보", description = "결제된 멘토링 정보를 조회합니다.")
    public ResponseDto<MentoringWithPaymentResponse> getPayments(@PathVariable Long mentoringId) {
        MentoringWithPaymentResponse mentoringWithPayment = mentoringManageUseCase.getMentoringWithPayment(mentoringId);
        return ResponseDto.create(ADMIN_FIND.getCode(), GET_DETAILS.getMessage(), mentoringWithPayment);
    }
}
