package com.postgraduate.domain.admin.application.dto;

public record UserMentoringInfo(
        String nickName,
        String phoneNumber
) {
    public UserMentoringInfo(String phoneNumber) {
        this(null, phoneNumber);
    }
}
