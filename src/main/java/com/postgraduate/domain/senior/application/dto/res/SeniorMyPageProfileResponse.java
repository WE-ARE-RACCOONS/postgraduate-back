package com.postgraduate.domain.senior.application.dto.res;


import jakarta.validation.constraints.NotNull;

public record SeniorMyPageProfileResponse(
        @NotNull
        String lab,
        @NotNull
        String[] keyword,
        @NotNull
        String info,
        @NotNull
        String target,
        @NotNull
        String chatLink,
        @NotNull
        String[] field,
        @NotNull
        String oneLiner,
        @NotNull
        String time
) { }
