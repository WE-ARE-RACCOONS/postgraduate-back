package com.postgraduate.domain.admin.presentation;

import com.postgraduate.domain.admin.application.dto.req.SeniorStatusRequest;
import com.postgraduate.domain.admin.application.dto.res.*;
import com.postgraduate.domain.admin.application.usecase.*;
import com.postgraduate.domain.wish.application.mapper.dto.res.WishResponse;
import com.postgraduate.domain.wish.application.usecase.WishInfoUseCase;
import com.postgraduate.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseCode.MENTORING_FIND;
import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseMessage.*;
import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseMessage.GET_MENTORING_LIST_INFO;
import static com.postgraduate.domain.payment.presentation.constant.PaymentResponseCode.PAYMENT_FIND;
import static com.postgraduate.domain.payment.presentation.constant.PaymentResponseMessage.GET_PAYMENT_LIST_INFO;
import static com.postgraduate.domain.salary.presentation.constant.SalaryResponseCode.SALARY_FIND;
import static com.postgraduate.domain.salary.presentation.constant.SalaryResponseCode.SALARY_UPDATE;
import static com.postgraduate.domain.salary.presentation.constant.SalaryResponseMessage.GET_SALARY_INFO;
import static com.postgraduate.domain.salary.presentation.constant.SalaryResponseMessage.UPDATE_SALARY_STATUS;
import static com.postgraduate.domain.senior.presentation.constant.SeniorResponseCode.SENIOR_FIND;
import static com.postgraduate.domain.senior.presentation.constant.SeniorResponseCode.SENIOR_UPDATE;
import static com.postgraduate.domain.senior.presentation.constant.SeniorResponseMessage.*;
import static com.postgraduate.domain.user.presentation.constant.UserResponseCode.USER_FIND;
import static com.postgraduate.domain.user.presentation.constant.UserResponseMessage.GET_USER_LIST_INFO;
import static com.postgraduate.domain.wish.presentation.constant.WishResponseCode.WISH_FIND;
import static com.postgraduate.domain.wish.presentation.constant.WishResponseMessage.GET_WISH_INFO;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Tag(name = "ADMIN Controller")
public class AdminController {
    private final SeniorManageByAdminUseCase seniorManageUseCase;
    private final UserManageByAdminUseCase userManageUseCase;
    private final MentoringManageByAdminUseCase mentoringManageUseCase;
    private final PaymentManageByAdminUseCase paymentManageUseCase;
    private final WishInfoUseCase wishInfoUseCase;
    private final SalaryManageByAdminUseCase salaryManageUseCase;

    @GetMapping("/certification/{seniorId}")
    @Operation(summary = "[관리자] 선배 프로필 승인 요청 조회", description = "선배 신청 시 작성한 사전 작성정보 및 첨부사진을 조회합니다.")
    public ResponseDto<CertificationDetailsResponse> getCertificationDetails(@PathVariable Long seniorId) {
        CertificationDetailsResponse certification = seniorManageUseCase.getCertificationDetails(seniorId);
        return ResponseDto.create(SENIOR_FIND.getCode(), GET_SENIOR_PROFILE.getMessage(), certification);
    }

//    @GetMapping("/certification")
//    @Operation(summary = "[관리자] 선배 프로필 승인 대기 목록", description = "선배 프로필 승인 신청한 유저 목록을 조회합니다.")
//    public ResponseDto<List<CertificationResponse>> getCertifications() {
//        List<CertificationResponse> certifications = seniorManageUseCase.getCertifications();
//        return ResponseDto.create(SENIOR_FIND.getCode(), GET_SENIOR_LIST_INFO.getMessage(), certifications);
//    }

    @PatchMapping("/certification/{seniorId}")
    @Operation(summary = "[관리자] 선배 프로필 승인 요청 응답", description = "선배 승인 신청한 유저를 승인 또는 거부합니다.")
    public ResponseDto updateSeniorStatus(@PathVariable Long seniorId, @RequestBody SeniorStatusRequest request) {
        seniorManageUseCase.updateSeniorStatus(seniorId, request);
        return ResponseDto.create(SENIOR_UPDATE.getCode(), UPDATE_STATUS.getMessage());
    }

    @GetMapping("/users")
    @Operation(summary = "[관리자] 후배 정보 목록", description = "대학생 후배 정보 목록을 조회합니다.")
    public ResponseDto<List<UserResponse>> getUsers() {
        List<UserResponse> users = userManageUseCase.getUsers();
        return ResponseDto.create(USER_FIND.getCode(), GET_USER_LIST_INFO.getMessage(), users);
    }

    @GetMapping("/wish/{wishId}")
    @Operation(summary = "[관리자] 후배 매칭 지원 정보", description = "대학생 후배 매칭 지원 정보를 상세 조회합니다.")
    public ResponseDto<WishResponse> getWish(@PathVariable Long wishId) {
        WishResponse wish = wishInfoUseCase.getWish(wishId);
        return ResponseDto.create(WISH_FIND.getCode(), GET_WISH_INFO.getMessage(), wish);
    }

    @GetMapping("/seniors")
    @Operation(summary = "[관리자] 선배 정보 목록", description = "대학원생 선배 정보 목록을 조회합니다.")
    public ResponseDto<List<SeniorResponse>> getSeniors() {
        List<SeniorResponse> seniors = seniorManageUseCase.getSeniors();
        return ResponseDto.create(SENIOR_FIND.getCode(), GET_SENIOR_LIST_INFO.getMessage(), seniors);
    }

    @GetMapping("/salary")
    @Operation(summary = "[관리자] 정산 목록 조회", description = "한 달 기준으로 정산 목록을 조회합니다. 기준일은 [11일 ~ 내월 10일]입니다.")
    public ResponseDto<AllSalariesResponse> getSalaries() {
        AllSalariesResponse salaries = salaryManageUseCase.getSalaries();
        return ResponseDto.create(SALARY_FIND.getCode(), GET_SALARY_INFO.getMessage(), salaries);
    }

    @GetMapping("/salary/{seniorId}")
    @Operation(summary = "[관리자] 선배 정산 상세 정보", description = "대학원생 선배 정산 상세 정보를 조회합니다.")
    public ResponseDto<SalaryDetailsResponse> getSalary(@PathVariable Long seniorId) {
        SalaryDetailsResponse salary = salaryManageUseCase.getSalary(seniorId);
        return ResponseDto.create(SALARY_FIND.getCode(), GET_SALARY_INFO.getMessage(), salary);
    }

    @PatchMapping("/salary/{seniorId}")
    @Operation(summary = "[관리자] 정산 상태 변경", description = "대학원생 선배 정산 상태를 변경합니다.")
    public ResponseDto updateSalaryStatus(@PathVariable Long seniorId, @RequestParam Boolean status) {
        salaryManageUseCase.updateSalaryStatus(seniorId, status);
        return ResponseDto.create(SALARY_UPDATE.getCode(), UPDATE_SALARY_STATUS.getMessage());
    }

    @GetMapping("/user/{userId}/mentoring")
    @Operation(summary = "[관리자] 후배 멘토링 조회", description = "후배의 멘토링 목록을 조회합니다.")
    public ResponseDto<List<MentoringResponse>> getUserMentorings(@PathVariable Long userId) {
        List<MentoringResponse> mentorings = mentoringManageUseCase.getUserMentorings(userId);
        return ResponseDto.create(MENTORING_FIND.getCode(), GET_MENTORING_LIST_INFO.getMessage(), mentorings);
    }

    @GetMapping("/senior/{seniorId}/mentoring")
    @Operation(summary = "[관리자] 선배 멘토링 조회", description = "선배의 멘토링 목록을 조회합니다.")
    public ResponseDto<List<MentoringResponse>> getSeniorMentorings(@PathVariable Long seniorId) {
        List<MentoringResponse> mentorings = mentoringManageUseCase.getSeniorMentorings(seniorId);
        return ResponseDto.create(MENTORING_FIND.getCode(), GET_MENTORING_LIST_INFO.getMessage(), mentorings);
    }

    @GetMapping("/payments")
    @Operation(summary = "[관리자] 결제 정보 목록", description = "결제 정보 목록을 조회합니다.")
    public ResponseDto<List<PaymentResponse>> getPayments() {
        List<PaymentResponse> payments = paymentManageUseCase.getPayments();
        return ResponseDto.create(PAYMENT_FIND.getCode(), GET_PAYMENT_LIST_INFO.getMessage(), payments);
    }

    @GetMapping("/payments/{mentoringId}")
    @Operation(summary = "[관리자] 결제된 멘토링 정보", description = "결제된 멘토링 정보를 조회합니다.")
    public ResponseDto<MentoringWithPaymentResponse> getPayments(@PathVariable Long mentoringId) {
        MentoringWithPaymentResponse mentoringWithPayment = mentoringManageUseCase.getMentoringWithPayment(mentoringId);
        return ResponseDto.create(MENTORING_FIND.getCode(), GET_MENTORING_DETAIL_INFO.getMessage(), mentoringWithPayment);
    }
}
