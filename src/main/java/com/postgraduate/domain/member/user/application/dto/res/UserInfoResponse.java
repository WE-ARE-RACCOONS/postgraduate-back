package com.postgraduate.domain.member.user.application.dto.res;

public record UserInfoResponse(
        String profile,
        String nickName,
        String phoneNumber
) { }
