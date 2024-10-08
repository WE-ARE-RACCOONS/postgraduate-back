package com.postgraduate.global.auth.login.application.dto.req;

import jakarta.validation.constraints.NotBlank;

public record SeniorChangeRequest(@NotBlank String major, @NotBlank String postgradu, @NotBlank String professor,
                                  @NotBlank String lab, @NotBlank String field, @NotBlank String keyword, @NotBlank String chatLink) {
}