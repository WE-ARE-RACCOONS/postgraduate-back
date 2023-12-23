package com.postgraduate.domain.admin.application.dto.res;

import com.postgraduate.domain.admin.application.dto.MentoringInfo;
import com.postgraduate.domain.admin.application.dto.UserMentoringInfo;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record MentoringManageResponse(
        @NotNull
        List<MentoringInfo> mentoringInfo,
        @NotNull
        UserMentoringInfo userMentoringInfo
) {
}
