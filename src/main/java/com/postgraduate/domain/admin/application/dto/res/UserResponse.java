package com.postgraduate.domain.admin.application.dto.res;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@SuperBuilder
public class UserResponse {
    private Long userId;
    private String nickName;
    private LocalDate createdAt;
    // 마케팅 수신 도으이
    // 매칭문자 수신 동의
}
