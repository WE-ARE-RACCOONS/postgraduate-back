package com.postgraduate.global.auth.login.application.dto.req;

import com.postgraduate.global.auth.quit.domain.entity.constant.QuitReason;

public record SignOutRequest (
    QuitReason reason,
    String etc
){}
