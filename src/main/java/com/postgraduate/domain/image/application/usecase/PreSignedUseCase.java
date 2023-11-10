package com.postgraduate.domain.image.application.usecase;

import com.postgraduate.domain.image.application.dto.PreSignedUrlRequest;
import com.postgraduate.domain.image.application.dto.PreSignedUrlResponse;
import com.postgraduate.domain.image.exception.EmptyFileException;
import com.postgraduate.global.config.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PreSignedUseCase {
    private final S3Service s3Service;

    public PreSignedUrlResponse getProfileUrl(PreSignedUrlRequest preSignedUrlRequest) {
        if (preSignedUrlRequest.getFileName().isEmpty())
            throw new EmptyFileException();
        String preSignedUrl = s3Service.getProfilePreSignedUrl(preSignedUrlRequest.getFileName());
        return new PreSignedUrlResponse(preSignedUrl);
    }

    public PreSignedUrlResponse getCertificationUrl(PreSignedUrlRequest preSignedUrlRequest) {
        if (preSignedUrlRequest.getFileName().isEmpty())
            throw new EmptyFileException();
        String preSignedUrl = s3Service.getCertificationPreSignedUrl(preSignedUrlRequest.getFileName());
        return new PreSignedUrlResponse(preSignedUrl);
    }
}
