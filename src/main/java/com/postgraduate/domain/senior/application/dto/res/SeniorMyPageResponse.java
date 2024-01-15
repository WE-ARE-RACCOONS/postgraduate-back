package com.postgraduate.domain.senior.application.dto.res;

import com.postgraduate.domain.senior.domain.entity.constant.Status;

public record SeniorMyPageResponse(Long seniorId, Long socialId, String nickName, String profile,
                                   Status certificationRegister, Boolean profileRegister) {

}
