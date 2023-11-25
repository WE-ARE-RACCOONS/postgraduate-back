package com.postgraduate.domain.image.application.usecase;

import com.postgraduate.domain.image.application.dto.res.ImageUrlResponse;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.global.config.s3.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageUploadUseCase {
    private final S3UploadService uploadService;
    private final SeniorGetService seniorGetService;

    public ImageUrlResponse uploadCertification(User user, MultipartFile certification) {
        Senior senior = seniorGetService.byUser(user);
        if (senior.getCertification() != null)
            uploadService.deleteCertificationImage(senior.getCertification());
        String url = uploadService.saveCertificationFile(certification);
        return new ImageUrlResponse(url);
    }

    public ImageUrlResponse uploadProfile(User user, MultipartFile profile) {
        if (user.getProfile() != null)
            uploadService.deleteProfileImage(user.getProfile());
        String url = uploadService.saveProfileFile(profile);
        return new ImageUrlResponse(url);
    }
}
