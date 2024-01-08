package com.postgraduate.domain.admin.application.dto.res;

import com.postgraduate.domain.admin.application.dto.UserInfo;

import java.util.List;

public record UserManageResponse(
        List<UserInfo> userInfo,
        Long totalElements,
        Integer totalPages
) {
}
