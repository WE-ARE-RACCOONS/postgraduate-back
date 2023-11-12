package com.postgraduate.domain.user.application.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserHopeRequest {
    private String major;
    private String field;
    private boolean receive;
}
