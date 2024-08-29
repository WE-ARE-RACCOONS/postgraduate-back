package com.postgraduate.domain.member.senior.application.dto.res;

public record SeniorMyPageUserAccountResponse(String profile, String phoneNumber, String nickName, String bank, String accountNumber,
                                              String accountHolder) {
    public SeniorMyPageUserAccountResponse(String profile, String phoneNumber, String nickName) {
        this(profile, phoneNumber, nickName, null, null, null);
    }
}
