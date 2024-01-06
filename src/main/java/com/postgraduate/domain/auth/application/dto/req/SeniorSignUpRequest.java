package com.postgraduate.domain.auth.application.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record SeniorSignUpRequest(@NotEmpty Long socialId, @NotBlank String phoneNumber, @NotBlank String nickName,
                                  @NotBlank Boolean marketingReceive, @NotBlank String major, @NotBlank String postgradu,
                                  @NotBlank String professor, @NotBlank String lab, @NotBlank String field,
                                  @NotBlank String keyword, @NotBlank String certification) {
}
