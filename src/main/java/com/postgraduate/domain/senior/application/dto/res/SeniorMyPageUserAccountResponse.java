package com.postgraduate.domain.senior.application.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class SeniorMyPageUserAccountResponse {
    private final String profile;
    private final String nickName;
    private final String bank;
    private final String accountNumber;
    private final String accountHolder;
}
