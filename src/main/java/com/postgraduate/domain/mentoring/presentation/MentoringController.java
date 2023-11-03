package com.postgraduate.domain.mentoring.presentation;

import com.postgraduate.domain.mentoring.application.dto.req.MentoringApplyRequest;
import com.postgraduate.domain.mentoring.application.dto.req.MentoringStatusRequest;
import com.postgraduate.domain.mentoring.application.dto.res.AppliedMentoringDetailResponse;
import com.postgraduate.domain.mentoring.application.dto.res.AppliedMentoringResponse;
import com.postgraduate.domain.mentoring.application.usecase.MentoringApplyUseCase;
import com.postgraduate.domain.mentoring.application.usecase.MentoringInfoUseCase;
import com.postgraduate.domain.mentoring.application.usecase.MentoringUpdateUseCase;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.global.auth.AuthDetails;
import com.postgraduate.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseCode.*;
import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("mentoring")
@Tag(name = "MENTORING Controller")
public class MentoringController {
    private final MentoringInfoUseCase infoUsecase;
    private final MentoringApplyUseCase applyUseCase;
    private final MentoringUpdateUseCase updateUseCase;

    @GetMapping("/me")
    @Operation(summary = "[대학생] 신청한 멘토링 목록 조회", description = "대학생이 신청한 멘토링 목록을 조회합니다.")
    public ResponseDto<AppliedMentoringResponse> getMentoringInfos(@RequestParam Status status, @AuthenticationPrincipal AuthDetails authDetails) {
        AppliedMentoringResponse mentoringResponse = infoUsecase.getMentorings(status, authDetails);
        return ResponseDto.create(MENTORING_FIND.getCode(), GET_MENTORING_LIST_INFO.getMessage(), mentoringResponse);
    }

    @GetMapping("/me/{mentoringId}")
    @Operation(summary = "[대학생] 신청한 멘토링 상세조회", description = "대학생이 신청한 멘토링을 상세조회합니다.")
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

    @PatchMapping("/me/{mentoringId}")
    @Operation(summary = "[대학생, 대학원생] 멘토링 상태 업데이트", description = "대학생, 대학원생이 멘토링 상태를 변경합니다.")
    public ResponseDto updateMentoringStatus(@AuthenticationPrincipal AuthDetails authDetails,
                                             @PathVariable Long mentoringId,
                                             @RequestBody MentoringStatusRequest request) {
        updateUseCase.updateStatus(authDetails, mentoringId, request);
        return ResponseDto.create(MENTORING_UPDATE.getCode(), UPDATE_MENTORING.getMessage());
    }
}
