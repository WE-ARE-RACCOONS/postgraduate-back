package com.postgraduate.domain.senior.application.dto.res;

import com.postgraduate.domain.senior.domain.entity.constant.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class SeniorInfoResponse {
    private String nickName;
    private String profile;
    private int amount;
    private String month;
    private Status certificationRegister;
    private boolean profileRegister;
}
