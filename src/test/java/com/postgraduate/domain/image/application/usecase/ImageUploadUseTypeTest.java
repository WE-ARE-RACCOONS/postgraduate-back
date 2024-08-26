package com.postgraduate.domain.image.application.usecase;

import com.postgraduate.global.auth.login.util.ProfileUtils;
import com.postgraduate.global.image.application.dto.res.ImageUrlResponse;
import com.postgraduate.domain.user.user.domain.entity.User;
import com.postgraduate.global.config.s3.S3UploadService;
import com.postgraduate.global.image.application.usecase.ImageUploadUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDateTime;

import static com.postgraduate.domain.user.user.domain.entity.constant.Role.USER;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ImageUploadUseTypeTest {
    @Mock
    private S3UploadService s3UploadService;
    @Mock
    private ProfileUtils profileUtils;

    @InjectMocks
    private ImageUploadUseCase imageUploadUseCase;

    @Test
    @DisplayName("Certification 업로드 테스트")
    void uploadCertification() {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("certification", new byte[]{});
        given(s3UploadService.saveCertificationFile(mockMultipartFile))
                .willReturn("url");

        Assertions.assertThat(imageUploadUseCase.uploadCertification(mockMultipartFile).profileUrl())
                .isEqualTo("url");
    }

    @Test
    @DisplayName("Profile 업로드 테스트")
    void uploadProfile() {
        User user = new User(-1L, -1234L, "abc.com", "abc"
                , " 123123", "abcab", 0
                , USER, TRUE, LocalDateTime.now(), LocalDateTime.now(), FALSE, TRUE);

        MockMultipartFile mockMultipartFile = new MockMultipartFile("profile", new byte[]{});
        given(s3UploadService.saveProfileFile(mockMultipartFile))
                .willReturn("url");

        ImageUrlResponse imageUrlResponse = imageUploadUseCase.uploadProfile(user, mockMultipartFile);

        verify(s3UploadService).deleteProfileImage(user.getProfile());
        Assertions.assertThat(imageUrlResponse.profileUrl())
                .isEqualTo("url");
    }
}