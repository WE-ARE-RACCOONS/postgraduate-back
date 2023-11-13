package com.postgraduate.domain.admin.application.dto.req;

import com.postgraduate.domain.senior.domain.entity.constant.Status;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SeniorStatusRequest {
    @NotNull
    private Status status;
}
