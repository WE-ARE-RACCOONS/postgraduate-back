package com.postgraduate.domain.admin.application.dto.res;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class UserWithSeniorResponse extends UserResponse {
    private Long seniorId;
}
