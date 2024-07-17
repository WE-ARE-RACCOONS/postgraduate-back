package com.postgraduate.domain.auth.application.dto.req;

import com.postgraduate.domain.user.domain.entity.constant.QuitReason;

public record SignOutRequest (
    QuitReason reason,
    String etc
){}
