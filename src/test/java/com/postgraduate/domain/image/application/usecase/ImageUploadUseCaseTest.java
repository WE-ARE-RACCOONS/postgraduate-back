package com.postgraduate.domain.image.application.usecase;

import com.postgraduate.domain.image.application.dto.res.ImageUrlResponse;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.global.config.s3.Default;
import com.postgraduate.global.config.s3.S3UploadService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;

import static com.postgraduate.domain.user.domain.entity.constant.Role.USER;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ImageUploadUseCaseTest {
    @Mock
    private S3UploadService s3UploadService;

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
                , USER, TRUE, LocalDate.now(), LocalDate.now(), FALSE);

        MockMultipartFile mockMultipartFile = new MockMultipartFile("profile", new byte[]{});
        given(s3UploadService.saveProfileFile(mockMultipartFile))
                .willReturn("url");

        ImageUrlResponse imageUrlResponse = imageUploadUseCase.uploadProfile(user, mockMultipartFile);

        verify(s3UploadService).deleteProfileImage(user.getProfile());
        Assertions.assertThat(imageUrlResponse.profileUrl())
                .isEqualTo("url");
    }

    @Test
    @DisplayName("Profile 업로드 테스트 기본 이미지 사용시")
    void uploadProfileWithDefaultProfile() {
        User user = new User(-11L, -12345L, "abc.com", "qwe"
                , " 123123", Default.USER.getUrl(), 0
                , USER, TRUE, LocalDate.now(), LocalDate.now(), FALSE);

        MockMultipartFile mockMultipartFile = new MockMultipartFile("change", new byte[]{});
        given(s3UploadService.saveProfileFile(mockMultipartFile))
                .willReturn("url");

        ImageUrlResponse imageUrlResponse = imageUploadUseCase.uploadProfile(user, mockMultipartFile);

        verify(s3UploadService, never()).deleteProfileImage(user.getProfile());
        Assertions.assertThat(imageUrlResponse.profileUrl())
                .isEqualTo("url");
    }
}