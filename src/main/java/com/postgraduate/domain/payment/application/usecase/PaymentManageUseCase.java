package com.postgraduate.domain.payment.application.usecase;

import com.postgraduate.domain.admin.application.dto.res.CertificationResponse;
import com.postgraduate.domain.admin.application.dto.res.RefundResponse;
import com.postgraduate.domain.payment.application.dto.req.PaymentResultRequest;
import com.postgraduate.domain.payment.application.mapper.PaymentMapper;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.service.PaymentGetService;
import com.postgraduate.domain.payment.domain.service.PaymentSaveService;
import com.postgraduate.domain.payment.domain.service.PaymentUpdateService;
import com.postgraduate.domain.payment.exception.CertificationFailException;
import com.postgraduate.domain.payment.exception.PaymentFailException;
import com.postgraduate.domain.payment.exception.RefundFailException;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.salary.domain.service.SalaryGetService;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.service.UserGetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.postgraduate.domain.payment.application.usecase.PaymentParameter.*;
import static org.springframework.http.CacheControl.noCache;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PaymentManageUseCase {
    @Value("${payple.refund.certification-uri}")
    private String CERTIFICATION_URI;
    @Value("${payple.refund.refund-uri}")
    private String REFUND_URI;
    @Value("${payple.refund.redirect-uri}")
    private String REFUND_REDIRECT_URI;
    @Value("${payple.cst-id}")
    private String CUSTOMER_ID;
    @Value("${payple.cust-key}")
    private String CUSTOMER_KEY;
    @Value("${payple.refund.clientKey}")
    private String CLIENT_KEY;
    @Value("${payple.refund.refundKey}")
    private String REFUND_KEY;
    @Value("${payple.refund.refundFlag}")
    private String REFUND_FLAG;

    private final PaymentSaveService paymentSaveService;
    private final PaymentGetService paymentGetService;
    private final PaymentUpdateService paymentUpdateService;
    private final SeniorGetService seniorGetService;
    private final UserGetService userGetService;
    private final SalaryGetService salaryGetService;
    private final WebClient webClient;

    public void savePay(PaymentResultRequest request) {
        try {
            if (!request.PCD_PAY_RST().equals(SUCCESS.getName()))
                throw new PaymentFailException();
            String seniorNickName = request.PCD_PAY_GOODS();
            long userId = Long.parseLong(request.PCD_PAYER_NO());
            User user = userGetService.byUserId(userId);
            Senior senior = seniorGetService.bySeniorNickName(seniorNickName);
            Salary salary = salaryGetService.bySeniorWithNull(senior);
            Payment payment = PaymentMapper.resultToPayment(salary, user, request);
            paymentSaveService.save(payment);
        } catch (PaymentFailException ex) {
            log.error("paymentError 발생 {}", ex.getMessage());
        }
    }

    public void refundPay(User user, String orderId) {
        Payment payment = paymentGetService.byUserAndOrderId(user, orderId);
        CertificationResponse certificationResponse = getCertificationResponse().orElseThrow();
        refundProcess(certificationResponse, payment);
        paymentUpdateService.updateCancel(payment);
    }

    private Optional<CertificationResponse> getCertificationResponse() {
        return Optional.ofNullable(webClient.post()
                .uri(CERTIFICATION_URI)
                .headers(h -> {
                    h.setContentType(MediaType.APPLICATION_JSON);
                    h.set(REFERER.getName(), REFUND_REDIRECT_URI);
                })
                .bodyValue(getCertificationRequestBody())
                .retrieve()
                .bodyToMono(CertificationResponse.class)
                .block());
    }

    private MultiValueMap<String, String> getCertificationRequestBody() {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add(CST_ID.getName(), CUSTOMER_ID);
        requestBody.add(CUST_KEY.getName(), CUSTOMER_KEY);
        requestBody.add(FLAG.getName(), REFUND_FLAG);
        return requestBody;
    }

    private void refundProcess(CertificationResponse response, Payment payment) {
        if (!response.result().equals(SUCCESS.getName()))
            throw new CertificationFailException();
        RefundResponse refundResponse = Optional.ofNullable(webClient.post()
                .uri(REFUND_URI + response.PCD_PAY_URL())
                .headers(h -> {
                    h.setContentType(MediaType.APPLICATION_JSON);
                    h.setCacheControl(noCache());
                    h.set(REFERER.getName(), REFUND_REDIRECT_URI);
                })
                .bodyValue(getRefundRequestBody(response, payment))
                .retrieve()
                .bodyToMono(RefundResponse.class)
                .block())
                .orElseThrow();
        if (!refundResponse.PCD_PAY_RST().equals(SUCCESS.getName()))
            throw new RefundFailException(refundResponse.PCD_PAY_CODE());
    }

    private MultiValueMap<String, String> getRefundRequestBody(CertificationResponse response, Payment payment) {
        String paidAt = payment.getPaidAt().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add(CST_ID.getName(), response.cst_id());
        requestBody.add(CUST_KEY.getName(), response.custKey());
        requestBody.add(AUTH_KEY.getName(), response.AuthKey());
        requestBody.add(REF_KEY.getName(), REFUND_KEY);
        requestBody.add(PAYCANCEL_FLAG.getName(), REFUND_FLAG);
        requestBody.add(OID.getName(), payment.getOrderId());
        requestBody.add(DATE.getName(), paidAt);
        requestBody.add(TOTAL.getName(), String.valueOf(payment.getPay()));
        return requestBody;
    }

}
