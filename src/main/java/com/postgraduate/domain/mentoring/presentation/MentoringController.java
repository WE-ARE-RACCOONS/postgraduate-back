package com.postgraduate.domain.mentoring.presentation;

import com.postgraduate.domain.mentoring.application.dto.req.MentoringApplyRequest;
import com.postgraduate.domain.mentoring.application.dto.req.MentoringDateRequest;
import com.postgraduate.domain.mentoring.application.dto.req.MentoringStatusRequest;
import com.postgraduate.domain.mentoring.application.dto.res.AppliedMentoringDetailResponse;
import com.postgraduate.domain.mentoring.application.dto.res.AppliedMentoringResponse;
import com.postgraduate.domain.mentoring.application.dto.res.SeniorMentoringDetailResponse;
import com.postgraduate.domain.mentoring.application.dto.res.SeniorMentoringResponse;
import com.postgraduate.domain.mentoring.application.usecase.MentoringApplyUseCase;
import com.postgraduate.domain.mentoring.application.usecase.MentoringInfoUseCase;
import com.postgraduate.domain.mentoring.application.usecase.MentoringManageUseCase;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.global.auth.AuthDetails;
import com.postgraduate.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseCode.*;
import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("mentoring")
@Tag(name = "MENTORING Controller")
public class MentoringController {
    private final MentoringInfoUseCase infoUsecase;
    private final MentoringApplyUseCase applyUseCase;
    private final MentoringManageUseCase manageUseCase;

    @GetMapping("/me")
    @Operation(summary = "[대학생] 신청한 멘토링 목록 조회", description = "대학생이 신청한 멘토링 목록을 조회합니다.")
    public ResponseDto<AppliedMentoringResponse> getMentoringInfos(@RequestParam Status status, @AuthenticationPrincipal AuthDetails authDetails) {
        AppliedMentoringResponse mentoringResponse = infoUsecase.getMentorings(status, authDetails);
        return ResponseDto.create(MENTORING_FIND.getCode(), GET_MENTORING_LIST_INFO.getMessage(), mentoringResponse);
    }

    @GetMapping("/me/{mentoringId}")
    @Operation(summary = "[대학생] 신청한 멘토링 상세조회", description = "대학생이 신청한 멘토링을 상세조회합니다. <확정대기> 상태의 멘토링만 조회 가능합니다.")
    public ResponseDto<AppliedMentoringDetailResponse> getMentoringDetail(@AuthenticationPrincipal AuthDetails authDetails, @PathVariable Long mentoringId) {
        AppliedMentoringDetailResponse mentoringDetail = infoUsecase.getMentoringDetail(authDetails, mentoringId);
        return ResponseDto.create(MENTORING_FIND.getCode(), GET_MENTORING_DETAIL_INFO.getMessage(), mentoringDetail);
    }

    @PostMapping()
    @Operation(summary = "[대학생] 멘토링 신청", description = "대학생이 멘토링을 신청합니다.")
    public ResponseDto applyMentoring(@AuthenticationPrincipal AuthDetails authDetails, @RequestBody MentoringApplyRequest request) {
        applyUseCase.applyMentoring(authDetails, request);
        return ResponseDto.create(MENTORING_CREATE.getCode(), CREATE_MENTORING.getMessage());
    }

    @PatchMapping("/me/{mentoringId}/done")
    @Operation(summary = "[대학생] 멘토링 상태 업데이트(완료)", description = "대학생이 멘토링을 완료합니다.")
    public ResponseDto updateMentoringDone(@AuthenticationPrincipal AuthDetails authDetails,
                                           @PathVariable Long mentoringId) {
        manageUseCase.updateStatus(authDetails, mentoringId, Status.DONE);
        return ResponseDto.create(MENTORING_UPDATE.getCode(), UPDATE_MENTORING.getMessage());
    }

    @PatchMapping("/me/{mentoringId}/cancel")
    @Operation(summary = "[대학생] 멘토링 상태 업데이트(취소)", description = "대학생이 신청한 멘토링을 취소합니다.")
    public ResponseDto updateMentoringCancel(@AuthenticationPrincipal AuthDetails authDetails,
                                             @PathVariable Long mentoringId) {
        manageUseCase.updateStatus(authDetails, mentoringId, Status.CANCEL);
        return ResponseDto.create(MENTORING_UPDATE.getCode(), UPDATE_MENTORING.getMessage());
    }

    @GetMapping("/senior/me")
    @Operation(summary = "[대학원생] 신청받은 멘토링 목록 조회", description = "대학원생이 신청받은 멘토링 목록을 조회합니다.")
    public ResponseDto<List<SeniorMentoringResponse>> getSeniorMentorings(@RequestParam Status status, @AuthenticationPrincipal AuthDetails authDetails) {
        List<SeniorMentoringResponse> seniorMentorings = infoUsecase.getSeniorMentorings(status, authDetails);
        return ResponseDto.create(MENTORING_FIND.getCode(), GET_MENTORING_LIST_INFO.getMessage(), seniorMentorings);
    }

    @GetMapping("/senior/me/{mentoringId}")
    @Operation(summary = "[대학원생] 신청받은 멘토링 상세조회", description = "대학원생이 신청받은 멘토링을 상세조회합니다. <완료> 상태의 멘토링은 상세조회되지 않습니다.")
    public ResponseDto<SeniorMentoringDetailResponse> getSeniorMentoringDetail(@AuthenticationPrincipal AuthDetails authDetails, @PathVariable Long mentoringId) {
        SeniorMentoringDetailResponse seniorMentoringDetail = infoUsecase.getSeniorMentoringDetail(authDetails, mentoringId);
        return ResponseDto.create(MENTORING_FIND.getCode(), GET_MENTORING_DETAIL_INFO.getMessage(), seniorMentoringDetail);
    }

    @PatchMapping("/senior/me/{mentoringId}/time")
    @Operation(summary = "[대학원생] 멘토링 시간 선택", description = "대학생이 신청한 시간 옵션 3개 중 하나를 선택합니다. 확정 대기 상태의 멘토링만 가능합니다.")
    public ResponseDto updateMentoringDate(@AuthenticationPrincipal AuthDetails authDetails,
                                           @PathVariable Long mentoringId,
                                           @RequestBody MentoringDateRequest request) {
        manageUseCase.updateDate(authDetails, mentoringId, request);
        return ResponseDto.create(MENTORING_UPDATE.getCode(), UPDATE_MENTORING.getMessage());
    }

    @PatchMapping("/senior/me/{mentoringId}/expected")
    @Operation(summary = "[대학원생] 멘토링 상태 업데이트(예정된 멘토링)", description = "대학원생이 멘토링을 수락합니다.")
    public ResponseDto updateMentoringExpected(@AuthenticationPrincipal AuthDetails authDetails, @PathVariable Long mentoringId) {
        manageUseCase.updateSeniorStatus(authDetails, mentoringId, Status.EXPECTED);
        return ResponseDto.create(MENTORING_UPDATE.getCode(), UPDATE_MENTORING.getMessage());
    }

    @PatchMapping("/senior/me/{mentoringId}/cancel")
    @Operation(summary = "[대학원생] 멘토링 상태 업데이트(거절)", description = "대학원생이 멘토링을 거절하고 거절사유를 변경합니다.")
    public ResponseDto updateMentoringCancel(@AuthenticationPrincipal AuthDetails authDetails,
                                             @PathVariable Long mentoringId,
                                             @RequestBody MentoringStatusRequest request) {
        manageUseCase.updateRefuse(authDetails, mentoringId, request, Status.CANCEL);
        return ResponseDto.create(MENTORING_UPDATE.getCode(), UPDATE_MENTORING.getMessage());
    }
}
