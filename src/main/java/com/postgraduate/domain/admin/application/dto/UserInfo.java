package com.postgraduate.domain.admin.application.dto;

import com.postgraduate.domain.wish.domain.entity.constant.Status;

import java.time.LocalDateTime;

public record UserInfo(
        Long userId,
        String nickName,
        String phoneNumber,
        LocalDateTime createdAt,
        Boolean marketingReceive,
        Boolean matchingReceive,
        Long wishId,
        Status matchingStatus,
        Boolean isSenior
) { }
