package com.postgraduate.domain.mentoring.application.dto;

import java.time.LocalDate;

public record DoneSeniorMentoringInfo(Long mentoringId, String profile, String nickName, int term, String date,
                                      LocalDate salaryDate, Boolean status) {
}
