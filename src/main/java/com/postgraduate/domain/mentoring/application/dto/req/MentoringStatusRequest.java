package com.postgraduate.domain.mentoring.application.dto.req;

import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MentoringStatusRequest {
    @NotNull
    private Status status;
}
