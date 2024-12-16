package com.postgraduate.admin.application.dto.res;

import java.util.List;

public record MentoringManageResponse(
        UserInfoBasic userInfoBasic,
        List<MentoringInfo> mentoringInfo
) {
}
