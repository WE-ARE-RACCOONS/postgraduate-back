package com.postgraduate.domain.auth.application.dto;

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
    private String nickName;
}
