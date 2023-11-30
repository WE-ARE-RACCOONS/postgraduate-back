package com.postgraduate.domain.admin.application.dto.res;

import com.postgraduate.domain.senior.domain.entity.constant.Status;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SeniorResponse {
    private Long seniorId;
    private String nickName;
    private String phoneNumber;
    private Status status;
    private String salaryStatus;
    private Boolean marketingReceive;
    // 후배 회원 가입
}
