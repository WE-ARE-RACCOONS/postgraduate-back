package com.postgraduate.domain.senior.domain.service;

import com.postgraduate.domain.member.senior.application.dto.req.AvailableCreateRequest;
import com.postgraduate.domain.member.senior.application.dto.req.SeniorMyPageProfileRequest;
import com.postgraduate.domain.member.senior.domain.entity.Info;
import com.postgraduate.domain.member.senior.domain.entity.Profile;
import com.postgraduate.domain.member.senior.domain.entity.Senior;
import com.postgraduate.domain.member.senior.domain.entity.constant.Status;
import com.postgraduate.domain.member.senior.domain.service.SeniorUpdateService;
import com.postgraduate.domain.member.user.domain.entity.User;
import com.postgraduate.domain.member.user.domain.entity.Wish;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.postgraduate.domain.member.senior.application.mapper.SeniorMapper.mapToInfo;
import static com.postgraduate.domain.member.senior.application.mapper.SeniorMapper.mapToProfile;
import static com.postgraduate.domain.member.user.domain.entity.constant.Role.SENIOR;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.time.LocalDateTime.now;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({MockitoExtension.class})
class SeniorUpdateServiceTest {
    @InjectMocks
    private SeniorUpdateService seniorUpdateService;

    private User user = new User(1L, 2L, "a", "b", "c", "d", 0, SENIOR, FALSE, now(), now(), TRUE, TRUE, new Wish());
    private Senior senior;
    @BeforeEach
    void setting() {
        senior = new Senior(1L, user, "a", Status.WAITING, 1, 100, new Info(), new Profile(), now(), now(), null, null);
    }

    @Test
    @DisplayName("profile 업데이트")
    void seniorProfile() {
        Profile profile = new Profile("a", "b", "c");
        seniorUpdateService.signUpSeniorProfile(senior, profile);
        Profile changeProfile = senior.getProfile();

        assertThat(changeProfile.getInfo())
                .isEqualTo(profile.getInfo());
        assertThat(changeProfile.getOneLiner())
                .isEqualTo(profile.getOneLiner());
        assertThat(changeProfile.getTarget())
                .isEqualTo(profile.getTarget());
    }

    @Test
    @DisplayName("인증사진 업데이트")
    void updateCertification() {
        String image = "image";
        seniorUpdateService.updateCertification(senior, image);

        assertThat(senior.getCertification())
                .isEqualTo(image);
    }

    @Test
    @DisplayName("마이페이지 프로필 업데이트")
    void updateMyPageProfile() {
        AvailableCreateRequest availableCreateRequest1 = new AvailableCreateRequest("day", "12:00", "18:00");
        AvailableCreateRequest availableCreateRequest2 = new AvailableCreateRequest("day", "12:00", "18:00");
        AvailableCreateRequest availableCreateRequest3 = new AvailableCreateRequest("day", "12:00", "18:00");
        SeniorMyPageProfileRequest request = new SeniorMyPageProfileRequest(
                "a", "b", "c",
                "d", "e", "f", "g",
                of(availableCreateRequest1, availableCreateRequest2, availableCreateRequest3)
        );
        Profile profile = mapToProfile(request);
        Info info = mapToInfo(senior, request);
        seniorUpdateService.updateMyPageProfile(senior, info, profile);
        Info changeInfo = senior.getInfo();
        Profile changeProfile = senior.getProfile();

        assertThat(changeInfo.getKeyword())
                .isEqualTo(request.keyword());
        assertThat(changeInfo.getLab())
                .isEqualTo(request.lab());
        assertThat(changeInfo.getField())
                .isEqualTo(request.field());
        assertThat(changeInfo.getChatLink())
                .isEqualTo(request.chatLink());

        assertThat(changeProfile.getInfo())
                .isEqualTo(profile.getInfo());
        assertThat(changeProfile.getTarget())
                .isEqualTo(profile.getTarget());
        assertThat(changeProfile.getOneLiner())
                .isEqualTo(profile.getOneLiner());
    }

    @Test
    @DisplayName("조회수 증가")
    void updateHit() {
        int originHit = senior.getHit();
        seniorUpdateService.updateHit(senior);

        assertThat(senior.getHit())
                .isEqualTo(++originHit);
    }
}