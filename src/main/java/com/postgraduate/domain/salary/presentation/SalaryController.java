package com.postgraduate.domain.salary.presentation;

import com.postgraduate.domain.salary.application.dto.res.SalaryDetailsResponse;
import com.postgraduate.domain.salary.application.dto.res.SalaryInfoResponse;
import com.postgraduate.domain.salary.application.usecase.SalaryInfoUseCase;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.postgraduate.domain.salary.presentation.constant.SalaryResponseCode.SALARY_FIND;
import static com.postgraduate.domain.salary.presentation.constant.SalaryResponseMessage.GET_SALARY_INFO;
import static com.postgraduate.domain.salary.presentation.constant.SalaryResponseMessage.GET_SALARY_LIST_INFO;
import static com.postgraduate.global.dto.ResponseDto.create;

@RestController
@RequiredArgsConstructor
@RequestMapping("/salary")
@Tag(name = "SALARY Controller", description = "정산의 모든 API는 토큰이 필요합니다.")
public class SalaryController {
    private final SalaryInfoUseCase salaryInfoUseCase;

    @GetMapping()
    @Operation(summary = "대학원생 정산 예정액, 다음 정산 예정일 조회", description = "마이페이지에서 공통으로 보이는 정산 예정 정보입니다.")
    public ResponseEntity<ResponseDto<SalaryInfoResponse>> getSalary(@AuthenticationPrincipal User user) {
        SalaryInfoResponse salary = salaryInfoUseCase.getSalary(user);
        return ResponseEntity.ok(create(SALARY_FIND.getCode(), GET_SALARY_INFO.getMessage(), salary));
    }

    @GetMapping("/waiting")
    @Operation(summary = "대학원생 정산예정 목록 조회", description = "정산 예정 탭에서 보이는 목록입니다.")
    public ResponseEntity<ResponseDto<SalaryDetailsResponse>> getWaitingSalary(@AuthenticationPrincipal User user) {
        SalaryDetailsResponse salary = salaryInfoUseCase.getSalaryDetail(user, false);
        return ResponseEntity.ok(create(SALARY_FIND.getCode(), GET_SALARY_LIST_INFO.getMessage(), salary));
    }

    @GetMapping("/done")
    @Operation(summary = "대학원생 정산완료 목록 조회", description = "정산 완료 탭에서 보이는 목록입니다.")
    public ResponseEntity<ResponseDto<SalaryDetailsResponse>> getDoneSalary(@AuthenticationPrincipal User user) {
        SalaryDetailsResponse salary = salaryInfoUseCase.getSalaryDetail(user, true);
        return ResponseEntity.ok(create(SALARY_FIND.getCode(), GET_SALARY_LIST_INFO.getMessage(), salary));
    }
}
