package com.postgraduate.domain.auth.application.dto.req;

import jakarta.validation.constraints.NotNull;

public record SeniorChangeRequest(@NotNull String major, @NotNull String postgradu, @NotNull String professor,
                                  @NotNull String lab, @NotNull String field, @NotNull String keyword, @NotNull String certification) {
}