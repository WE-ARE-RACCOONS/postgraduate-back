package com.postgraduate.domain.payment.presentation;

import com.postgraduate.domain.payment.application.dto.req.PaymentResultRequest;
import com.postgraduate.domain.payment.application.usecase.PaymentManageUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
// @RequestMapping("/payment")
@Tag(name = "PAYMENT Controller", description = "")
public class PaymentController {
    private final PaymentManageUseCase paymentManageUseCase;
    @Value("${payple.redirect-uri}")
    private String redirectUri;
    @Value("${payple.redirect-uri-dev}")
    private String redirectUriDev;

    @PostMapping("/payple/result")
    public void resultGet(HttpServletResponse response, @ModelAttribute PaymentResultRequest request) throws IOException {
        paymentManageUseCase.savePay(request);
        response.sendRedirect(redirectUri + request.PCD_PAY_OID());
    }

    @PostMapping("/payple/dev/result")
    public void resultGetWithDev(HttpServletResponse response, @ModelAttribute PaymentResultRequest request) throws IOException {
        paymentManageUseCase.savePay(request);
        response.sendRedirect(redirectUriDev + request.PCD_PAY_OID());
    }

    @PostMapping("/webhook")
    public void webhook(@RequestBody PaymentResultRequest request) {
        log.info("payple webhook");
    }
}
