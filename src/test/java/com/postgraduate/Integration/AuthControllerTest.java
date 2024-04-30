package com.postgraduate.Integration;

import com.postgraduate.domain.auth.application.dto.req.*;
import com.postgraduate.domain.auth.application.dto.res.KakaoUserInfoResponse;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.wish.domain.entity.Wish;
import com.postgraduate.domain.wish.domain.entity.constant.Status;
import com.postgraduate.support.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.http.MediaType;

import java.util.Optional;

import static com.postgraduate.domain.auth.presentation.constant.AuthResponseCode.*;
import static com.postgraduate.domain.auth.presentation.constant.AuthResponseMessage.*;
import static com.postgraduate.domain.senior.presentation.constant.SeniorResponseCode.SENIOR_CREATE;
import static com.postgraduate.domain.senior.presentation.constant.SeniorResponseCode.SENIOR_NOT_FOUND;
import static com.postgraduate.domain.senior.presentation.constant.SeniorResponseMessage.CREATE_SENIOR;
import static com.postgraduate.domain.senior.presentation.constant.SeniorResponseMessage.NOT_FOUND_SENIOR;
import static com.postgraduate.domain.user.domain.entity.constant.Role.SENIOR;
import static com.postgraduate.domain.user.domain.entity.constant.Role.USER;
import static com.postgraduate.domain.user.presentation.constant.UserResponseCode.USER_NOT_FOUND;
import static com.postgraduate.domain.user.presentation.constant.UserResponseMessage.NOT_FOUND_USER;
import static com.postgraduate.global.constant.ErrorCode.VALID_BLANK;
import static java.lang.Boolean.FALSE;
import static java.time.LocalDateTime.now;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends IntegrationTest {
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private User user;
    private final Long anonymousUserSocialId = 2L;

    @BeforeEach
    void setUp() {
        user = new User(0L, 1L, "mail", "후배", "011", "profile", 0, USER, true, now(), now(), false);
        userRepository.save(user);
        doNothing().when(slackLogErrorMessage).sendSlackLog(any());
        doNothing().when(slackSignUpMessage).sendSeniorSignUp(any());
        doNothing().when(slackSignUpMessage).sendJuniorSignUp(any(), any());
    }

    @Test
    @DisplayName("회원이 로그인한다.")
    void authLoginByUser() throws Exception {
        Wish wish = new Wish(0L, "major", "field", true, user, Status.MATCHED);
        wishRepository.save(wish);

        CodeRequest codeRequest = new CodeRequest("code");
        String request = objectMapper.writeValueAsString(codeRequest);

        when(kakaoAccessTokenUseCase.getAccessToken(codeRequest))
                .thenReturn(new KakaoUserInfoResponse(1L, any()));

        mvc.perform(post("/auth/login/KAKAO")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(AUTH_ALREADY.getCode()))
                .andExpect(jsonPath("$.message").value(SUCCESS_AUTH.getMessage()))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.socialId").doesNotExist());
    }

    @Test
    @DisplayName("비회원이 로그인한다.")
    void authLoginByAnonymousUser() throws Exception {
        CodeRequest codeRequest = new CodeRequest("code");
        String request = objectMapper.writeValueAsString(codeRequest);

        when(kakaoAccessTokenUseCase.getAccessToken(codeRequest))
                .thenReturn(new KakaoUserInfoResponse(anonymousUserSocialId, any()));

        mvc.perform(post("/auth/login/KAKAO")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(AUTH_NONE.getCode()))
                .andExpect(jsonPath("$.message").value(NOT_REGISTERED_USER.getMessage()))
                .andExpect(jsonPath("$.data.accessToken").doesNotExist())
                .andExpect(jsonPath("$.data.socialId").value(anonymousUserSocialId));
    }

    @Test
    @DisplayName("대학생이 회원가입 한다.")
    void signUpUser() throws Exception {
        String request = objectMapper.writeValueAsString(
                new SignUpRequest(anonymousUserSocialId, "01012345678", "새로운닉네임",
                        true, "major", "field", true)
        );

        mvc.perform(post("/auth/user/signup")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(AUTH_CREATE.getCode()))
                .andExpect(jsonPath("$.message").value(SUCCESS_AUTH.getMessage()))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.role").value(USER.name()));
    }

    @Test
    @DisplayName("닉네임은 6글자 이하만 허용한다")
    void signUpUserInvalidNickName() throws Exception {
        String request = objectMapper.writeValueAsString(
                new SignUpRequest(anonymousUserSocialId, "01012345678", "nickname",
                        true, "major", "field", true)
        );

        mvc.perform(post("/auth/user/signup")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(VALID_BLANK.getCode()));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("희망 대학원/학과와 연구분야를 입력하지 않아도 대학생 회원가입이 가능하다.")
    void signUpUserWithoutWish(String empty) throws Exception {
        String request = objectMapper.writeValueAsString(
                new SignUpRequest(anonymousUserSocialId, "01012345678", "새로운닉네임",
                        true, empty, empty, false)
        );

        mvc.perform(post("/auth/user/signup")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(AUTH_CREATE.getCode()))
                .andExpect(jsonPath("$.message").value(SUCCESS_AUTH.getMessage()))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.role").value(USER.name()));
    }

    @Test
    @DisplayName("대학원생이 대학생으로 변경한다.")
    void changeUserToken() throws Exception {
        Wish wish = new Wish(0L, "major", "field", true, user, Status.MATCHED);
        wishRepository.save(wish);

        String token = jwtUtil.generateAccessToken(user.getUserId(), SENIOR);

        mvc.perform(post("/auth/user/token")
                        .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(AUTH_CREATE.getCode()))
                .andExpect(jsonPath("$.message").value(SUCCESS_AUTH.getMessage()))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.role").value(USER.name()));
    }

    @Test
    @DisplayName("대학생으로 가입하지 않은 경우 대학생으로 변경할 수 없다.")
    void changeUserTokenWithoutWish() throws Exception {
        String token = jwtUtil.generateAccessToken(user.getUserId(), SENIOR);

        mvc.perform(post("/auth/user/token")
                        .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(USER_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(NOT_FOUND_USER.getMessage()));
    }

    @Test
    @DisplayName("선배가 후배로 추가 가입합니다.")
    void changeUser() throws Exception {
        String seniorAccessToken = jwtUtil.generateAccessToken(user.getUserId(), SENIOR);

        String request = objectMapper.writeValueAsString(
                new UserChangeRequest("major", "field", true)
        );

        mvc.perform(post("/auth/user/change")
                        .header(AUTHORIZATION, BEARER + seniorAccessToken)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(AUTH_CREATE.getCode()))
                .andExpect(jsonPath("$.message").value(SUCCESS_AUTH.getMessage()))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.role").value(USER.name()));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("전공과 분야가 없어도 후배로 추가 가입할 수 있다")
    void changeUser(String empty) throws Exception {
        String seniorAccessToken = jwtUtil.generateAccessToken(user.getUserId(), SENIOR);

        String request = objectMapper.writeValueAsString(
                new UserChangeRequest(empty, empty, FALSE)
        );

        mvc.perform(post("/auth/user/change")
                        .header(AUTHORIZATION, BEARER + seniorAccessToken)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(AUTH_CREATE.getCode()))
                .andExpect(jsonPath("$.message").value(SUCCESS_AUTH.getMessage()));
    }

    @Test
    @DisplayName("선배가 회원가입한다.")
    void singUpSenior() throws Exception {
        String request = objectMapper.writeValueAsString(
                new SeniorSignUpRequest(anonymousUserSocialId, "01012345678", "새로운닉네임",
                        true, "전공", "서울대학교", "교수", "연구실",
                        "AI", "키워드", "chatLink")
        );

        mvc.perform(post("/auth/senior/signup")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_CREATE.getCode()))
                .andExpect(jsonPath("$.message").value(CREATE_SENIOR.getMessage()))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.role").value(SENIOR.name()));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("필수 정보를 입력하지 않으면 선배로 회원가입할 수 없다.")
    void singUpSenior(String empty) throws Exception {
        String request = objectMapper.writeValueAsString(
                new SeniorSignUpRequest(anonymousUserSocialId, "01012345678", "새로운닉네임",
                        true, empty, empty, empty, empty, empty, empty, empty)
        );

        mvc.perform(post("/auth/senior/signup")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(VALID_BLANK.getCode()));
    }

    @Test
    @DisplayName("후배가 선배로 추가 가입합니다.")
    void changeSenior() throws Exception {
        String userAccessToken = jwtUtil.generateAccessToken(user.getUserId(), USER);

        String request = objectMapper.writeValueAsString(
                new SeniorChangeRequest("major", "field", "교수", "연구실",
                        "AI", "키워드", "chatLink")
        );

        mvc.perform(post("/auth/senior/change")
                        .header(AUTHORIZATION, BEARER + userAccessToken)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_CREATE.getCode()))
                .andExpect(jsonPath("$.message").value(CREATE_SENIOR.getMessage()))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.role").value(SENIOR.name()));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("선배 필수 정보가 없으면 선배로 추가 가입할 수 없다")
    void changeSenior(String empty) throws Exception {
        String userAccessToken = jwtUtil.generateAccessToken(user.getUserId(), USER);

        String request = objectMapper.writeValueAsString(
                new SeniorChangeRequest(empty, empty, empty, empty, empty, empty, empty)
        );

        mvc.perform(post("/auth/senior/change")
                        .header(AUTHORIZATION, BEARER + userAccessToken)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(VALID_BLANK.getCode()));
    }

    @Test
    @DisplayName("대학생이 대학원생으로 변경한다.")
    void changeSeniorToken() throws Exception {
        User senior = new User(0L, 2L, "mail", "선배", "011", "profile", 0, SENIOR, true, now(), now(), false);
        userRepository.save(senior);

        String token = jwtUtil.generateAccessToken(senior.getUserId(), USER);

        mvc.perform(post("/auth/senior/token")
                        .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(AUTH_CREATE.getCode()))
                .andExpect(jsonPath("$.message").value(SUCCESS_AUTH.getMessage()))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.role").value(SENIOR.name()));
    }

    @Test
    @DisplayName("대학원생으로 가입하지 않은 경우 대학원생으로 변경할 수 없다.")
    void changeSeniorTokenWithoutWish() throws Exception {
        String token = jwtUtil.generateAccessToken(user.getUserId(), USER);

        mvc.perform(post("/auth/senior/token")
                        .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(NOT_FOUND_SENIOR.getMessage()));
    }

    @Test
    @DisplayName("토큰을 재발급한다.")
    void refresh() throws Exception {
        Wish wish = new Wish(0L, "major", "field", true, user, Status.MATCHED);
        wishRepository.save(wish);

        String refreshToken = jwtUtil.generateRefreshToken(user.getUserId(), USER);
        when(redisRepository.getValues(any())).thenReturn(Optional.of(refreshToken));

        mvc.perform(post("/auth/refresh")
                        .header(AUTHORIZATION, BEARER + refreshToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(AUTH_UPDATE.getCode()))
                .andExpect(jsonPath("$.message").value(SUCCESS_REGENERATE_TOKEN.getMessage()))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.role").value(USER.name()));
    }

    @Test
    @DisplayName("로그아웃한다.")
    void logout() throws Exception {
        String accessToken = jwtUtil.generateAccessToken(user.getUserId(), USER);

        mvc.perform(post("/auth/logout")
                        .header(AUTHORIZATION, BEARER + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(AUTH_DELETE.getCode()))
                .andExpect(jsonPath("$.message").value(LOGOUT_USER.getMessage()));
    }
}