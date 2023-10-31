package com.postgraduate.domain.mentoring.presentation;

import com.postgraduate.domain.mentoring.application.dto.AppliedMentoringDetailResponse;
import com.postgraduate.domain.mentoring.application.dto.AppliedMentoringResponse;
import com.postgraduate.domain.mentoring.application.usecase.MentoringInfoUseCase;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.global.auth.AuthDetails;
import com.postgraduate.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseMessage.GET_MENTORING_DETAIL_INFO;
import static com.postgraduate.domain.mentoring.presentation.constant.MentoringResponseMessage.GET_MENTORING_LIST_INFO;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("mentoring")
@Tag(name = "MENTORING Controller")
public class MentoringController {
    private final MentoringInfoUseCase infoUsecase;

    @GetMapping("/me")
    @Operation(description = "대학생 신청 멘토링 조회")
    public ResponseDto<AppliedMentoringResponse> getMentoringInfos(@RequestParam Status status, @AuthenticationPrincipal AuthDetails authDetails) {
        AppliedMentoringResponse mentoringResponse = infoUsecase.getMentorings(status, authDetails);
        return ResponseDto.create(OK.value(), GET_MENTORING_LIST_INFO.getMessage(), mentoringResponse);
    }

    @GetMapping("/me/{mentoringId}")
    @Operation(description = "대학생 신청 멘토링 상세조회")
    public ResponseDto<AppliedMentoringDetailResponse> getMentoringDetail(@PathVariable Long mentoringId) {
        AppliedMentoringDetailResponse mentoringDetail = infoUsecase.getMentoringDetail(mentoringId);
        return ResponseDto.create(OK.value(), GET_MENTORING_DETAIL_INFO.getMessage(), mentoringDetail);
    }
}
