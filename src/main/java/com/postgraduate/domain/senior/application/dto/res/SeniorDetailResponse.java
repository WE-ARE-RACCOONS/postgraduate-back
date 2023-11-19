package com.postgraduate.domain.senior.application.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SeniorDetailResponse {
    private String nickName;
    private String profile;
    private String postgradu;
    private String major;
    private String lab;
    private String professor;
    private String[] keyword;
    private String info;
    private String oneLiner;
    private String target;
    private String time;
}
