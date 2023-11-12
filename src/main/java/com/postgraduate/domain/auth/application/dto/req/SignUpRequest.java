package com.postgraduate.domain.auth.application.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    @NotNull
    private Long socialId;
    @NotNull
    private String phoneNumber;
    @NotNull
    private String nickName;
    private String major;
    private String field;
    private Boolean receive;
}
