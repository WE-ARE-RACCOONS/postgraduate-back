package com.postgraduate.domain.payment.presentation;

import com.postgraduate.domain.payment.application.dto.req.PaymentResultRequest;
import com.postgraduate.domain.payment.application.usecase.PaymentManageUseCase;
import com.postgraduate.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
@Tag(name = "PAYMENT Controller", description = "")
public class PaymentController {
    private final PaymentManageUseCase paymentManageUseCase;

    @PostMapping("/order/result")
    public ResponseDto resultGet(@ModelAttribute PaymentResultRequest request) {
        paymentManageUseCase.savePay(request);
        return ResponseDto.create("a","a");
    }
}
