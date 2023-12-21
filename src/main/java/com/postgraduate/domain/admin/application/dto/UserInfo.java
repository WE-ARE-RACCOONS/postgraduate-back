package com.postgraduate.domain.admin.application.dto;

import com.postgraduate.domain.user.domain.entity.constant.Role;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record UserInfo(
        @NotNull
        Long userId,
        @NotNull
        String nickName,
        @NotNull
        String phoneNumber,
        @NotNull
        LocalDateTime createdAt,
        @NotNull
        Boolean marketingReceive,
        @NotNull
        Boolean matchingReceive,
        @NotNull
        Long wishId,
        @NotNull
        Role role
) { }
