package com.postgraduate.domain.image.presentation;

import com.postgraduate.domain.image.application.dto.req.PreSignedUrlRequest;
import com.postgraduate.domain.image.application.dto.res.PreSignedUrlResponse;
import com.postgraduate.domain.image.application.dto.res.ImageUrlResponse;
import com.postgraduate.domain.image.application.usecase.ImageUploadUseCase;
import com.postgraduate.domain.image.application.usecase.PreSignedUseCase;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.postgraduate.domain.image.presentation.constant.ImageResponseCode.IMAGE_CREATE;
import static com.postgraduate.domain.image.presentation.constant.ImageResponseMessage.ISSUE_URL;
import static com.postgraduate.domain.image.presentation.constant.ImageResponseMessage.UPLOAD_URL;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
@Tag(name = "IMAGE Controller")
public class ImageController {
    private final PreSignedUseCase preSignedUseCase;
    private final ImageUploadUseCase imageUploadUseCase;

    @PostMapping("/url/profile")
    @Operation(description = "USER Profile 등록 URL API - 이미지 풀네임으로 주세요 xxx.확장자, 이미지 이름을 유니크하게 만들어주세요 UUID+파일명 등등")
    public ResponseDto<PreSignedUrlResponse> getProfilePreSignedUrl(@RequestBody PreSignedUrlRequest preSignedUrlRequest) {
        PreSignedUrlResponse profileUrl = preSignedUseCase.getProfileUrl(preSignedUrlRequest);
        return ResponseDto.create(IMAGE_CREATE.getCode(), ISSUE_URL.getMessage(), profileUrl);
    }

    @PostMapping("/url/certification")
    @Operation(description = "Senior 학생증 인증 등록 URL API - 이미지 풀네임으로 주세요 xxx.확장자, 이미지 이름을 유니크하게 만들어주세요 UUID+파일명 등등")
    public ResponseDto<PreSignedUrlResponse> getCertificationPreSignedUrl(@RequestBody PreSignedUrlRequest preSignedUrlRequest) {
        PreSignedUrlResponse certificationUrl = preSignedUseCase.getCertificationUrl(preSignedUrlRequest);
        return ResponseDto.create(IMAGE_CREATE.getCode(), ISSUE_URL.getMessage(), certificationUrl);
    }

    @PostMapping("/upload/profile")
    @Operation(summary = "USER Profile 업로드 후 업로드 URL return", description = "profileFile 사진 Multipart File로 보내주세요")
    public ResponseDto<ImageUrlResponse> getProfileUrl(@AuthenticationPrincipal User user, @RequestPart MultipartFile profileFile) {
        ImageUrlResponse imageUrlResponse = imageUploadUseCase.uploadProfile(user, profileFile);
        return ResponseDto.create(IMAGE_CREATE.getCode(), UPLOAD_URL.getMessage(), imageUrlResponse);
    }

    @PostMapping("/upload/certification")
    @Operation(summary = "SENIOR Certification 업로드 후 업로드 URL return", description = "certificationFile 사진 Multipart File로 보내주세요")
    public ResponseDto<ImageUrlResponse> getCertificationUrl(@AuthenticationPrincipal User user, @RequestPart MultipartFile certificationFile) {
        ImageUrlResponse imageUrlResponse = imageUploadUseCase.uploadCertification(user, certificationFile);
        return ResponseDto.create(IMAGE_CREATE.getCode(), UPLOAD_URL.getMessage(), imageUrlResponse);
    }
}
