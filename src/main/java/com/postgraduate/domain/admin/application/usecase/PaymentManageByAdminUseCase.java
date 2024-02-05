package com.postgraduate.domain.admin.application.usecase;

import com.postgraduate.domain.admin.application.dto.PaymentInfo;
import com.postgraduate.domain.admin.application.dto.res.CertificationResponse;
import com.postgraduate.domain.admin.application.dto.res.PaymentManageResponse;
import com.postgraduate.domain.admin.application.dto.res.RefundResponse;
import com.postgraduate.domain.admin.application.mapper.AdminMapper;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.service.MentoringGetService;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.service.PaymentUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static com.postgraduate.domain.payment.domain.entity.constant.Status.CANCEL;
import static org.springframework.http.CacheControl.noCache;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentManageByAdminUseCase {
    private static final String SUCCESS = "0000";
    private static final String CST_ID = "test";
    private static final String CUST_KEY = "abcd1234567890";
    private static final String REDIRECT_URL = "http://localhost:8080";
    private static final String REFUND_KEY = "a41ce010ede9fcbfb3be86b24858806596a9db68b79d138b147c3e563e1829a0";
    private static final String FLAG = "Y";
    private static final String REFUND_URL = "https://democpay.payple.kr";
    private static final String CERTIFICATION_URL = "https://democpay.payple.kr/php/auth.php";

    private final MentoringGetService mentoringGetService;
    private final PaymentUpdateService paymentUpdateService;
    private final WebClient webClient;

    public PaymentManageResponse getPayments(Integer page, String search) {
        Page<Mentoring> mentorings = mentoringGetService.all(page, search);
        List<PaymentInfo> paymentInfos = mentorings.map(AdminMapper::mapToPaymentInfo).toList();
        long totalElements = mentorings.getTotalElements();
        int totalPages = mentorings.getTotalPages();
        return new PaymentManageResponse(paymentInfos, totalElements, totalPages);
    }

    public void refundPayment(Long mentoringId) {
        Mentoring mentoring = mentoringGetService.byMentoringId(mentoringId);
        Payment payment = mentoring.getPayment();
        CertificationResponse certificationResponse = getCertificationResponse().orElseThrow();
        RefundResponse response = getRefundResponse(certificationResponse, payment).orElseThrow();
        if (!response.PCD_PAY_CODE().equals(SUCCESS))
            throw new IllegalArgumentException();
        paymentUpdateService.updateStatus(payment, CANCEL);
    }

    private Optional<RefundResponse> getRefundResponse(CertificationResponse response, Payment payment) {
        return Optional.ofNullable(webClient.post()
                .uri(response.PCD_PAY_URL())
                .headers(h -> {
                    h.setContentType(MediaType.APPLICATION_JSON);
                    h.setCacheControl(noCache());
                    h.set("referer", REDIRECT_URL);
                })
                .bodyValue(getRefundRequestBody(response, payment))
                .retrieve()
                .bodyToMono(RefundResponse.class)
                .block());
    }

    private MultiValueMap<String, String> getRefundRequestBody(CertificationResponse response, Payment payment) {
        String paidAt = payment.getPaidAt().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("PCD_CST_ID", response.cst_id());
        requestBody.add("PCD_CUST_KEY", response.custKey());
        requestBody.add("PCD_AUTH_KEY", response.AuthKey());
        requestBody.add("PCD_REFUND_KEY", REFUND_KEY);
        requestBody.add("PCD_PAYCANCEL_FLAG", FLAG);
        requestBody.add("PCD_PAY_OID", payment.getOrderId());
        requestBody.add("PCD_PAY_DATE", paidAt);
        requestBody.add("PCD_REFUND_TOTAL", String.valueOf(payment.getPay()));
        return requestBody;
    }

    private Optional<CertificationResponse> getCertificationResponse() {
        return Optional.ofNullable(webClient.post()
                .uri(CERTIFICATION_URL)
                .headers(h -> {
                    h.setContentType(MediaType.APPLICATION_JSON);
                    h.set("referer", REDIRECT_URL);
                })
                .bodyValue(getCertificationRequestBody())
                .retrieve()
                .bodyToMono(CertificationResponse.class)
                .block());
    }

    private MultiValueMap<String, String> getCertificationRequestBody() {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("cst_id", CST_ID);
        requestBody.add("custKey", CUST_KEY);
        requestBody.add("PCD_PAYCANCEL_FLAG", FLAG);
        return requestBody;
    }
}
//todo : 환불 로직 수정 필요