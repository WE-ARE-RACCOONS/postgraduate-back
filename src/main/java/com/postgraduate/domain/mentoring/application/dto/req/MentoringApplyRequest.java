package com.postgraduate.domain.mentoring.application.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MentoringApplyRequest {
    @NotNull
    private Long seniorId;
    @NotNull
    private String topic;
    @NotNull
    private String question;
    @NotNull
    private String date;
}
