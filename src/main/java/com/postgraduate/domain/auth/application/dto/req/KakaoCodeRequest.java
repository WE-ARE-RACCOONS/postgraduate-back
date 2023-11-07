package com.postgraduate.domain.auth.application.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class KakaoCodeRequest {
    @NotNull
    private String code;
}
