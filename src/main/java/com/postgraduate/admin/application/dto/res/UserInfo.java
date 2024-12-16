package com.postgraduate.admin.application.dto.res;

import java.time.LocalDateTime;

public record UserInfo(
        Long userId,
        String nickName,
        String phoneNumber,
        LocalDateTime createdAt,
        Boolean marketinRgeceive,
        Boolean isSenior
) { }
