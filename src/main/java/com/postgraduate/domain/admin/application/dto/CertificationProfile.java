package com.postgraduate.domain.admin.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class CertificationProfile {
    private Integer term;
    private String time;
    //연구실 대표 키워드
}
