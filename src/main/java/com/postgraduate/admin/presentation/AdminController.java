package com.postgraduate.admin.presentation;

import com.postgraduate.admin.application.dto.res.*;
import com.postgraduate.admin.application.usecase.*;
import com.postgraduate.domain.member.user.domain.entity.User;
import com.postgraduate.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
@Tag(name = "ADMIN Controller", description = "관리자의 모든 API는 관리자 권한의 토큰이 필요합니다.")
public class AdminController {
    private final AdminSeniorUseCase adminSeniorUseCase;
    private final AdminUserUseCase adminUserUseCase;
    private final AdminMentoringUseCase adminMentoringUseCase;
    private final AdminSalaryUseCase adminSalaryUseCase;
    private final AdminPaymentUseCase adminPaymentUseCase;
    private final AdminWishUseCase adminWishUseCase;
    private final AdminBatchUseCase adminBatchUseCase;

// todo : 관리자페이지용 로그인이 필요한지 고민 필요

    @GetMapping("/senior")
    @Operation(summary = "대학원생에 대한 정보 노출", description = "닉네임, 전화번호, 인증여부, 총 정산금액, 마케팅동의 여부, 후배 여부")
    public ResponseEntity<ResponseDto<SeniorInfos>> seniorInfo() {
        SeniorInfos seniorInfos = adminSeniorUseCase.allSenior();
        return ResponseEntity.ok(ResponseDto.create("","", seniorInfos));
    }

    @GetMapping("/senior/certification/{seniorId}")
    @Operation(summary = "대학원생 인증 상세정보", description = "이미지, 닉네임, 핸드폰번호, 가입일시, 대학원, 전공, 분야, 연구실, 교수님, 키워드")
    public ResponseEntity<ResponseDto<CertificationDetailsResponse>> certification(@PathVariable Long seniorId) {
        CertificationDetailsResponse certification = adminSeniorUseCase.getCertification(seniorId);
        return ResponseEntity.ok(ResponseDto.create("", "", certification));
    }

    @PostMapping("/senior/certification/{seniorId}/refuse")
    @Operation(summary = "대학원생 인증 반려", description = "인증 반려")
    public ResponseEntity<ResponseDto<Void>> certificationRefuse(@PathVariable Long seniorId) {
        adminSeniorUseCase.updateNotApprove(seniorId);
        return ResponseEntity.ok(ResponseDto.create("", ""));
    }

    @PostMapping("/senior/certification/{seniorId}/approve")
    @Operation(summary = "대학원생 인증 승인", description = "인증 승인")
    public ResponseEntity<ResponseDto<Void>> certificationApprove(@PathVariable Long seniorId) {
        adminSeniorUseCase.updateApprove(seniorId);
        return ResponseEntity.ok(ResponseDto.create("", ""));
    }

    @GetMapping("/senior/mentoring/{seniorId}")
    @Operation(summary = "대학원생 멘토링 정보", description = "닉네임, 핸드폰번호 (기본정보), 신청받은 멘토링 리스트(멘토링 진행 상태, 후배 닉네임, 후배 핸드폰번호, 신청일시, 멘토링 진행 일시")
    public ResponseEntity<ResponseDto<MentoringManageResponse>> seniorMentoringInfo(@PathVariable Long seniorId) {
        MentoringManageResponse mentoringInfos = adminMentoringUseCase.seniorMentorings(seniorId);
        return ResponseEntity.ok(ResponseDto.create("", "", mentoringInfos));
    }

    // senior 정보 처리 완료

    @PostMapping("/mentoring/refund/{mentoringId}")
    @Operation(summary = "멘토링 환불", description = "멘토링 환불")
    public ResponseEntity<ResponseDto<Void>> refundMentoring(@AuthenticationPrincipal User user, @PathVariable Long mentoringId) {
        adminMentoringUseCase.refundMentoring(user, mentoringId);
        return ResponseEntity.ok(ResponseDto.create("",""));
    }

    // mentoring 전용

    @GetMapping("/salary") //정산 관리
    @Operation(summary = "정산완료 확인", description = "정산별 닉네임, 핸드폰번호, 총금액, 예금주, 은행, 계좌번호, 완료일")
    public ResponseEntity<ResponseDto<SalaryInfos>> salaryInfo() {
        SalaryInfos salaryInfos = adminSalaryUseCase.salaryInfos();
        return ResponseEntity.ok(ResponseDto.create("","",salaryInfos));
    }

    @GetMapping("/salary/unsettled") //미정산건 관리
    @Operation(summary = "정산미완료 확인", description = "정산별 닉네임, 핸드폰번호, 총금액, 예금주, 은행, 계좌번호, 정산예정일")
    public ResponseEntity<ResponseDto<UnsettledSalaryInfos>> unsettledSalaryInfo() {
        UnsettledSalaryInfos unSettledSalaryInfos = adminSalaryUseCase.unSettledSalaryInfo();
        return ResponseEntity.ok(ResponseDto.create("","", unSettledSalaryInfos));
    }

    @PostMapping("/salary/done/{salaryId}") //미정산건 완료 처리
    @Operation(summary = "정산 완료 처리", description = "정산 완료 처리")
    public ResponseEntity<ResponseDto<Void>> salaryDone(@PathVariable Long salaryId) {
        adminSalaryUseCase.salaryDone(salaryId);
        return ResponseEntity.ok(ResponseDto.create("",""));
    }

    // 정산/미정산 관련

    @GetMapping("/junior")
    @Operation(summary = "후배 정보 확인", description = "후배별 닉네임, 핸드폰번호, 가입일, 마케팅 동의 여부, 선배 여부")
    public ResponseEntity<ResponseDto<UserInfos>> userInfo() {
        UserInfos userInfos = adminUserUseCase.userInfos();
        return ResponseEntity.ok(ResponseDto.create("","", userInfos));
    }

    @GetMapping("/junior/mentoring/{userId}")
    @Operation(summary = "후배 멘토링 정보 확인", description = "닉네임, 핸드폰번호 (기본정보), 신청한 멘토링 리스트(멘토링 진행 상태, 선배 닉네임, 선배 핸드폰번호, 신청일시, 멘토링 진행 일시")    public ResponseEntity<ResponseDto<MentoringManageResponse>> userMentoring(@PathVariable Long userId) {
        MentoringManageResponse mentoringInfos = adminMentoringUseCase.userMentoringInfos(userId);
        return ResponseEntity.ok(ResponseDto.create("","", mentoringInfos));
    }

    // 후배 관련

    @GetMapping("/payment")
    @Operation(summary = "결제 정보 확인", description = "멘토링ID, 후배 닉네임, 핸드폰번호, 결제일, 금액, 결제상태")
    public ResponseEntity<ResponseDto<PaymentInfos>> paymentInfo() {
        PaymentInfos paymentInfos = adminPaymentUseCase.paymentInfos();
        return ResponseEntity.ok(ResponseDto.create("","",paymentInfos));
    }

    @GetMapping("/payment/mentoring/{paymentId}")
    @Operation(summary = "결제 멘토링 확인", description = "결제ID, 후배닉네임, 후배핸드폰, 선배닉네임, 선배핸드폰, 멘토링날짜, 몇분짜리, 금액, 수수료")
    public ResponseEntity<ResponseDto<MentoringWithPaymentResponse>> paymentWithMentoring(@PathVariable Long paymentId) {
        MentoringWithPaymentResponse mentoringWithPaymentResponse = adminPaymentUseCase.paymentMentoringInfo(paymentId);
        return ResponseEntity.ok(ResponseDto.create("","",mentoringWithPaymentResponse));
    }

    @PostMapping("/payment/mentoring/refund/{paymentId}")
    @Operation(summary = "결제 환불", description = "결제 기록을 기반으로 환불")
    public ResponseEntity<ResponseDto<Void>> refundPayment(@AuthenticationPrincipal User user, @PathVariable Long paymentId) {
        adminPaymentUseCase.refundPayment(user, paymentId);
        return ResponseEntity.ok(ResponseDto.create("",""));
    }

    // 결제 관련

    @GetMapping("/wish/waiting")
    @Operation(summary = "대기중 신청서 확인", description = "분야, 대학원, 교수, 랩실, 핸드폰번호, 신청일")
    public ResponseEntity<ResponseDto<WaitingWishResponses>> waitingWish() {
        WaitingWishResponses waitingWishResponses = adminWishUseCase.waitingWish();
        return ResponseEntity.ok(ResponseDto.create("","", waitingWishResponses));
    }

    @GetMapping("/wish/matching")
    @Operation(summary = "완료된 신청서 확인", description = "분야, 대학원, 교수, 랩실, 핸드폰번호, 신청일, 완료일")
    public ResponseEntity<ResponseDto<MatchingWishResponses>> matching() {
        MatchingWishResponses matchingWishResponses = adminWishUseCase.matchingWish();
        return ResponseEntity.ok(ResponseDto.create("","", matchingWishResponses));
    }

    @PostMapping("/wish/done/{wishId}")
    @Operation(summary = "신청서 완료 확인 및 알림톡 전송", description = "신청서 완료 상태 변경 및 후배 알림톡 전송")
    public ResponseEntity<ResponseDto<Void>> wishDone(@PathVariable Long wishId) {
        adminWishUseCase.matchFin(wishId);
        return ResponseEntity.ok(ResponseDto.create("",""));
    }

    // 신청서 관련 처리

    @PostMapping("/batch/salary")
    @Operation(summary = "개발팀 전용 API", description = "정산 관련 배치 재시도")
    public ResponseEntity<ResponseDto<Void>> startSalaryBatch() {
        adminBatchUseCase.startSalaryBatch();
        return ResponseEntity.ok(ResponseDto.create("",""));
    }

    @PostMapping("/batch/mentoring/done")
    @Operation(summary = "개발팀 전용 API", description = "멘토링 완료 관련 배치 재시도")
    public ResponseEntity<ResponseDto<Void>> startMentoringDone() {
        adminBatchUseCase.startMentoringDoneBatch();
        return ResponseEntity.ok(ResponseDto.create("",""));
    }

    @PostMapping("/batch/mentoring/cancel")
    @Operation(summary = "개발팀 전용 API", description = "멘토링 취소 관련 배치 재시도")
    public ResponseEntity<ResponseDto<Void>> startMentoringCancel() {
        adminBatchUseCase.startMentoringCancelBatch();
        return ResponseEntity.ok(ResponseDto.create("",""));
    }
}
