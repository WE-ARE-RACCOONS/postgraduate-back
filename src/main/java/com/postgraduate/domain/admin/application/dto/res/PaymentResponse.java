package com.postgraduate.domain.admin.application.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
@AllArgsConstructor
public class PaymentResponse {
    private Long paymentId;
    private Long mentoringId;
    private String userNickName;
    private String seniorNickName;
    private LocalDate createdAt;
    private Integer pay;
}
