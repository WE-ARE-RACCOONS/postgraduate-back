package com.postgraduate.domain.senior.application.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SeniorSignUpRequest {
    @NotNull
    private String college;
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
    private String certification;
    @NotNull
    private String account;
    @NotNull
    private String bank;
    @NotNull
    private String rrn;
    @NotNull
    private String info;
    @NotNull
    private String target;
    @NotNull
    private String chatLink;
//    @NotNull
//    private int term;
    @NotNull
    private String time;
}
