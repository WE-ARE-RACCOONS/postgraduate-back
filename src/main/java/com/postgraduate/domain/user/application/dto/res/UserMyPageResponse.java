package com.postgraduate.domain.user.application.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserMyPageResponse {
    private String nickName;
    private String profile;
}