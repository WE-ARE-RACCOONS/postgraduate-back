package com.postgraduate.global.image.presentation;

import com.postgraduate.global.image.application.dto.req.PreSignedUrlRequest;
import com.postgraduate.global.image.application.dto.res.PreSignedUrlResponse;
import com.postgraduate.global.image.application.dto.res.ImageUrlResponse;
import com.postgraduate.global.image.application.usecase.ImageUploadUseCase;
import com.postgraduate.global.image.application.usecase.PreSignedUseCase;
import com.postgraduate.domain.member.user.domain.entity.User;
import com.postgraduate.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.postgraduate.global.image.presentation.constant.ImageResponseCode.IMAGE_CREATE;
import static com.postgraduate.global.image.presentation.constant.ImageResponseMessage.ISSUE_URL;
import static com.postgraduate.global.image.presentation.constant.ImageResponseMessage.UPLOAD_URL;
import static com.postgraduate.global.dto.ResponseDto.create;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
@Tag(name = "IMAGE Controller")
public class ImageController {
    private final PreSignedUseCase preSignedUseCase;
    private final ImageUploadUseCase imageUploadUseCase;

    @PostMapping("/url/profile")
    @Operation(description = "USER Profile 등록 URL API - 이미지 풀네임으로 주세요 xxx.확장자, 이미지 이름을 유니크하게 만들어주세요 UUID+파일명 등등")
    public ResponseEntity<ResponseDto<PreSignedUrlResponse>> getProfilePreSignedUrl(@RequestBody PreSignedUrlRequest preSignedUrlRequest) {
        PreSignedUrlResponse profileUrl = preSignedUseCase.getProfileUrl(preSignedUrlRequest);
        return ResponseEntity.ok(create(IMAGE_CREATE.getCode(), ISSUE_URL.getMessage(), profileUrl));
    }

    @PostMapping("/url/certification")
    @Operation(description = "Senior 학생증 인증 등록 URL API - 이미지 풀네임으로 주세요 xxx.확장자, 이미지 이름을 유니크하게 만들어주세요 UUID+파일명 등등")
    public ResponseEntity<ResponseDto<PreSignedUrlResponse>> getCertificationPreSignedUrl(@RequestBody PreSignedUrlRequest preSignedUrlRequest) {
        PreSignedUrlResponse certificationUrl = preSignedUseCase.getCertificationUrl(preSignedUrlRequest);
        return ResponseEntity.ok(create(IMAGE_CREATE.getCode(), ISSUE_URL.getMessage(), certificationUrl));
    }

    @PostMapping("/upload/profile")
    @Operation(summary = "USER Profile 업로드 후 업로드 URL return / 토큰 필요", description = "profileFile 사진 Multipart File로 보내주세요")
    public ResponseEntity<ResponseDto<ImageUrlResponse>> getProfileUrl(@AuthenticationPrincipal User user, @RequestPart MultipartFile profileFile) {
        ImageUrlResponse imageUrlResponse = imageUploadUseCase.uploadProfile(user, profileFile);
        return ResponseEntity.ok(create(IMAGE_CREATE.getCode(), UPLOAD_URL.getMessage(), imageUrlResponse));
    }

    @PostMapping("/upload/certification")
    @Operation(summary = "SENIOR Certification 업로드 후 업로드 URL return", description = "certificationFile 사진 Multipart File로 보내주세요")
    public ResponseEntity<ResponseDto<ImageUrlResponse>> getCertificationUrl(@RequestPart MultipartFile certificationFile) {
        ImageUrlResponse imageUrlResponse = imageUploadUseCase.uploadCertification(certificationFile);
        return ResponseEntity.ok(create(IMAGE_CREATE.getCode(), UPLOAD_URL.getMessage(), imageUrlResponse));
    }
}
