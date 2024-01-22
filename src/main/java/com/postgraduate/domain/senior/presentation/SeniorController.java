package com.postgraduate.domain.senior.presentation;

import com.postgraduate.domain.available.application.dto.res.AvailableTimesResponse;
import com.postgraduate.domain.senior.application.dto.req.*;
import com.postgraduate.domain.senior.application.dto.res.*;
import com.postgraduate.domain.senior.application.usecase.SeniorInfoUseCase;
import com.postgraduate.domain.senior.application.usecase.SeniorManageUseCase;
import com.postgraduate.domain.senior.application.usecase.SeniorMyPageUseCase;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

    @GetMapping("/all")
    @Operation(summary = "모든 SeniorID 조회")
    public ResponseDto<AllSeniorIdResponse> getAllSeniorId() {
        AllSeniorIdResponse seniorIds = seniorInfoUseCase.getAllSeniorId();
        return ResponseDto.create(SENIOR_FIND.getCode(), GET_SENIOR_ID_LIST.getMessage(), seniorIds);
    }

    @PatchMapping("/certification")
    @Operation(summary = "대학원생 인증 | 토큰 필요", description = "이미지 업로드 이후 url 담아서 요청")
    public ResponseDto updateCertification(@AuthenticationPrincipal User user,
                                           @RequestBody @Valid SeniorCertificationRequest certificationRequest) {
        seniorManageUseCase.updateCertification(user, certificationRequest);
        return ResponseDto.create(SENIOR_UPDATE.getCode(), UPDATE_CERTIFICATION.getMessage());
    }

    @PatchMapping("/profile")
    @Operation(summary = "대학원생 프로필 등록 | 토큰 필요")
    public ResponseDto singUpSenior(@AuthenticationPrincipal User user,
                                    @RequestBody @Valid SeniorProfileRequest profileRequest) {
        seniorManageUseCase.signUpProfile(user, profileRequest);
        return ResponseDto.create(SENIOR_UPDATE.getCode(), UPDATE_PROFILE.getMessage());
    }

    @PostMapping("/account")
    @Operation(summary = "대학원생 정산 계좌 생성 | 토큰 필요")
    public ResponseDto updateAccount(@AuthenticationPrincipal User user,
                                     @RequestBody @Valid SeniorAccountRequest accountRequest) {
        seniorManageUseCase.saveAccount(user, accountRequest);
        return ResponseDto.create(SENIOR_CREATE.getCode(), CREATE_ACCOUNT.getMessage());
    }

    @GetMapping("/me")
    @Operation(summary = "대학원생 마이페이지 기본 정보 조회 | 토큰 필요", description = "닉네임, 프로필 사진, 인증 여부")
    public ResponseDto<SeniorMyPageResponse> getSeniorInfo(@AuthenticationPrincipal User user) {
        SeniorMyPageResponse seniorMyPageResponse = seniorMyPageUseCase.getSeniorInfo(user);
        return ResponseDto.create(SENIOR_FIND.getCode(), GET_SENIOR_INFO.getMessage(), seniorMyPageResponse);
    }

    @GetMapping("/me/profile")
    @Operation(summary = "대학원생 마이페이지 프로필 수정시 기존 정보 조회 | 토큰 필요")
    public ResponseDto<SeniorMyPageProfileResponse> getSeniorProfile(@AuthenticationPrincipal User user) {
        SeniorMyPageProfileResponse myPageProfile = seniorMyPageUseCase.getSeniorMyPageProfile(user);
        return ResponseDto.create(SENIOR_FIND.getCode(), GET_SENIOR_MYPAGE_PROFILE.getMessage(), myPageProfile);
    }

    @PatchMapping("/me/profile")
    @Operation(summary = "대학원생 마이페이지 프로필 수정 | 토큰 필요")
    public ResponseDto updateSeniorProfile(@AuthenticationPrincipal User user,
                                           @RequestBody @Valid SeniorMyPageProfileRequest myPageProfileRequest) {
        seniorManageUseCase.updateSeniorMyPageProfile(user, myPageProfileRequest);
        return ResponseDto.create(SENIOR_UPDATE.getCode(), UPDATE_MYPAGE_PROFILE.getMessage());
    }

    @GetMapping("/me/account")
    @Operation(summary = "대학원생 마이페이지 계정 설정시 기존 정보 조회 | 토큰 필요")
    public ResponseDto<SeniorMyPageUserAccountResponse> getSeniorUserAccount(@AuthenticationPrincipal User user) {
        SeniorMyPageUserAccountResponse seniorOriginInfo = seniorMyPageUseCase.getSeniorMyPageUserAccount(user);
        return ResponseDto.create(SENIOR_FIND.getCode(), GET_SENIOR_MYPAGE_ACCOUNT.getMessage(), seniorOriginInfo);
    }

    @PatchMapping("/me/account")
    @Operation(summary = "대학원생 마이페이지 계정 설정 | 토큰 필요")
    public ResponseDto updateSeniorUserAccount(@AuthenticationPrincipal User user,
                                               @RequestBody @Valid SeniorMyPageUserAccountRequest myPageUserAccountRequest) {
        seniorManageUseCase.updateSeniorMyPageUserAccount(user, myPageUserAccountRequest);
        return ResponseDto.create(SENIOR_UPDATE.getCode(), UPDATE_MYPAGE_ACCOOUNT.getMessage());
    }

    @GetMapping("/{seniorId}")
    @Operation(summary = "대학원생 상세 조회 | 토큰 필요")
    public ResponseDto<SeniorDetailResponse> getSeniorDetails(@AuthenticationPrincipal User user,
                                                              @PathVariable Long seniorId) {
        SeniorDetailResponse seniorDetail = seniorInfoUseCase.getSeniorDetail(user, seniorId);
        return ResponseDto.create(SENIOR_FIND.getCode(), GET_SENIOR_INFO.getMessage(), seniorDetail);
    }

    @GetMapping("/{seniorId}/profile")
    @Operation(summary = "대학원생 닉네임~연구실 등 기본 정보 확인 | 토큰 필요", description = "신청서 완료 후 결제시 노출 필요")
    public ResponseDto<SeniorProfileResponse> getSeniorProfile(@PathVariable Long seniorId) {
        SeniorProfileResponse seniorProfile = seniorInfoUseCase.getSeniorProfile(seniorId);
        return ResponseDto.create(SENIOR_FIND.getCode(), GET_SENIOR_INFO.getMessage(), seniorProfile);
    }

    @GetMapping("/{seniorId}/times")
    @Operation(summary = "대학원생 가능 시간 확인 | 토큰 필요", description = "신청서 작성에서 가능 시간 작성시 노출 필요")
    public ResponseDto<AvailableTimesResponse> getSeniorTimes(@PathVariable Long seniorId) {
        AvailableTimesResponse times = seniorInfoUseCase.getSeniorTimes(seniorId);
        return ResponseDto.create(SENIOR_FIND.getCode(), GET_SENIOR_TIME.getMessage(), times);
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
    public ResponseDto<AllSeniorSearchResponse> getFieldSenior(@RequestParam String field,
                                                               @RequestParam String postgradu,
                                                               @RequestParam(required = false) Integer page) {
        AllSeniorSearchResponse searchSenior = seniorInfoUseCase.getFieldSenior(field, postgradu, page);
        return ResponseDto.create(SENIOR_FIND.getCode(), GET_SENIOR_LIST_INFO.getMessage(), searchSenior);
    }

    @GetMapping("/me/role")
    @Operation(summary = "후배 전환시 가능 여부 확인 | 토큰 필요", description = "true-가능, false-불가능")
    public ResponseDto<SeniorPossibleResponse> checkRole(@AuthenticationPrincipal User user) {
        SeniorPossibleResponse possibleResponse = seniorMyPageUseCase.checkUser(user);
        return ResponseDto.create(SENIOR_FIND.getCode(), GET_USER_CHECK.getMessage(), possibleResponse);
    }
}
