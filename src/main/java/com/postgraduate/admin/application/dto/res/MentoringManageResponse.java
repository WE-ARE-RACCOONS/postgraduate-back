package com.postgraduate.admin.application.dto.res;

import java.util.List;

public record MentoringManageResponse(
        List<MentoringInfo> mentoringInfo,
        UserMentoringInfo userMentoringInfo
) {
}
