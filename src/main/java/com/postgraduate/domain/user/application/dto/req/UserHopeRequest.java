package com.postgraduate.domain.user.application.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserHopeRequest {
    private String major;
    private String field;
    private boolean receive;
}
