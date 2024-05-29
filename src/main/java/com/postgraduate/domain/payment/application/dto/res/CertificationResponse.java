package com.postgraduate.domain.payment.application.dto.res;

public record CertificationResponse (
        String result,
        String result_msg,
        String cst_id,
        String custKey,
        String AuthKey,
        String PCD_PAY_URL,
        String return_url
){ }
