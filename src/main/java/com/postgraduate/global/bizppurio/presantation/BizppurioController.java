package com.postgraduate.global.bizppurio.presantation;

import com.postgraduate.global.bizppurio.application.dto.req.JuniorMatchingFailRequest;
import com.postgraduate.global.bizppurio.application.dto.req.JuniorMatchingSuccessRequest;
import com.postgraduate.global.bizppurio.application.dto.req.JuniorMatchingWaitingRequest;
import com.postgraduate.global.bizppurio.application.usecase.BizppurioJuniorMessage;
import com.postgraduate.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bizppurio")
@Tag(name = "BIZPPURIO Controller", description = "알림톡 수동 전송용 API (관리자 권한 필요)")
public class BizppurioController {
    private final BizppurioJuniorMessage bizppurioJuniorMessage;

    @PostMapping("/matching/waiting")
    public ResponseDto<Void> matchingWaiting(@RequestBody JuniorMatchingWaitingRequest request) {
        bizppurioJuniorMessage.matchingWaiting(request);
        return ResponseDto.create("200", request.phoneNumber() + " 번호로 " + request.name() + " 님께 알림톡 전송 완료");
    }

    @PostMapping("/matching/fail")
    public ResponseDto<Void> matchingFail(@RequestBody JuniorMatchingFailRequest request) {
        bizppurioJuniorMessage.matchingFail(request);
        return ResponseDto.create("200", request.phoneNumber() + " 번호로 " + request.name() + " 님께 알림톡 전송 완료");
    }

    @PostMapping("/matching/success")
    public ResponseDto<Void> matchingSuccess(@RequestBody JuniorMatchingSuccessRequest request) {
        bizppurioJuniorMessage.matchingSuccess(request);
        return ResponseDto.create("200", request.phoneNumber() + " 번호로 " + request.name() + " 님께 알림톡 전송 완료");
    }
}
