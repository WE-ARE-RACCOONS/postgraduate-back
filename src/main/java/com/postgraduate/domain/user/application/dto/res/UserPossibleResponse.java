package com.postgraduate.domain.user.application.dto.res;

import jakarta.validation.constraints.NotNull;

public record UserPossibleResponse(@NotNull Boolean possible, @NotNull Long socialId) {}
