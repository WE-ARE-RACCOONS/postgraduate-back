package com.postgraduate.domain.payment.application.usecase;

import com.postgraduate.domain.payment.application.dto.res.CertificationResponse;
import com.postgraduate.domain.payment.application.dto.res.RefundResponse;
import com.postgraduate.domain.payment.application.dto.req.PaymentResultRequest;
import com.postgraduate.domain.payment.application.mapper.PaymentMapper;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.service.PaymentGetService;
import com.postgraduate.domain.payment.domain.service.PaymentSaveService;
import com.postgraduate.domain.payment.domain.service.PaymentUpdateService;
import com.postgraduate.domain.payment.exception.CertificationFailException;
import com.postgraduate.domain.payment.exception.RefundFailException;
import com.postgraduate.domain.member.senior.domain.entity.Senior;
import com.postgraduate.domain.member.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.member.user.domain.entity.User;
import com.postgraduate.domain.member.user.domain.service.UserGetService;
import com.postgraduate.global.slack.SlackPaymentMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.postgraduate.domain.payment.application.usecase.constant.PaymentParameter.*;
import static com.postgraduate.domain.payment.presentation.constant.PaymentResponseMessage.FAIL_PAYMENT;
import static com.postgraduate.domain.member.user.domain.entity.constant.Role.ADMIN;
import static org.springframework.http.CacheControl.noCache;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PaymentManageUseCase {
    @Value("${payple.refund.certification-uri}")
    private String certificationUri;
    @Value("${payple.refund.refund-uri}")
    private String refundUri;
    @Value("${payple.refund.referer-uri}")
    private String refererUri;
    @Value("${payple.cst-id}")
    private String custId;
    @Value("${payple.cust-key}")
    private String custKey;
    @Value("${payple.refund.refundKey}")
    private String refundKey;
    @Value("${payple.refund.refundFlag}")
    private String refundFlag;

    private final PaymentSaveService paymentSaveService;
    private final PaymentGetService paymentGetService;
    private final PaymentUpdateService paymentUpdateService;
    private final SeniorGetService seniorGetService;
    private final UserGetService userGetService;
    private final WebClient webClient;
    private final SlackPaymentMessage slackPaymentMessage;

    public void savePay(PaymentResultRequest request) {
        if (!request.PCD_PAY_RST().equals(SUCCESS.getName())) {
            log.error("PayPle 결제 진행 취소 및 오류");
            log.error("message : {} code : {}", FAIL_PAYMENT.getMessage(), request.PCD_PAY_CODE());
            return;
        }
        try {
            String seniorNickName = request.PCD_PAY_GOODS();
            long userId = Long.parseLong(request.PCD_PAYER_NO());
            User user = userGetService.byUserId(userId);
            Senior senior = seniorGetService.bySeniorNickName(seniorNickName);
            Payment payment = PaymentMapper.resultToPayment(senior, user, request);
            paymentSaveService.save(payment);
            slackPaymentMessage.sendPayment(payment);
        } catch (Exception ex) {
            log.error("paymentError 발생 환불 진행 | errorMessage : {}", ex.getMessage());
            Payment payment = PaymentMapper.resultToPayment(request);
            paymentSaveService.save(payment);
            refundPay(payment);
        }
    }

    public void refundPayByUser(User user, String orderId) {
        log.info("환불 진행");
        Payment payment = paymentGetService.byUserAndOrderId(user, orderId);
        refundPay(payment);
    }

    public void refundPayByUser(Long userId, Long paymentId) {
        log.info("환불 진행");
        Payment payment = paymentGetService.byUserIdAndOrderId(userId, paymentId);
        refundPay(payment);
    }

    public void refundPayBySenior(Senior senior, String orderId) {
        Payment payment = paymentGetService.bySeniorAndOrderId(senior, orderId);
        refundPay(payment);
    }

    public void refundPayByAdmin(User user, Long paymentId) {
        if (user.getRole() != ADMIN) {
            log.error("Refund Fail : NOT ADMIN");
            throw new RefundFailException();
        }
        Payment payment = paymentGetService.byId(paymentId);
        log.info("환불 진행 paymentId : {}", paymentId);
        refundPay(payment);
    }

    private void refundPay(Payment payment) {
        CertificationResponse certificationResponse = getCertificationResponse()
                .orElseThrow(() -> new CertificationFailException("NPE"));
        refundProcess(certificationResponse, payment);
        paymentUpdateService.updateCancel(payment);
    }

    private Optional<CertificationResponse> getCertificationResponse() {
        Map<String, String> requestBody = getCertificationRequestBody();
        return Optional.ofNullable(webClient.post()
                .uri(certificationUri)
                .headers(h -> {
                    h.setContentType(MediaType.APPLICATION_JSON);
                    h.setCacheControl(noCache());
                    h.set(REFERER.getName(), refererUri);
                })
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(CertificationResponse.class)
                .block());
    }

    private Map<String, String> getCertificationRequestBody() {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put(CUSTOMER_ID.getName(), custId);
        requestBody.put(CUSTOMER_KEY.getName(), custKey);
        requestBody.put(FLAG.getName(), refundFlag);
        return requestBody;
    }

    private void refundProcess(CertificationResponse response, Payment payment) {
        if (!response.result().equals(SUCCESS.getName())) {
            throw new CertificationFailException(response.result_msg());
        }
        Map<String, String> requestBody = getRefundRequestBody(response, payment);
        RefundResponse refundResponse = Optional.ofNullable(webClient.post()
                        .uri(refundUri + response.PCD_PAY_URL())
                        .headers(h -> {
                            h.setContentType(MediaType.APPLICATION_JSON);
                            h.setCacheControl(noCache());
                            h.set(REFERER.getName(), refererUri);
                        })
                        .bodyValue(requestBody)
                        .retrieve()
                        .bodyToMono(RefundResponse.class)
                        .block())
                .orElseThrow(() -> {
                    log.error("RefundFail : NPE");
                    throw new RefundFailException();
                });
        if (!refundResponse.PCD_PAY_RST().equals(SUCCESS.getName())) {
            log.error("Refund fail : {}", refundResponse.PCD_PAY_CODE());
            throw new RefundFailException();
        }
    }

    private Map<String, String> getRefundRequestBody(CertificationResponse response, Payment payment) {
        String paidAt = payment.getPaidAt().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put(CST_ID.getName(), response.cst_id());
        requestBody.put(CUST_KEY.getName(), response.custKey());
        requestBody.put(AUTH_KEY.getName(), response.AuthKey());
        requestBody.put(REF_KEY.getName(), refundKey);
        requestBody.put(PAYCANCEL_FLAG.getName(), refundFlag);
        requestBody.put(OID.getName(), payment.getOrderId());
        requestBody.put(DATE.getName(), paidAt);
        requestBody.put(TOTAL.getName(), String.valueOf(payment.getPay()));
        return requestBody;
    }

}
