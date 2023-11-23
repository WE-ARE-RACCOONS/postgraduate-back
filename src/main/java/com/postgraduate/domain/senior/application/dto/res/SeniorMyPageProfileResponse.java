package com.postgraduate.domain.senior.application.dto.res;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class SeniorMyPageProfileResponse {
    private String lab;
    private String[] keyword;
    private String info;
    private String target;
    private String chatLink;
    private String[] field;
    private String oneLiner;
}
