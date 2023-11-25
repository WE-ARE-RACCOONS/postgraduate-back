package com.postgraduate.domain.auth.application.dto.req;

import jakarta.validation.constraints.NotNull;

public record SeniorSignUpRequest(@NotNull Long socialId, @NotNull String phoneNumber, @NotNull String nickName,
                                  @NotNull Boolean marketingReceive, @NotNull String major, @NotNull String postgradu,
                                  @NotNull String professor, @NotNull String lab, @NotNull String field,
                                  @NotNull String keyword, @NotNull String certification) {
}
