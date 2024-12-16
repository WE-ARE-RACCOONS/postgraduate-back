package com.postgraduate.domain.auth.presentation;

import com.postgraduate.global.auth.login.application.dto.req.*;
import com.postgraduate.global.auth.login.application.dto.res.AuthUserResponse;
import com.postgraduate.global.auth.login.application.dto.res.JwtTokenResponse;
import com.postgraduate.global.auth.login.presentation.constant.Provider;
import com.postgraduate.domain.member.user.domain.entity.User;
import com.postgraduate.support.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static com.postgraduate.domain.member.senior.presentation.constant.SeniorResponseCode.*;
import static com.postgraduate.domain.member.senior.presentation.constant.SeniorResponseMessage.*;
import static com.postgraduate.domain.member.user.domain.entity.constant.Role.USER;
import static com.postgraduate.global.auth.login.presentation.constant.AuthResponseCode.*;
import static com.postgraduate.global.auth.login.presentation.constant.AuthResponseMessage.*;
import static java.lang.Boolean.TRUE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends ControllerTest {
    private static final String BEARER = "Bearer token";

    User user = resource.getUser();

    @Test
    @WithMockUser
    @DisplayName("대학생이 로그인한다.")
    void authLoginByUser() throws Exception {
        CodeRequest codeRequest = new CodeRequest("code");
        String request = objectMapper.writeValueAsString(codeRequest);
        AuthUserResponse response = new AuthUserResponse(user, user.getSocialId());
        JwtTokenResponse tokenResponse = new JwtTokenResponse("access", 10, "refresh", 10, USER, TRUE);
        given(selectOauth.selectSignIn(Provider.KAKAO))
                .willReturn(kakaoSignInUseCase);
        given(kakaoSignInUseCase.getUser(codeRequest))
                .willReturn(response);
        given(jwtUseCase.signIn(any(), any()))
                .willReturn(tokenResponse);

        mvc.perform(post("/auth/login/KAKAO")
                        .with(csrf())
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(AUTH_ALREADY.getCode()))
                .andExpect(jsonPath("$.message").value(SUCCESS_AUTH.getMessage()))
                .andExpect(jsonPath("$.data.accessToken").value(tokenResponse.accessToken()))
                .andExpect(jsonPath("$.data.accessExpiration").value(tokenResponse.accessExpiration()))
                .andExpect(jsonPath("$.data.refreshToken").value(tokenResponse.refreshToken()))
                .andExpect(jsonPath("$.data.refreshExpiration").value(tokenResponse.refreshExpiration()))
                .andExpect(jsonPath("$.data.role").value(tokenResponse.role().toString()));
    }

    @Test
    @WithMockUser
    @DisplayName("비회원이 로그인한다.")
    void authLoginByAnonymousUser() throws Exception {
        CodeRequest codeRequest = new CodeRequest("code");
        String request = objectMapper.writeValueAsString(codeRequest);
        AuthUserResponse response = new AuthUserResponse(user.getSocialId());
        given(selectOauth.selectSignIn(Provider.KAKAO))
                .willReturn(kakaoSignInUseCase);
        given(kakaoSignInUseCase.getUser(codeRequest))
                .willReturn(response);

        mvc.perform(post("/auth/login/KAKAO")
                        .content(request)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(AUTH_NONE.getCode()))
                .andExpect(jsonPath("$.message").value(NOT_REGISTERED_USER.getMessage()))
                .andExpect(jsonPath("$.data.accessToken").doesNotExist())
                .andExpect(jsonPath("$.data.socialId").value(user.getSocialId()));
    }

    @Test
    @WithMockUser
    @DisplayName("대학생이 회원가입 한다.")
    void signUpUser() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest(user.getSocialId(), user.getPhoneNumber(), user.getNickName(),
                user.getMarketingReceive());
        String request = objectMapper.writeValueAsString(signUpRequest);
        JwtTokenResponse tokenResponse = new JwtTokenResponse("access", 10, "refresh", 10, USER, TRUE);

        given(signUpUseCase.userSignUp(signUpRequest))
                .willReturn(user);
        given(jwtUseCase.signIn(any(), any()))
                .willReturn(tokenResponse);

        mvc.perform(post("/auth/user/signup")
                        .content(request)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(AUTH_CREATE.getCode()))
                .andExpect(jsonPath("$.message").value(SUCCESS_AUTH.getMessage()))
                .andExpect(jsonPath("$.data.accessToken").value(tokenResponse.accessToken()))
                .andExpect(jsonPath("$.data.accessExpiration").value(tokenResponse.accessExpiration()))
                .andExpect(jsonPath("$.data.refreshToken").value(tokenResponse.refreshToken()))
                .andExpect(jsonPath("$.data.refreshExpiration").value(tokenResponse.refreshExpiration()))
                .andExpect(jsonPath("$.data.role").value(tokenResponse.role().toString()));
    }

    @Test
    @WithMockUser
    @DisplayName("대학원생이 대학생으로 변경한다.")
    void changeUserToken() throws Exception {
        JwtTokenResponse tokenResponse = new JwtTokenResponse("access", 10, "refresh", 10, USER, TRUE);

        given(jwtUseCase.changeUser(any(), any()))
                .willReturn(tokenResponse);

        mvc.perform(post("/auth/user/token")
                        .header(HttpHeaders.AUTHORIZATION, BEARER)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(AUTH_CREATE.getCode()))
                .andExpect(jsonPath("$.message").value(SUCCESS_AUTH.getMessage()))
                .andExpect(jsonPath("$.data.accessToken").value(tokenResponse.accessToken()))
                .andExpect(jsonPath("$.data.accessExpiration").value(tokenResponse.accessExpiration()))
                .andExpect(jsonPath("$.data.refreshToken").value(tokenResponse.refreshToken()))
                .andExpect(jsonPath("$.data.refreshExpiration").value(tokenResponse.refreshExpiration()))
                .andExpect(jsonPath("$.data.role").value(tokenResponse.role().toString()));
    }

    @Test
    @WithMockUser
    @DisplayName("선배가 회원가입한다.")
    void singUpSenior() throws Exception {
        String request = objectMapper.writeValueAsString(
                new SeniorSignUpRequest(user.getSocialId(), "01012345678", "새로운닉네임",
                        true, "전공", "서울대학교", "교수", "연구실",
                        "AI", "키워드", "chatLink")
        );
        JwtTokenResponse tokenResponse = new JwtTokenResponse("access", 10, "refresh", 10, USER, TRUE);

        given(signUpUseCase.seniorSignUp(any()))
                .willReturn(user);
        given(jwtUseCase.signIn(any(), any()))
                .willReturn(tokenResponse);

        mvc.perform(post("/auth/senior/signup")
                        .header(HttpHeaders.AUTHORIZATION, BEARER)
                        .with(csrf())
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_CREATE.getCode()))
                .andExpect(jsonPath("$.message").value(CREATE_SENIOR.getMessage()))
                .andExpect(jsonPath("$.data.accessToken").value(tokenResponse.accessToken()))
                .andExpect(jsonPath("$.data.accessExpiration").value(tokenResponse.accessExpiration()))
                .andExpect(jsonPath("$.data.refreshToken").value(tokenResponse.refreshToken()))
                .andExpect(jsonPath("$.data.refreshExpiration").value(tokenResponse.refreshExpiration()));
    }

    @Test
    @WithMockUser
    @DisplayName("후배가 선배로 추가 가입합니다.")
    void changeSenior() throws Exception {
        String request = objectMapper.writeValueAsString(
                new SeniorChangeRequest("major", "field", "교수", "연구실",
                        "AI", "키워드", "chatLink")
        );
        JwtTokenResponse tokenResponse = new JwtTokenResponse("access", 10, "refresh", 10, USER, TRUE);

        given(signUpUseCase.changeSenior(any(), any()))
                .willReturn(user);
        given(jwtUseCase.changeSenior(user, any()))
                .willReturn(tokenResponse);

        mvc.perform(post("/auth/senior/change")
                        .header(HttpHeaders.AUTHORIZATION, BEARER)
                        .with(csrf())
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SENIOR_CREATE.getCode()))
                .andExpect(jsonPath("$.message").value(CREATE_SENIOR.getMessage()))
                .andExpect(jsonPath("$.data.accessToken").value(tokenResponse.accessToken()))
                .andExpect(jsonPath("$.data.accessExpiration").value(tokenResponse.accessExpiration()))
                .andExpect(jsonPath("$.data.refreshToken").value(tokenResponse.refreshToken()))
                .andExpect(jsonPath("$.data.refreshExpiration").value(tokenResponse.refreshExpiration()));
    }

    @Test
    @WithMockUser
    @DisplayName("대학생이 대학원생으로 변경한다.")
    void changeSeniorToken() throws Exception {
        JwtTokenResponse tokenResponse = new JwtTokenResponse("access", 10, "refresh", 10, USER, TRUE);

        given(jwtUseCase.changeSenior(any(), any()))
                .willReturn(tokenResponse);

        mvc.perform(post("/auth/senior/token")
                        .header(HttpHeaders.AUTHORIZATION, BEARER)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(AUTH_CREATE.getCode()))
                .andExpect(jsonPath("$.message").value(SUCCESS_AUTH.getMessage()))
                .andExpect(jsonPath("$.data.accessToken").value(tokenResponse.accessToken()))
                .andExpect(jsonPath("$.data.accessExpiration").value(tokenResponse.accessExpiration()))
                .andExpect(jsonPath("$.data.refreshToken").value(tokenResponse.refreshToken()))
                .andExpect(jsonPath("$.data.refreshExpiration").value(tokenResponse.refreshExpiration()))
                .andExpect(jsonPath("$.data.role").value(tokenResponse.role().toString()));
    }

    @Test
    @WithMockUser
    @DisplayName("토큰을 재발급한다.")
    void refresh() throws Exception {
        JwtTokenResponse tokenResponse = new JwtTokenResponse("access", 10, "refresh", 10, USER, TRUE);

        given(jwtUseCase.regenerateToken(any(), any(), any()))
                .willReturn(tokenResponse);

        mvc.perform(post("/auth/refresh")
                        .header(HttpHeaders.AUTHORIZATION, BEARER)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(AUTH_UPDATE.getCode()))
                .andExpect(jsonPath("$.message").value(SUCCESS_REGENERATE_TOKEN.getMessage()))
                .andExpect(jsonPath("$.data.accessToken").value(tokenResponse.accessToken()))
                .andExpect(jsonPath("$.data.accessExpiration").value(tokenResponse.accessExpiration()))
                .andExpect(jsonPath("$.data.refreshToken").value(tokenResponse.refreshToken()))
                .andExpect(jsonPath("$.data.refreshExpiration").value(tokenResponse.refreshExpiration()))
                .andExpect(jsonPath("$.data.role").value(tokenResponse.role().toString()));
    }

    @Test
    @WithMockUser
    @DisplayName("로그아웃한다.")
    void logout() throws Exception {
        willDoNothing().given(jwtUseCase)
                        .logout(any(), any());

        mvc.perform(post("/auth/logout")
                        .header(HttpHeaders.AUTHORIZATION, BEARER)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(AUTH_DELETE.getCode()))
                .andExpect(jsonPath("$.message").value(LOGOUT_USER.getMessage()));
    }
}