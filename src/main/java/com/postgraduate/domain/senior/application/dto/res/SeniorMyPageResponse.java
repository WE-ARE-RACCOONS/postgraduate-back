package com.postgraduate.domain.senior.application.dto.res;

import com.postgraduate.domain.senior.domain.entity.constant.Status;
import jakarta.validation.constraints.NotNull;

public record SeniorMyPageResponse(@NotNull Long seniorId, @NotNull String nickName, @NotNull String profile,
                                   @NotNull Status certificationRegister, @NotNull Boolean profileRegister) {

}
