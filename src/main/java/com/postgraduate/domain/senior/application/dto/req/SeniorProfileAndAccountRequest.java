package com.postgraduate.domain.senior.application.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SeniorProfileAndAccountRequest {
    @NotNull
    private String info;
    @NotNull
    private String target;
    @NotNull
    private String chatLink;
    @NotNull
    private String time;
    @NotNull
    private String oneLiner;
    @NotNull
    private String keyword;
    @NotNull
    private String account;
    @NotNull
    private String bank;
}
