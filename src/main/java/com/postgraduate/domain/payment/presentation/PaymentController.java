package com.postgraduate.domain.payment.presentation;

import com.postgraduate.domain.mentoring.application.dto.req.MentoringApplyRequest;
import com.postgraduate.domain.payment.application.dto.req.PaymentResultRequest;
import com.postgraduate.domain.mentoring.application.usecase.MentoringApplyUseCase;
import com.postgraduate.domain.payment.application.dto.req.PaymentResultWithMentoringRequest;
import com.postgraduate.domain.payment.application.usecase.PaymentManageUseCase;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.postgraduate.domain.payment.presentation.constant.PaymentResponseCode.PAYMENT_CREATE;
import static com.postgraduate.domain.payment.presentation.constant.PaymentResponseMessage.CREATE_PAYMENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
@Tag(name = "PAYMENT Controller", description = "")
public class PaymentController {
    private final PaymentManageUseCase paymentManageUseCase;
    private final MentoringApplyUseCase mentoringApplyUseCase;

    @PostMapping("/result")
    public ResponseDto resultGet(@ModelAttribute PaymentResultRequest request) {
        paymentManageUseCase.savePay(request);
        return ResponseDto.create(PAYMENT_CREATE.getCode(), CREATE_PAYMENT.getMessage());
    }

    @PostMapping("/mentoring")
    public ResponseDto applyForMentoringWithPayment(@AuthenticationPrincipal User user, @RequestBody PaymentResultWithMentoringRequest request) {
        Payment payment = paymentManageUseCase.savePay(request);
        mentoringApplyUseCase.applyMentoringWithPayment(user, payment, request.mentoringApplyRequest());
        return ResponseDto.create(PAYMENT_CREATE.getCode(), CREATE_PAYMENT.getMessage());
    }
}
