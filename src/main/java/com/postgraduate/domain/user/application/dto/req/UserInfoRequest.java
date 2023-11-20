package com.postgraduate.domain.user.application.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoRequest {
    @NotNull
    private String profile;
    @NotNull
    private String nickName;
    @NotNull
    private String phoneNumber;
}
