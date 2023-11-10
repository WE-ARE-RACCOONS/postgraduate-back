package com.postgraduate.domain.admin.application.dto.res;

import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
@AllArgsConstructor
public class MentoringResponse {
    private Long mentoringId;
    private Status status;
    private String userNickName;
    private Long seniorId;
    private String seniorNickName;
    private LocalDate createdAt;
    private String date;
}
