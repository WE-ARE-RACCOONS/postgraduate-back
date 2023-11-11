package com.postgraduate.domain.admin.application.dto.res;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class SeniorResponse {
    private Long seniorId;
    private String nickName;
    private LocalDate createdAt;
}
