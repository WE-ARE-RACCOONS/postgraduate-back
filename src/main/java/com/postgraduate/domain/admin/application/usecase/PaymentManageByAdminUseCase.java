package com.postgraduate.domain.admin.application.usecase;

import com.postgraduate.domain.admin.application.dto.PaymentInfo;
import com.postgraduate.domain.admin.application.dto.res.CertificationResponse;
import com.postgraduate.domain.admin.application.dto.res.PaymentManageResponse;
import com.postgraduate.domain.admin.application.dto.res.RefundResponse;
import com.postgraduate.domain.admin.application.mapper.AdminMapper;
import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.payment.domain.service.PaymentGetService;
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

import static com.postgraduate.domain.payment.domain.entity.constant.Status.CANCEL;
import static org.springframework.http.CacheControl.noCache;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentManageByAdminUseCase {
    private final PaymentGetService paymentGetService;
    private final PaymentUpdateService paymentUpdateService;
    private final WebClient webClient;

    private String CST_ID = "test"; //todo : 나중에 @Value사용하도록 변경
    private String CUST_KEY = "abcd1234567890";//todo : 나중에 @Value사용하도록 변경
    private String REDIRECT_URL = "http://localhost:8080"; //todo : 마찬가지
    private String REFUND_KEY = "a41ce010ede9fcbfb3be86b24858806596a9db68b79d138b147c3e563e1829a0";
    private final static String FLAG = "Y";
    private final static String REFUND_URL = "https://democpay.payple.kr";
    private final static String CERTIFICATION_URL = "https://democpay.payple.kr/php/auth.php";

    public PaymentManageResponse getPayments(Integer page, String search) {
        Page<Payment> payments = paymentGetService.all(page, search);
        List<PaymentInfo> paymentInfos = payments.stream()
                .map(AdminMapper::mapToPaymentInfo)
                .toList();
        long totalElements = payments.getTotalElements();
        int totalPages = payments.getTotalPages();
        return new PaymentManageResponse(paymentInfos, totalElements, totalPages);
    }

    public void refundPayment(Long mentoringId) {
        Payment payment = paymentGetService.byMentoringId(mentoringId);
        CertificationResponse certificationResponse = getCertificationResponse();
        getRefundResponse(certificationResponse, payment);
        paymentUpdateService.updateStatus(payment, CANCEL);
    }

    private RefundResponse getRefundResponse(CertificationResponse response, Payment payment) {
        return webClient.post()
                .uri(response.PCD_PAY_URL())
                .headers(h -> h.setContentType(MediaType.APPLICATION_JSON))
                .headers(h -> h.setCacheControl(noCache()))
                .headers(h -> h.set("referer", REDIRECT_URL))
                .bodyValue(getRefundRequestBody(response, payment))
                .retrieve()
                .bodyToMono(RefundResponse.class)
                .block();
    }

    private MultiValueMap<String, String> getRefundRequestBody(CertificationResponse response, Payment payment) {
        String paidAt = payment.getPaidAt()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));
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

    private CertificationResponse getCertificationResponse() {
        return webClient.post()
                .uri(CERTIFICATION_URL)
                .headers(h -> h.setContentType(MediaType.APPLICATION_JSON))
                .headers(h -> h.set("referer", REDIRECT_URL))
                .bodyValue(getCertificationRequestBody())
                .retrieve()
                .bodyToMono(CertificationResponse.class)
                .block();
    }

    private MultiValueMap<String, String> getCertificationRequestBody() {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("cst_id", CST_ID);
        requestBody.add("custKey", CUST_KEY);
        requestBody.add("PCD_PAYCANCEL_FLAG", FLAG);
        return requestBody;
    }
}
