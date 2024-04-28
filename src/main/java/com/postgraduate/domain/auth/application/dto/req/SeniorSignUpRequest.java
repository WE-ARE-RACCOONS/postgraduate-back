package com.postgraduate.domain.auth.application.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SeniorSignUpRequest(@NotNull Long socialId, @NotBlank String phoneNumber,
                                  @Size(max = 6, message = "6글자까지 입력 가능합니다.") @NotBlank String nickName,
                                  Boolean marketingReceive, @NotBlank String major, @NotBlank String postgradu,
                                  @NotBlank String professor, @NotBlank String lab, @NotBlank String field,
                                  @NotBlank String keyword) {
}
