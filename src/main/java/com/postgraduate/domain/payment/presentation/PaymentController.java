package com.postgraduate.domain.payment.presentation;

import com.postgraduate.domain.payment.application.dto.req.PaymentResultRequest;
import com.postgraduate.domain.payment.application.usecase.PaymentManageUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
@Tag(name = "PAYMENT Controller", description = "")
public class PaymentController {
    private final PaymentManageUseCase paymentManageUseCase;

    @PostMapping("/payple/result")
    public void resultGet(HttpServletResponse response, @ModelAttribute PaymentResultRequest request) throws IOException {
        try {
            paymentManageUseCase.savePay(request);
            response.sendRedirect("https://kimseonbae-develop.vercel.app/result");
        }
        catch (Exception ex){
            response.sendRedirect("https://kimseonbae-develop.vercel.app/result");
        }
    }

//    @PostMapping("/webhook")
//    public void webhook(@RequestBody PaymentResultRequest request) {
//
//    }
}
