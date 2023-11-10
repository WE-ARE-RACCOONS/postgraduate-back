package com.postgraduate.domain.admin.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class CertificationInfo {
    private String nickName;
    private String certification;
    private String postgradu;
    private String field;
    private String professor;
    private Integer term;
    private String info;
    private String time;
}
