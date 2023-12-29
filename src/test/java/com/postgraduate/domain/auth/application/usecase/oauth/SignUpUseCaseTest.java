package com.postgraduate.domain.auth.application.usecase.oauth;

import com.postgraduate.domain.auth.application.dto.req.SeniorChangeRequest;
import com.postgraduate.domain.auth.application.dto.req.SeniorSignUpRequest;
import com.postgraduate.domain.auth.application.dto.req.SignUpRequest;
import com.postgraduate.domain.senior.application.utils.SeniorUtils;
import com.postgraduate.domain.senior.domain.entity.Info;
import com.postgraduate.domain.senior.domain.entity.Profile;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorSaveService;
import com.postgraduate.domain.senior.exception.KeywordException;
import com.postgraduate.domain.user.application.utils.UserUtils;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.service.UserGetService;
import com.postgraduate.domain.user.domain.service.UserSaveService;
import com.postgraduate.domain.user.domain.service.UserUpdateService;
import com.postgraduate.domain.user.exception.PhoneNumberException;
import com.postgraduate.domain.wish.domain.entity.Wish;
import com.postgraduate.domain.wish.domain.entity.constant.Status;
import com.postgraduate.domain.wish.domain.service.WishSaveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static com.postgraduate.domain.senior.domain.entity.constant.Status.APPROVE;
import static com.postgraduate.domain.user.domain.entity.constant.Role.SENIOR;
import static com.postgraduate.domain.user.domain.entity.constant.Role.USER;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SignUpUseCaseTest {
    @Mock
    private UserSaveService userSaveService;
    @Mock
    private UserUpdateService userUpdateService;
    @Mock
    private UserGetService userGetService;
    @Mock
    private WishSaveService wishSaveService;
    @Mock
    private SeniorSaveService seniorSaveService;
    @Mock
    private UserUtils userUtils;
    @Mock
    private SeniorUtils seniorUtils;
    @InjectMocks
    private SignUpUseCase signUpUseCase;

    private User user;
    private Wish wish;
    private Senior senior;
    private Info info;
    private Profile profile;

    @BeforeEach
    void setting() {
        info = new Info("a", "a", "a", "a", "a", "a", TRUE, TRUE, "a");
        profile = new Profile("a", "a", "a", "a", 40);
        user = new User(1L, 1234L, "a",
                "a", "123", "a",
                1, USER, TRUE, LocalDateTime.now(), LocalDateTime.now(), FALSE);
        wish = new Wish(1L, "major", "field", TRUE, user, Status.WAITING);
        senior = new Senior(1L, user, "a",
                APPROVE, 1, info, profile,
                LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    @DisplayName("USER 회원가입 테스트")
    void userSignUp() {
        SignUpRequest request = new SignUpRequest(user.getSocialId(), user.getPhoneNumber(), user.getNickName(), user.getMarketingReceive(),
                wish.getMajor(), wish.getField(), wish.getMatchingReceive());

        User saveUser = signUpUseCase.userSignUp(request);

        assertThat(saveUser.getRole())
                .isEqualTo(USER);
        verify(wishSaveService, times(1))
                .saveWish(any(Wish.class));
        verify(userSaveService, times(1))
                .saveUser(any(User.class));
    }

    @Test
    @DisplayName("핸드폰 번호 예외 테스트")
    void invalidPhoneNumber() {
        SignUpRequest request = new SignUpRequest(user.getSocialId(), user.getPhoneNumber(), user.getNickName(), user.getMarketingReceive(),
                wish.getMajor(), wish.getField(), wish.getMatchingReceive());

        doThrow(PhoneNumberException.class)
                .when(userUtils)
                .checkPhoneNumber(request.phoneNumber());
        assertThatThrownBy(() -> signUpUseCase.userSignUp(request))
                .isInstanceOf(PhoneNumberException.class);
    }

    @Test
    @DisplayName("SENIOR 회원가입 테스트")
    void seniorSignUp() {
        SeniorSignUpRequest request = new SeniorSignUpRequest(user.getSocialId(), user.getPhoneNumber(), user.getNickName(), user.getMarketingReceive(),
                info.getMajor(), info.getPostgradu(), info.getProfessor(), info.getLab(), info.getField(),
                info.getKeyword(), senior.getCertification());

        User saveUser = signUpUseCase.seniorSignUp(request);

        assertThat(saveUser.getRole())
                .isEqualTo(SENIOR);
        verify(userSaveService, times(1))
                .saveUser(any(User.class));
        verify(seniorSaveService, times(1))
                .saveSenior(any(Senior.class));
    }

    @Test
    @DisplayName("키워드 예외 테스트")
    void invalidKeyword() {
        SeniorSignUpRequest request = new SeniorSignUpRequest(user.getSocialId(), user.getPhoneNumber(), user.getNickName(), user.getMarketingReceive(),
                info.getMajor(), info.getPostgradu(), info.getProfessor(), info.getLab(), info.getField(),
                info.getKeyword(), senior.getCertification());

        doThrow(KeywordException.class)
                .when(seniorUtils)
                .checkKeyword(request.keyword());

        assertThatThrownBy(() -> signUpUseCase.seniorSignUp(request))
                .isInstanceOf(KeywordException.class);
    }

    @Test
    @DisplayName("선배 변경 테스트")
    void changeSenior() {
        SeniorChangeRequest seniorChangeRequest = new SeniorChangeRequest(info.getMajor(), info.getPostgradu(), info.getProfessor(),
                info.getLab(), info.getField(), info.getKeyword(),
                senior.getCertification());

        given(userGetService.getUser(user.getUserId()))
                .willReturn(user);

        signUpUseCase.changeSenior(user, seniorChangeRequest);

        verify(seniorSaveService, times(1))
                .saveSenior(any(Senior.class));
        verify(userUpdateService, times(1))
                .updateRole(any(), eq(SENIOR));

    }
}