package com.postgraduate.domain.admin.application.dto.res;

import com.postgraduate.domain.admin.application.dto.UserInfo;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UserManageResponse(@NotNull List<UserInfo> userInfo) {
}
