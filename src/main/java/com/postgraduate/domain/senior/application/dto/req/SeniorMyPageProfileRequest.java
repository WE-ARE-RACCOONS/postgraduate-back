package com.postgraduate.domain.senior.application.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SeniorMyPageProfileRequest {
    @NotNull
    private String lab;
    @NotNull
    private String keyword;
    @NotNull
    private String info;
    @NotNull
    private String target;
    @NotNull
    private String chatLink;
    @NotNull
    private String field;
    @NotNull
    private String oneLiner;
}
