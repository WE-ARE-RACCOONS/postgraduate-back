package com.postgraduate.domain.payment.presentation;

import com.postgraduate.domain.payment.application.dto.req.PaymentResultRequest;
import com.postgraduate.domain.payment.application.usecase.PaymentManageUseCase;
import com.postgraduate.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.postgraduate.domain.payment.presentation.constant.PaymentResponseCode.PAYMENT_CREATE;
import static com.postgraduate.domain.payment.presentation.constant.PaymentResponseMessage.CREATE_PAYMENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
@Tag(name = "PAYMENT Controller", description = "")
public class PaymentController {
    private final PaymentManageUseCase paymentManageUseCase;

    @PostMapping("/payple/result")
    public ResponseDto resultGet(@ModelAttribute PaymentResultRequest request) {
        paymentManageUseCase.savePay(request);
        return ResponseDto.create(PAYMENT_CREATE.getCode(), CREATE_PAYMENT.getMessage());
    }

    @PostMapping("/webhook")
    public void webhook(@RequestBody PaymentResultRequest request) {
        System.out.println(request.PCD_PAY_RST());
        System.out.println(request.PCD_PAY_CARDAUTHNO());
    }
}
