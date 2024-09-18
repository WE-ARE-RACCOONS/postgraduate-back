package com.postgraduate.global.auth.login.application.dto.req;

import jakarta.validation.constraints.NotBlank;

public record CodeRequest(@NotBlank String code) {
}
