package com.postgraduate.domain.admin.application.dto.res;

import com.postgraduate.domain.salary.domain.entity.constant.SalaryStatus;
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
    private SalaryStatus salaryStatus;
    private Boolean marketingReceive;
    private Boolean isUser;
}
