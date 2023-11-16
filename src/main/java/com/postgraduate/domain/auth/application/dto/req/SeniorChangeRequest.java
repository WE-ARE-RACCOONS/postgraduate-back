package com.postgraduate.domain.auth.application.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SeniorChangeRequest {
    @NotNull
    private String major;
    @NotNull
    private String postgradu;
    @NotNull
    private String professor;
    @NotNull
    private String lab;
    @NotNull
    private String field;
    @NotNull
    private String keyword;
    @NotNull
    private String certification;
}