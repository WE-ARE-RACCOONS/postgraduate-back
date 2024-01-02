package com.postgraduate.domain.user.application.dto.req;

import jakarta.validation.constraints.NotBlank;

public record UserInfoRequest(@NotBlank String profile, @NotBlank String nickName, @NotBlank String phoneNumber) {
}
