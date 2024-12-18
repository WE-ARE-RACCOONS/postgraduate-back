package com.postgraduate.domain.member.senior.presentation;

import com.postgraduate.domain.member.senior.application.dto.req.*;
import com.postgraduate.domain.member.senior.application.dto.res.*;
import com.postgraduate.domain.member.senior.application.usecase.SeniorInfoUseCase;
import com.postgraduate.domain.member.senior.application.usecase.SeniorManageUseCase;
import com.postgraduate.domain.member.senior.application.usecase.SeniorMyPageUseCase;
import com.postgraduate.domain.member.senior.presentation.constant.SeniorResponseCode;
import com.postgraduate.domain.member.senior.presentation.constant.SeniorResponseMessage;
import com.postgraduate.domain.member.user.domain.entity.User;
import com.postgraduate.domain.member.senior.application.dto.res.AvailableTimesResponse;
import com.postgraduate.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.postgraduate.global.dto.ResponseDto.create;


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
    public ResponseEntity<ResponseDto<AllSeniorIdResponse>> getAllSeniorId() {
        AllSeniorIdResponse seniorIds = seniorInfoUseCase.getAllSeniorId();
        return ResponseEntity.ok(create(SeniorResponseCode.SENIOR_FIND.getCode(), SeniorResponseMessage.GET_SENIOR_ID_LIST.getMessage(), seniorIds));
    }

    @PatchMapping("/certification")
    @Operation(summary = "대학원생 인증 | 토큰 필요", description = "이미지 업로드 이후 url 담아서 요청")
    public ResponseEntity<ResponseDto<Void>> updateCertification(@AuthenticationPrincipal User user,
                                           @RequestBody @Valid SeniorCertificationRequest certificationRequest) {
        seniorManageUseCase.updateCertification(user, certificationRequest);
        return ResponseEntity.ok(create(SeniorResponseCode.SENIOR_UPDATE.getCode(), SeniorResponseMessage.UPDATE_CERTIFICATION.getMessage()));
    }

    @PatchMapping("/profile")
    @Operation(summary = "대학원생 프로필 등록 | 토큰 필요")
    public ResponseEntity<ResponseDto<SeniorProfileUpdateResponse>> singUpSenior(@AuthenticationPrincipal User user,
                                                                                 @RequestBody @Valid SeniorProfileRequest profileRequest) {
        SeniorProfileUpdateResponse updateResponse = seniorManageUseCase.signUpProfile(user, profileRequest);
        return ResponseEntity.ok(create(SeniorResponseCode.SENIOR_UPDATE.getCode(), SeniorResponseMessage.UPDATE_PROFILE.getMessage(), updateResponse));
    }

    @PostMapping("/account")
    @Operation(summary = "대학원생 정산 계좌 생성 | 토큰 필요")
    public ResponseEntity<ResponseDto<Void>> updateAccount(@AuthenticationPrincipal User user,
                                     @RequestBody @Valid SeniorAccountRequest accountRequest) {
        seniorManageUseCase.saveAccount(user, accountRequest);
        return ResponseEntity.ok(create(SeniorResponseCode.SENIOR_CREATE.getCode(), SeniorResponseMessage.CREATE_ACCOUNT.getMessage()));
    }

    @GetMapping("/me")
    @Operation(summary = "대학원생 마이페이지 기본 정보 조회 | 토큰 필요", description = "닉네임, 프로필 사진, 인증 여부")
    public ResponseEntity<ResponseDto<SeniorMyPageResponse>> getSeniorInfo(@AuthenticationPrincipal User user) {
        SeniorMyPageResponse seniorMyPageResponse = seniorMyPageUseCase.getSeniorMyPage(user);
        return ResponseEntity.ok(create(SeniorResponseCode.SENIOR_FIND.getCode(), SeniorResponseMessage.GET_SENIOR_INFO.getMessage(), seniorMyPageResponse));
    }

    @GetMapping("/me/profile")
    @Operation(summary = "대학원생 마이페이지 프로필 수정시 기존 정보 조회 | 토큰 필요")
    public ResponseEntity<ResponseDto<SeniorMyPageProfileResponse>> getSeniorProfile(@AuthenticationPrincipal User user) {
        SeniorMyPageProfileResponse myPageProfile = seniorMyPageUseCase.getSeniorMyPageProfile(user);
        return ResponseEntity.ok(create(SeniorResponseCode.SENIOR_FIND.getCode(), SeniorResponseMessage.GET_SENIOR_MYPAGE_PROFILE.getMessage(), myPageProfile));
    }

    @PatchMapping("/me/profile")
    @Operation(summary = "대학원생 마이페이지 프로필 수정 | 토큰 필요")
    public ResponseEntity<ResponseDto<SeniorProfileUpdateResponse>> updateSeniorProfile(@AuthenticationPrincipal User user,
                                           @RequestBody @Valid SeniorMyPageProfileRequest myPageProfileRequest) {
        SeniorProfileUpdateResponse updateResponse = seniorManageUseCase.updateSeniorMyPageProfile(user, myPageProfileRequest);
        return ResponseEntity.ok(create(SeniorResponseCode.SENIOR_UPDATE.getCode(), SeniorResponseMessage.UPDATE_MYPAGE_PROFILE.getMessage(), updateResponse));
    }

    @GetMapping("/me/account")
    @Operation(summary = "대학원생 마이페이지 계정 설정시 기존 정보 조회 | 토큰 필요")
    public ResponseEntity<ResponseDto<SeniorMyPageUserAccountResponse>> getSeniorUserAccount(@AuthenticationPrincipal User user) {
        SeniorMyPageUserAccountResponse seniorOriginInfo = seniorMyPageUseCase.getSeniorMyPageUserAccount(user);
        return ResponseEntity.ok(create(SeniorResponseCode.SENIOR_FIND.getCode(), SeniorResponseMessage.GET_SENIOR_MYPAGE_ACCOUNT.getMessage(), seniorOriginInfo));
    }

    @PatchMapping("/me/account")
    @Operation(summary = "대학원생 마이페이지 계정 설정 | 토큰 필요")
    public ResponseEntity<ResponseDto<Void>> updateSeniorUserAccount(@AuthenticationPrincipal User user,
                                               @RequestBody @Valid SeniorMyPageUserAccountRequest myPageUserAccountRequest) {
        seniorManageUseCase.updateSeniorMyPageUserAccount(user, myPageUserAccountRequest);
        return ResponseEntity.ok(create(SeniorResponseCode.SENIOR_UPDATE.getCode(), SeniorResponseMessage.UPDATE_MYPAGE_ACCOUNT.getMessage()));
    }

    @GetMapping("/{seniorId}")
    @Operation(summary = "대학원생 상세 조회 | 토큰 필요")
    public ResponseEntity<ResponseDto<SeniorDetailResponse>> getSeniorDetails(@AuthenticationPrincipal User user,
                                                              @PathVariable Long seniorId) {
        SeniorDetailResponse seniorDetail = seniorInfoUseCase.getSeniorDetail(user, seniorId);
        return ResponseEntity.ok(create(SeniorResponseCode.SENIOR_FIND.getCode(), SeniorResponseMessage.GET_SENIOR_INFO.getMessage(), seniorDetail));
    }

    @GetMapping("/{seniorId}/profile")
    @Operation(summary = "대학원생 닉네임~연구실 등 기본 정보 확인 | 토큰 필요", description = "신청서 완료 후 결제시 노출 필요")
    public ResponseEntity<ResponseDto<SeniorProfileResponse>> getSeniorProfile(@AuthenticationPrincipal User user, @PathVariable Long seniorId) {
        SeniorProfileResponse seniorProfile = seniorInfoUseCase.getSeniorProfile(user, seniorId);
        return ResponseEntity.ok(create(SeniorResponseCode.SENIOR_FIND.getCode(), SeniorResponseMessage.GET_SENIOR_INFO.getMessage(), seniorProfile));
    }

    @GetMapping("/{seniorId}/times")
    @Operation(summary = "대학원생 가능 시간 확인 | 토큰 필요", description = "신청서 작성에서 가능 시간 작성시 노출 필요")
    public ResponseEntity<ResponseDto<AvailableTimesResponse>> getSeniorTimes(@PathVariable Long seniorId) {
        AvailableTimesResponse times = seniorInfoUseCase.getSeniorTimes(seniorId);
        return ResponseEntity.ok(create(SeniorResponseCode.SENIOR_FIND.getCode(), SeniorResponseMessage.GET_SENIOR_TIME.getMessage(), times));
    }

    @GetMapping("/search")
    @Operation(summary = "대학원생 검색어 검색", description = "find 필수, sort 선택 - 안보낼 경우 아예 파라미터 추가x (조회수 낮은순 low, 높은순 high), page선택 (안보내면 기본 1페이지)")
    public ResponseEntity<ResponseDto<AllSeniorSearchResponse>> getSearchSenior(@RequestParam String find,
                                                                @RequestParam(required = false) String sort,
                                                                @RequestParam(required = false) Integer page) {
        AllSeniorSearchResponse searchSenior = seniorInfoUseCase.getSearchSenior(find, page, sort);
        return ResponseEntity.ok(create(SeniorResponseCode.SENIOR_FIND.getCode(), SeniorResponseMessage.GET_SENIOR_LIST_INFO.getMessage(), searchSenior));
    }

    @GetMapping("/field")
    @Operation(summary = "대학원생 필드 검색", description = "분야 (분야1,분야2 이런식으로, 다른분야 : others, 전체 : all), 대학원 필수 (대학원1,대학원2 이런식으로, 다른학교 : others, 전체 : all), 페이지 선택 ")
    public ResponseEntity<ResponseDto<AllSeniorSearchResponse>> getFieldSenior(@RequestParam String field,
                                                               @RequestParam String postgradu,
                                                               @RequestParam(required = false) Integer page) {
        AllSeniorSearchResponse searchSenior = seniorInfoUseCase.getFieldSenior(field, postgradu, page);
        return ResponseEntity.ok(create(SeniorResponseCode.SENIOR_FIND.getCode(), SeniorResponseMessage.GET_SENIOR_LIST_INFO.getMessage(), searchSenior));
    }
}
