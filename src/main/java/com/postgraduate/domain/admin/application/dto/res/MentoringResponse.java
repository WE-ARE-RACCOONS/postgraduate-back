package com.postgraduate.domain.admin.application.dto.res;

import com.postgraduate.domain.mentoring.domain.entity.constant.Status;

public record MentoringResponse(
        Long mentoringId,
        Status status,
        String userNickName,
        String userPhoneNumber,
        String seniorNickName,
        String seniorPhoneNumber
) {
}
