package com.postgraduate.domain.image.application.usecase;

import com.postgraduate.domain.auth.util.ProfileUtils;
import com.postgraduate.domain.image.application.dto.res.ImageUrlResponse;
import com.postgraduate.domain.user.user.domain.entity.User;
import com.postgraduate.global.config.s3.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageUploadUseCase {
    private final ProfileUtils profileUtils;
    private final S3UploadService uploadService;

    public ImageUrlResponse uploadCertification(MultipartFile certification) {
        String url = uploadService.saveCertificationFile(certification);
        return new ImageUrlResponse(url);
    }

    public ImageUrlResponse uploadProfile(User user, MultipartFile profile) {
        List<String> profiles = profileUtils.allProfiles();
        if (!(user.isDefaultProfile(profiles)))
            uploadService.deleteProfileImage(user.getProfile());
        String url = uploadService.saveProfileFile(profile);
        return new ImageUrlResponse(url);
    }
}
