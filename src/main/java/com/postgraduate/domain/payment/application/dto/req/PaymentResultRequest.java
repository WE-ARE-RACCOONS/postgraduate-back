package com.postgraduate.domain.payment.application.dto.req;

public record PaymentResultRequest(
        String PCD_PAY_RST, //요청 결과 "success, error, close"
        String PCD_PAY_CODE, //응답 코드
        String PCD_PAY_MSG, //응답 메시지
        String PCD_PAY_TYPE, //응답 메시지
        String PCD_AUTH_KEY, //결제 후 리탄 토큰키
        String PCD_PAY_HOST,
        String PCD_PAYER_NO,
        String PCD_PAYER_NAME,
        String PCD_PAYER_EMAIL,
        String PCD_PAY_OID, //주문번호
        String PCD_PAY_GOODS, //상품명
        String PCD_PAY_AMOUNT,
        String PCD_PAY_AMOUNT_REAL,
        String PCD_PAY_TOTAL, //결제 금액
        String PCD_PAY_CARDTRADENUM, //거래키
        String PCD_PAY_CARDAUTHNO, //카드 승인번호
        String PCD_PAY_CARDRECEIPT, //매출 전표 출력 링크
        String PCD_PAY_TIME //결제 시점
) {
}