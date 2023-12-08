package com.postgraduate.domain.admin.application.dto.res;

import com.postgraduate.domain.user.domain.entity.constant.Role;

import java.time.LocalDate;

public record UserResponse(
        Long userId,
        String nickName,
        String phoneNumber,
        LocalDate createdAt,
        Boolean marketingReceive,
        Boolean matchingReceive,
        Long wishId,
        Role role
) { }
