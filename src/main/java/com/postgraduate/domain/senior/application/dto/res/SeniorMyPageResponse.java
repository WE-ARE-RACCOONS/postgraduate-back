package com.postgraduate.domain.senior.application.dto.res;

import com.postgraduate.domain.senior.domain.entity.constant.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class SeniorMyPageResponse {
    private String nickName;
    private String profile;
    private Status certificationRegister;
    private boolean profileRegister;
}
