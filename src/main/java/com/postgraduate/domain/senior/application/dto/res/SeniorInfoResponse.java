package com.postgraduate.domain.senior.application.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class SeniorInfoResponse {
    private String nickName;
    private String profile;
    private boolean certificationRegister;
    private boolean profileRegister;
}
