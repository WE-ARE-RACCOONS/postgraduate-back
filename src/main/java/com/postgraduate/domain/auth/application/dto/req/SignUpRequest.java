package com.postgraduate.domain.auth.application.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @NotNull
        Long socialId,
        @NotBlank
        String phoneNumber,
        @NotBlank
        @Size(max = 6, message = "6글자까지 입력 가능합니다.")
        String nickName,
        boolean marketingReceive,
        String major,
        String field,
        boolean matchingReceive
) {
}
