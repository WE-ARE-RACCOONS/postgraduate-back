package com.postgraduate.domain.senior.application.usecase;

import com.postgraduate.domain.available.application.dto.res.AvailableTimesResponse;
import com.postgraduate.domain.available.domain.entity.Available;
import com.postgraduate.domain.available.domain.service.AvailableGetService;
import com.postgraduate.domain.senior.application.dto.res.AllSeniorSearchResponse;
import com.postgraduate.domain.senior.application.dto.res.SeniorDetailResponse;
import com.postgraduate.domain.senior.application.dto.res.SeniorProfileResponse;
import com.postgraduate.domain.senior.domain.entity.Info;
import com.postgraduate.domain.senior.domain.entity.Profile;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.senior.domain.service.SeniorUpdateService;
import com.postgraduate.domain.user.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.List;

import static com.postgraduate.domain.senior.domain.entity.constant.Status.APPROVE;
import static com.postgraduate.domain.user.domain.entity.constant.Role.USER;
import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SeniorInfoUseTypeTest {
    @Mock
    private SeniorGetService seniorGetService;
    @Mock
    private SeniorUpdateService seniorUpdateService;
    @Mock
    private AvailableGetService availableGetService;
    @InjectMocks
    private SeniorInfoUseCase seniorInfoUseCase;

    private User user;
    private User originUser;
    private Senior senior;
    private Info info;
    private Profile profile;

    @BeforeEach
    void setting() {
        info = new Info("a", "a", "a", "a", "a", "a", TRUE, TRUE, "a");
        profile = new Profile("a", "a", "a", "a", 40);
        user = new User(1L, 1234L, "a",
                "a", "123", "a",
                1, USER, TRUE, LocalDateTime.now(), LocalDateTime.now(), TRUE);
        originUser = new User(2L, 12345L, "a",
                "a", "12345", "a",
                1, USER, TRUE, LocalDateTime.now(), LocalDateTime.now(), TRUE);
        senior = new Senior(1L, user, "a",
                APPROVE, 1, info, profile,
                LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    @DisplayName("선배 상세보기 테스트 USER")
    void getSeniorDetail() {
        Available available1 = new Available(1L, "월", "12:00", "18:00", senior);
        Available available2 = new Available(2L, "화", "12:00", "18:00", senior);
        Available available3 = new Available(3L, "수", "12:00", "18:00", senior);
        List<Available> availables = List.of(available1, available2, available3);

        given(seniorGetService.bySeniorId(any()))
                .willReturn(senior);
        given(availableGetService.bySenior(senior))
                .willReturn(availables);

        SeniorDetailResponse seniorDetail = seniorInfoUseCase.getSeniorDetail(user, senior.getSeniorId());

        verify(seniorUpdateService, times(1))
                .updateHit(senior);
        assertThat(seniorDetail.times())
                .hasSameSizeAs(availables);
        assertThat(seniorDetail).isNotNull();
    }

    @Test
    @DisplayName("검색어 기본 페이지 조회")
    void getSearchSeniorWithNull() {
        Senior otherSenior = new Senior(-2L, user, "a",
                APPROVE, 1, info, profile,
                LocalDateTime.now(), LocalDateTime.now());
        List<Senior> seniors = List.of(senior, otherSenior);
        Page<Senior> seniorPage = new PageImpl<>(seniors);

        given(seniorGetService.bySearch("a", null, "low"))
                .willReturn(seniorPage);

        AllSeniorSearchResponse searchSenior = seniorInfoUseCase.getSearchSenior("a", null, "low");

        assertThat(searchSenior.seniorSearchResponses())
                .hasSameSizeAs(seniors);
    }

    @Test
    @DisplayName("검색어 페이지 조회")
    void getSearchSeniorWithPage() {
        Senior senior1 = new Senior(1L, user, "a",
                APPROVE, 1, info, profile,
                LocalDateTime.now(), LocalDateTime.now());
        List<Senior> seniors = List.of(senior, senior1);
        Page<Senior> seniorPage = new PageImpl<>(seniors);

        given(seniorGetService.byField("a", info.getPostgradu(), null))
                .willReturn(seniorPage);

        AllSeniorSearchResponse searchSenior = seniorInfoUseCase.getFieldSenior("a", info.getPostgradu(), null);

        assertThat(searchSenior.seniorSearchResponses())
                .hasSameSizeAs(seniors);
    }

    @Test
    @DisplayName("필터 기본 페이지 조회")
    void getFieldSeniorWithNull() {
        Senior senior1 = new Senior(1L, user, "a",
                APPROVE, 1, info, profile,
                LocalDateTime.now(), LocalDateTime.now());
        List<Senior> seniors = List.of(senior, senior1);
        Page<Senior> seniorPage = new PageImpl<>(seniors);

        given(seniorGetService.byField("a", info.getPostgradu(), null))
                .willReturn(seniorPage);

        AllSeniorSearchResponse fieldSenior = seniorInfoUseCase.getFieldSenior("a", info.getPostgradu(), null);

        assertThat(fieldSenior.seniorSearchResponses())
                .hasSameSizeAs(seniors);
    }

    @Test
    @DisplayName("필터 페이지 조회")
    void getFieldSeniorWithPage() {
        Senior senior1 = new Senior(1L, user, "a",
                APPROVE, 1, info, profile,
                LocalDateTime.now(), LocalDateTime.now());
        List<Senior> seniors = List.of(senior, senior1);
        Page<Senior> seniorPage = new PageImpl<>(seniors);

        given(seniorGetService.byField("a", "서울대학교", 1))
                .willReturn(seniorPage);

        AllSeniorSearchResponse fieldSenior = seniorInfoUseCase.getFieldSenior("a", "서울대학교", 1);

        assertThat(fieldSenior.seniorSearchResponses())
                .hasSameSizeAs(seniors);
    }

    @Test
    @DisplayName("선배 프로필 조회")
    void getSeniorProfile() {
        given(seniorGetService.bySeniorId(senior.getSeniorId()))
                .willReturn(senior);

        SeniorProfileResponse seniorProfile = seniorInfoUseCase.getSeniorProfile(originUser, senior.getSeniorId());

        assertThat(seniorProfile.profile())
                .isEqualTo(user.getProfile());
        assertThat(seniorProfile.lab())
                .isEqualTo(info.getLab());
        assertThat(seniorProfile.postgradu())
                .isEqualTo(info.getPostgradu());
        assertThat(seniorProfile.major())
                .isEqualTo(info.getMajor());
        assertThat(seniorProfile.nickName())
                .isEqualTo(user.getNickName());
        assertThat(seniorProfile.userId())
                .isEqualTo(originUser.getUserId());
        assertThat(seniorProfile.phoneNumber())
                .isEqualTo(originUser.getPhoneNumber());
    }

    @Test
    @DisplayName("선배 가능 시간 조회")
    void getSeniorTimes() {
        Available available1 = new Available(1L, "월", "12:00", "18:00", senior);
        Available available2 = new Available(2L, "화", "12:00", "18:00", senior);
        Available available3 = new Available(3L, "수", "12:00", "18:00", senior);
        List<Available> availables = List.of(available1, available2, available3);

        given(seniorGetService.bySeniorId(senior.getSeniorId()))
                .willReturn(senior);
        given(availableGetService.bySenior(senior))
                .willReturn(availables);

        AvailableTimesResponse seniorTimes = seniorInfoUseCase.getSeniorTimes(senior.getSeniorId());

        assertThat(seniorTimes.times())
                .hasSameSizeAs(availables);
    }
}