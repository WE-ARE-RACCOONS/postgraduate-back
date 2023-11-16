package com.postgraduate.domain.refuse.application.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MentoringRefuseRequest {
    @NotNull
    private String reason;
}
