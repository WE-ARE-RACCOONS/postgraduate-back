package com.postgraduate.domain.image.application.usecase;

import com.postgraduate.domain.image.application.dto.res.ImageUrlResponse;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.global.config.s3.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageUploadUseCase {
    @Value("${profile.default}")
    private String defaultProfile;

    private final S3UploadService uploadService;

    public ImageUrlResponse uploadCertification(MultipartFile certification) {
        String url = uploadService.saveCertificationFile(certification);
        return new ImageUrlResponse(url);
    }

    public ImageUrlResponse uploadProfile(User user, MultipartFile profile) {
        if (!(user.getProfile().equals(defaultProfile)))
            uploadService.deleteProfileImage(user.getProfile());
        String url = uploadService.saveProfileFile(profile);
        return new ImageUrlResponse(url);
    }
}
