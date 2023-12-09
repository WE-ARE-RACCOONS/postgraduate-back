package com.postgraduate.domain.admin.application.dto.res;

import com.postgraduate.domain.admin.application.dto.MentoringInfo;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record MentoringManageResponse(@NotNull List<MentoringInfo> mentoringInfo) {
}
