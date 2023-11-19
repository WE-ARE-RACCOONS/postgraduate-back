package com.postgraduate.domain.senior.application.dto.res;

import com.postgraduate.domain.senior.domain.entity.constant.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SeniorDetailResponse {
    private String nickName;
    private String profile;
    private Status certificationRegister;
    private String postgradu;
    private String field;
    private String lab;
    private String professor;
    private String keyword;
    private String info;
    private String target;
    private String time;
}
