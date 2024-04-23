package com.postgraduate.domain.auth.application.dto.req;

import jakarta.validation.constraints.NotBlank;

public record SeniorChangeRequestB(@NotBlank String major, @NotBlank String postgradu, @NotBlank String professor,
                                   @NotBlank String lab, @NotBlank String field, @NotBlank String keyword) {
}