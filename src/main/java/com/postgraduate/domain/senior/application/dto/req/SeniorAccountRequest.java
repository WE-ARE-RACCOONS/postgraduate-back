package com.postgraduate.domain.senior.application.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SeniorAccountRequest {
    @NotNull
    private String accountNumber;
    @NotNull
    private String bank;
    @NotNull
    private String accountHolder;
    @NotNull
    private String name;
    @NotNull
    private String rrn;
}
