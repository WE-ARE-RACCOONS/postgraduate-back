package com.postgraduate.domain.senior.application.dto.res;

import jakarta.validation.constraints.NotNull;

public record SeniorMyPageUserAccountResponse(@NotNull String profile, @NotNull String phoneNumber, @NotNull String nickName, @NotNull String bank, @NotNull String accountNumber,
                                              @NotNull String accountHolder) {
    public SeniorMyPageUserAccountResponse(String profile, String phoneNumber, String nickName) {
        this(profile, phoneNumber, nickName, null, null, null);
    }
}
