package com.postgraduate.domain.admin.application.dto;

import com.postgraduate.domain.wish.domain.entity.constant.Status;
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
        Status matchingStatus,
        @NotNull
        Boolean isSenior
) { }
