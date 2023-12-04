package com.postgraduate.domain.senior.presentation;

import com.postgraduate.domain.senior.application.dto.req.*;
import com.postgraduate.domain.senior.application.dto.res.*;
import com.postgraduate.domain.senior.application.usecase.SeniorInfoUseCase;
import com.postgraduate.domain.senior.application.usecase.SeniorManageUseCase;
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
    private final SeniorManageUseCase seniorManageUseCase;
    private final SeniorInfoUseCase seniorInfoUseCase;

    @PatchMapping("/certification")
    @Operation(summary = "대학원생 인증", description = "이미지 업로드 이후 url 담아서 요청")
    public ResponseDto updateCertification(@AuthenticationPrincipal User user,
                                           @RequestBody SeniorCertificationRequest certificationRequest) {
        seniorManageUseCase.updateCertification(user, certificationRequest);
        return ResponseDto.create(SENIOR_UPDATE.getCode(), UPDATE_CERTIFICATION.getMessage());
    }

    @PatchMapping("/profile")
    @Operation(summary = "대학원생 프로필 등록")
    public ResponseDto singUpSenior(@AuthenticationPrincipal User user,
                                    @RequestBody SeniorProfileRequest profileRequest) {
        seniorManageUseCase.signUpProfile(user, profileRequest);
        return ResponseDto.create(SENIOR_UPDATE.getCode(), UPDATE_PROFILE.getMessage());
    }

    @PostMapping("/account")
    @Operation(summary = "대학원생 정산 계좌 생성")
    public ResponseDto updateAccount(@AuthenticationPrincipal User user,
                                     @RequestBody SeniorAccountRequest accountRequest) {
        seniorManageUseCase.saveAccount(user, accountRequest);
        return ResponseDto.create(SENIOR_CREATE.getCode(), CREATE_ACCOUNT.getMessage());
    }

    @GetMapping("/me")
    @Operation(summary = "대학원생 마이페이지 기본 정보 조회", description = "닉네임, 프로필 사진, 인증 여부")
    public ResponseDto<SeniorMyPageResponse> getSeniorInfo(@AuthenticationPrincipal User user) {
        SeniorMyPageResponse seniorMyPageResponse = seniorMyPageUseCase.getSeniorInfo(user);
        return ResponseDto.create(SENIOR_FIND.getCode(), GET_SENIOR_INFO.getMessage(), seniorMyPageResponse);
    }

    @GetMapping("/me/profile")
    @Operation(summary = "대학원생 마이페이지 프로필 수정시 기존 정보 조회")
    public ResponseDto<SeniorMyPageProfileResponse> getSeniorProfile(@AuthenticationPrincipal User user) {
        SeniorMyPageProfileResponse myPageProfile = seniorMyPageUseCase.getSeniorMyPageProfile(user);
        return ResponseDto.create(SENIOR_FIND.getCode(), GET_SENIOR_MYPAGE_PROFILE.getMessage(), myPageProfile);
    }

    @PatchMapping("/me/profile")
    @Operation(summary = "대학원생 마이페이지 프로필 수정")
    public ResponseDto updateSeniorProfile(@AuthenticationPrincipal User user,
                                        @RequestBody SeniorMyPageProfileRequest myPageProfileRequest) {
        seniorManageUseCase.updateSeniorMyPageProfile(user, myPageProfileRequest);
        return ResponseDto.create(SENIOR_UPDATE.getCode(), UPDATE_MYPAGE_PROFILE.getMessage());
    }

    @GetMapping("/me/account")
    @Operation(summary = "대학원생 마이페이지 계정 설정시 기존 정보 조회")
    public ResponseDto<SeniorMyPageUserAccountResponse> getSeniorUserAccount(@AuthenticationPrincipal User user) {
        SeniorMyPageUserAccountResponse seniorOriginInfo = seniorMyPageUseCase.getSeniorMyPageUserAccount(user);
        return ResponseDto.create(SENIOR_FIND.getCode(), GET_SENIOR_MYPAGE_ACCOUNT.getMessage(), seniorOriginInfo);
    }

    @PatchMapping("/me/account")
    @Operation(summary = "대학원생 마이페이지 계정 설정")
    public ResponseDto updateSeniorUserAccount(@AuthenticationPrincipal User user,
                                           @RequestBody SeniorMyPageUserAccountRequest myPageUserAccountRequest) {
        seniorManageUseCase.updateSeniorMyPageUserAccount(user, myPageUserAccountRequest);
        return ResponseDto.create(SENIOR_UPDATE.getCode(), UPDATE_MYPAGE_ACCOOUNT.getMessage());
    }

    @GetMapping("/{seniorId}")
    @Operation(summary = "대학원생 상세 조회")
    public ResponseDto<SeniorDetailResponse> getSeniorDetails(@PathVariable Long seniorId) {
        SeniorDetailResponse seniorDetail = seniorInfoUseCase.getSeniorDetail(seniorId);
        return ResponseDto.create(SENIOR_FIND.getCode(), GET_SENIOR_INFO.getMessage(), seniorDetail);
    }

    @GetMapping("/search")
    @Operation(summary = "대학원생 검색어 검색", description = "find 필수, sort 선택 - 안보낼 경우 아예 파라미터 추가x (조회수 낮은순 low, 높은순 high), page선택 (안보내면 기본 1페이지)")
    public ResponseDto<AllSeniorSearchResponse> getSearchSenior(@RequestParam String find,
                                                                @RequestParam(required = false) String sort,
                                                                @RequestParam(required = false) Integer page) {
        AllSeniorSearchResponse searchSenior = seniorInfoUseCase.getSearchSenior(find, page, sort);
        return ResponseDto.create(SENIOR_FIND.getCode(), GET_SENIOR_LIST_INFO.getMessage(), searchSenior);
    }

    @GetMapping("/field")
    @Operation(summary = "대학원생 필드 검색", description = "분야 (분야1,분야2 이런식으로, 다른분야 : others), 대학원 필수 (대학원1,대학원2 이런식으로, 다른학교 : others, 전체 : all), 페이지 선택 ")
    public ResponseDto<AllSeniorFieldResponse> getFieldSenior(@RequestParam String field,
                                                                @RequestParam String postgradu,
                                                                @RequestParam(required = false) Integer page) {
        AllSeniorFieldResponse fieldSenior = seniorInfoUseCase.getFieldSenior(field, postgradu, page);
        return ResponseDto.create(SENIOR_FIND.getCode(), GET_SENIOR_LIST_INFO.getMessage(), fieldSenior);
    }
}
