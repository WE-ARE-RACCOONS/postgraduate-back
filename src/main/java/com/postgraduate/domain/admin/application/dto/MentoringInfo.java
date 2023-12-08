package com.postgraduate.domain.admin.application.dto;

import com.postgraduate.domain.mentoring.domain.entity.constant.Status;

public record MentoringInfo(
        Long mentoringId,
        Status status,
        String userNickName,
        String userPhoneNumber,
        String seniorNickName,
        String seniorPhoneNumber
) {
}
