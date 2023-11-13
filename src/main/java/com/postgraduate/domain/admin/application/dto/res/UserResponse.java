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
    private Boolean marketingReceive;
    private Boolean matchingReceive;
}
