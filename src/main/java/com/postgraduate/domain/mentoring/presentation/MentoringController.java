package com.postgraduate.domain.mentoring.presentation;

import com.postgraduate.domain.mentoring.application.dto.req.MentoringApplyRequest;
import com.postgraduate.domain.mentoring.application.dto.req.MentoringDateRequest;
import com.postgraduate.domain.mentoring.application.dto.res.*;
import com.postgraduate.domain.mentoring.application.usecase.MentoringApplyUseCase;
import com.postgraduate.domain.mentoring.application.usecase.MentoringManageUseCase;
import com.postgraduate.domain.mentoring.application.usecase.MentoringSeniorInfoUseCase;
import com.postgraduate.domain.mentoring.application.usecase.MentoringUserInfoUseCase;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.refuse.application.dto.req.MentoringRefuseRequest;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.*;
import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseCode.*;
import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("mentoring")
@Tag(name = "MENTORING Controller")
public class MentoringController {
    private final MentoringUserInfoUseCase userInfoUseCase;
    private final MentoringSeniorInfoUseCase seniorInfoUseCase;
    private final MentoringApplyUseCase applyUseCase;
    private final MentoringManageUseCase manageUseCase;

    @GetMapping("/me/waiting")
    @Operation(summary = "[대학생] 신청한 멘토링 목록 조회", description = "대학생이 신청한 멘토링 목록을 조회합니다.")
    public ResponseDto<AppliedWaitingMentoringResponse> getWaitingMentorings(@AuthenticationPrincipal User user) {
        AppliedWaitingMentoringResponse mentoringResponse = userInfoUseCase.getWaiting(user);
        return ResponseDto.create(MENTORING_FIND.getCode(), GET_MENTORING_LIST_INFO.getMessage(), mentoringResponse);
    }

    @GetMapping("/me/expected")
    @Operation(summary = "[대학생] 예정된 멘토링 목록 조회", description = "대학생이 예정된 멘토링 목록을 조회합니다.")
    public ResponseDto<AppliedExpectedMentoringResponse> getExpectedMentorings(@AuthenticationPrincipal User user) {
        AppliedExpectedMentoringResponse mentoringResponse = userInfoUseCase.getExpected(user);
        return ResponseDto.create(MENTORING_FIND.getCode(), GET_MENTORING_LIST_INFO.getMessage(), mentoringResponse);
    }

    @GetMapping("/me/done")
    @Operation(summary = "[대학생] 완료한 멘토링 목록 조회", description = "대학생이 완료한 멘토링 목록을 조회합니다.")
    public ResponseDto<AppliedDoneMentoringResponse> getDoneMentorings(@AuthenticationPrincipal User user) {
        AppliedDoneMentoringResponse mentoringResponse = userInfoUseCase.getDone(user);
        return ResponseDto.create(MENTORING_FIND.getCode(), GET_MENTORING_LIST_INFO.getMessage(), mentoringResponse);
    }

    @GetMapping("/me/{mentoringId}")
    @Operation(summary = "[대학생] 신청한 멘토링 상세조회", description = "대학생이 신청한 멘토링을 상세조회합니다. <완료> 상태의 멘토링은 상세조회되지 않습니다.")
    public ResponseDto<AppliedMentoringDetailResponse> getMentoringDetail(@AuthenticationPrincipal User user, @PathVariable Long mentoringId) {
        AppliedMentoringDetailResponse mentoringDetail = userInfoUseCase.getMentoringDetail(user, mentoringId);
        return ResponseDto.create(MENTORING_FIND.getCode(), GET_MENTORING_DETAIL_INFO.getMessage(), mentoringDetail);
    }

    @PostMapping("/applying")
    @Operation(summary = "[대학생] 멘토링 신청", description = "대학생이 멘토링을 신청합니다.")
    public ResponseDto applyMentoring(@AuthenticationPrincipal User user, @RequestBody MentoringApplyRequest request) {
        applyUseCase.applyMentoring(user, request);
        return ResponseDto.create(MENTORING_CREATE.getCode(), CREATE_MENTORING.getMessage());
    }

    @PatchMapping("/me/{mentoringId}/done")
    @Operation(summary = "[대학생] 멘토링 상태 업데이트(완료)", description = "대학생이 멘토링을 완료합니다.")
    public ResponseDto updateMentoringDone(@AuthenticationPrincipal User user,
                                           @PathVariable Long mentoringId) {
        manageUseCase.updateDone(user, mentoringId);
        return ResponseDto.create(MENTORING_UPDATE.getCode(), UPDATE_MENTORING.getMessage());
    }

    @PatchMapping("/me/{mentoringId}/cancel")
    @Operation(summary = "[대학생] 멘토링 상태 업데이트(취소)", description = "대학생이 신청한 멘토링을 취소합니다.")
    public ResponseDto updateMentoringCancel(@AuthenticationPrincipal User user,
                                             @PathVariable Long mentoringId) {
        manageUseCase.updateCancel(user, mentoringId);
        return ResponseDto.create(MENTORING_UPDATE.getCode(), UPDATE_MENTORING.getMessage());
    }

    @GetMapping("/senior/me/waiting")
    @Operation(summary = "[대학원생] 신청받은 확정대기 멘토링 목록 조회", description = "대학원생이 신청받은 멘토링 목록을 조회합니다.")
    public ResponseDto<SeniorWaitingMentoringResponse> getSeniorWaitingMentorings(@AuthenticationPrincipal User user) {
        SeniorWaitingMentoringResponse mentoringResponse = seniorInfoUseCase.getSeniorWaiting(user);
        return ResponseDto.create(MENTORING_FIND.getCode(), GET_MENTORING_LIST_INFO.getMessage(), mentoringResponse);
    }

    @GetMapping("/senior/me/expected")
    @Operation(summary = "[대학원생] 신청받은 예정된 멘토링 목록 조회", description = "대학원생이 신청받은 멘토링 목록을 조회합니다.")
    public ResponseDto<SeniorExpectedMentoringResponse> getSeniorExpectedMentorings(@AuthenticationPrincipal User user) {
        SeniorExpectedMentoringResponse mentoringResponse = seniorInfoUseCase.getSeniorExpected(user);
        return ResponseDto.create(MENTORING_FIND.getCode(), GET_MENTORING_LIST_INFO.getMessage(), mentoringResponse);
    }

    @GetMapping("/senior/me/done")
    @Operation(summary = "[대학원생] 신청받은 완료된 멘토링 목록 조회", description = "대학원생이 신청받은 멘토링 목록을 조회합니다.")
    public ResponseDto<SeniorDoneMentoringResponse> getSeniorDoneMentorings(@AuthenticationPrincipal User user) {
        SeniorDoneMentoringResponse mentoringResponse = seniorInfoUseCase.getSeniorDone(user);
        return ResponseDto.create(MENTORING_FIND.getCode(), GET_MENTORING_LIST_INFO.getMessage(), mentoringResponse);
    }

    @GetMapping("/senior/me/{mentoringId}")
    @Operation(summary = "[대학원생] 신청받은 멘토링 상세조회", description = "대학원생이 신청받은 멘토링을 상세조회합니다. <완료> 상태의 멘토링은 상세조회되지 않습니다.")
    public ResponseDto<SeniorMentoringDetailResponse> getSeniorMentoringDetail(@AuthenticationPrincipal User user, @PathVariable Long mentoringId) {
        SeniorMentoringDetailResponse seniorMentoringDetail = seniorInfoUseCase.getSeniorMentoringDetail(user, mentoringId);
        return ResponseDto.create(MENTORING_FIND.getCode(), GET_MENTORING_DETAIL_INFO.getMessage(), seniorMentoringDetail);
    }

    @PatchMapping("/senior/me/{mentoringId}/expected")
    @Operation(summary = "[대학원생] 멘토링 상태 업데이트(예정된 멘토링)", description = "대학원생이 멘토링을 수락합니다.")
    public ResponseDto updateMentoringExpected(@AuthenticationPrincipal User user,
                                               @PathVariable Long mentoringId,
                                               @RequestBody MentoringDateRequest dateRequest) {
        Boolean accountPresent = manageUseCase.updateExpected(user, mentoringId, dateRequest);
        return ResponseDto.create(MENTORING_UPDATE.getCode(), UPDATE_MENTORING.getMessage(), accountPresent);
    }

    @PatchMapping("/senior/me/{mentoringId}/refuse")
    @Operation(summary = "[대학원생] 멘토링 상태 업데이트(거절)", description = "대학원생이 멘토링을 거절하고 거절사유를 변경합니다.")
    public ResponseDto updateMentoringCancel(@AuthenticationPrincipal User user,
                                             @PathVariable Long mentoringId,
                                             @RequestBody MentoringRefuseRequest request) {
        manageUseCase.updateRefuse(user, mentoringId, request);
        return ResponseDto.create(MENTORING_UPDATE.getCode(), UPDATE_MENTORING.getMessage());
    }
}
