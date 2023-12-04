package com.postgraduate.domain.admin.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class CertificationInfo {
    private String certification;
    private String nickName;
    private String phoneNumber;
    //인증 신청 날짜
    private String postgradu;
    private String major;
    private String field;
    private String lab;
    private String professor;
    private String keyword;
}
