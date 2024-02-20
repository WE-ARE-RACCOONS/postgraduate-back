package com.postgraduate.domain.mentoring.application.dto;

public record WaitingMentoringInfo(Long mentoringId, Long seniorId, String profile, String nickName, String postgradu,
                                   String major, String lab, int term) {
}