package com.postgraduate.domain.admin.application.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
@AllArgsConstructor
public class UserResponse {
    private Long userId;
    private String nickName;
    private LocalDate createdAt;
    private boolean isSenior;
    // 마케팅 수신 도으이
    // 매칭문자 수신 동의
}
