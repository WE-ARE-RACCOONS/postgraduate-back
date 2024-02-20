package com.postgraduate.domain.mentoring.application.dto;

public record ExpectedMentoringInfo(Long mentoringId, Long seniorId, String profile, String nickName, String postgradu,
                                    String major, String lab, String date, int term, String chatLink) {
}