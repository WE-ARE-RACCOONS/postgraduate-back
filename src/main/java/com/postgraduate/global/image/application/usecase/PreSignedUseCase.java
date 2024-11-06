package com.postgraduate.global.image.application.usecase;

import com.postgraduate.global.image.application.dto.req.PreSignedUrlRequest;
import com.postgraduate.global.image.application.dto.res.PreSignedUrlResponse;
import com.postgraduate.global.image.exception.EmptyFileException;
import com.postgraduate.global.config.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PreSignedUseCase {
    private final S3Service s3Service;

    public PreSignedUrlResponse getProfileUrl(PreSignedUrlRequest preSignedUrlRequest) {
        if (preSignedUrlRequest.fileName().isEmpty())
            throw new EmptyFileException();
        String preSignedUrl = s3Service.getProfilePreSignedUrl(preSignedUrlRequest.fileName());
        return new PreSignedUrlResponse(preSignedUrl);
    }

    public PreSignedUrlResponse getCertificationUrl(PreSignedUrlRequest preSignedUrlRequest) {
        if (preSignedUrlRequest.fileName().isEmpty())
            throw new EmptyFileException();
        String preSignedUrl = s3Service.getCertificationPreSignedUrl(preSignedUrlRequest.fileName());
        return new PreSignedUrlResponse(preSignedUrl);
    }
}
