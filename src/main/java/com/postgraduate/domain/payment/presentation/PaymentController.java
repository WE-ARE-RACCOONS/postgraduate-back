package com.postgraduate.domain.payment.presentation;

import com.postgraduate.domain.payment.application.dto.req.PaymentResultRequest;
import com.postgraduate.domain.payment.application.usecase.PaymentManageUseCase;
import com.postgraduate.domain.senior.application.usecase.SeniorInfoUseCase;
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
@RequestMapping("/payment")
@Tag(name = "PAYMENT Controller", description = "")
public class PaymentController {
    private final PaymentManageUseCase paymentManageUseCase;
    private final SeniorInfoUseCase seniorInfoUseCase;
    @Value("${payple.redirect-uri}")
    private String redirectUri;
    @Value("${payple.redirect-uri-dev}")
    private String redirectUriDev;
    @Value("${payple.cancel-redirect-uri}")
    private String cancelRedirectUri;
    @Value("${payple.cancel-redirect-uri-dev}")
    private String cancelRedirectUriDev;

    @PostMapping("/payple/result")
    public void resultGet(HttpServletResponse response, @ModelAttribute PaymentResultRequest request) throws IOException {
        if (paymentManageUseCase.savePay(request)) {
            response.sendRedirect(redirectUri + request.PCD_PAY_OID());
            return;
        }
        Long seniorId = seniorInfoUseCase.getSeniorId(request.PCD_PAY_GOODS());
        response.sendRedirect(cancelRedirectUri + seniorId);
    }

    @PostMapping("/payple/dev/result")
    public void resultGetWithDev(HttpServletResponse response, @ModelAttribute PaymentResultRequest request) throws IOException {
        if (paymentManageUseCase.savePay(request)) {
            response.sendRedirect(redirectUriDev + request.PCD_PAY_OID());
            return;
        }
        Long seniorId = seniorInfoUseCase.getSeniorId(request.PCD_PAY_GOODS());
        response.sendRedirect(cancelRedirectUriDev + seniorId);
    }

    @PostMapping("/webhook")
    public void webhook(@RequestBody PaymentResultRequest request) {
        log.info("payple webhook");
    }
}
