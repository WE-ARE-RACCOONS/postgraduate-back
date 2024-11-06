package com.postgraduate.domain.member.senior.application.dto.res;

import com.postgraduate.domain.member.senior.domain.entity.constant.Status;

public record SeniorMyPageResponse(Long socialId, Long seniorId, String nickName, String profile,
                                   Status certificationRegister, Boolean profileRegister) {

}
