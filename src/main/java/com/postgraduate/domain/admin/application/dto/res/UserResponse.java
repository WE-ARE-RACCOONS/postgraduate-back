package com.postgraduate.domain.admin.application.dto.res;

import com.postgraduate.domain.user.domain.entity.constant.Role;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@SuperBuilder
public class UserResponse {
    private Long userId;
    private String nickName;
    private String phoneNumber;
    private LocalDate createdAt;
    private Boolean marketingReceive;
    private Boolean matchingReceive;
    private Long wishId;
    private Role role;
    // 매칭 지원 완료 여부
}
