package com.postgraduate.domain.admin.application.dto.res;

import com.postgraduate.domain.admin.application.dto.MentoringInfo;
import com.postgraduate.domain.admin.application.dto.UserMentoringInfo;
import java.util.List;

public record MentoringManageResponse(
        List<MentoringInfo> mentoringInfo,
        UserMentoringInfo userMentoringInfo
) {
}
