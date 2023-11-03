package com.postgraduate.domain.image.presentation;

import com.postgraduate.domain.image.application.dto.PreSignedUrlRequest;
import com.postgraduate.domain.image.application.dto.PreSignedUrlResponse;
import com.postgraduate.domain.image.application.usecase.PreSignedUseCase;
import com.postgraduate.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.postgraduate.domain.image.presentation.constant.ImageResponseCode.IMAGE_CREATE;
import static com.postgraduate.domain.image.presentation.constant.ImageResponseMessage.ISSUE_URL;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
@Tag(name = "IMAGE Controller")
public class ImageController {
    private final PreSignedUseCase preSignedUseCase;

    @PostMapping("/url/profile")
    @Operation(description = "USER Profile 등록 URL API - 이미지 풀네임으로 주세요 xxx.확장자, 이미지 이름을 유니크하게 만들어주세요 UUID+파일명 등등")
    public ResponseDto<PreSignedUrlResponse> getProfileUrl(@RequestBody PreSignedUrlRequest preSignedUrlRequest) {
        PreSignedUrlResponse profileUrl = preSignedUseCase.getProfileUrl(preSignedUrlRequest);
        return ResponseDto.create(IMAGE_CREATE.getCode(), ISSUE_URL.getMessage(), profileUrl);
    }

    @PostMapping("/url/certification")
    @Operation(description = "Senior 학생증 인증 등록 URL API - 이미지 풀네임으로 주세요 xxx.확장자, 이미지 이름을 유니크하게 만들어주세요 UUID+파일명 등등")
    public ResponseDto<PreSignedUrlResponse> getCertificationUrl(@RequestBody PreSignedUrlRequest preSignedUrlRequest) {
        PreSignedUrlResponse certificationUrl = preSignedUseCase.getCertificationUrl(preSignedUrlRequest);
        return ResponseDto.create(IMAGE_CREATE.getCode(), ISSUE_URL.getMessage(), certificationUrl);
    }
}
