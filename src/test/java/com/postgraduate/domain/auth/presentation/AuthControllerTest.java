package com.postgraduate.domain.auth.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postgraduate.IntegrationTest;
import com.postgraduate.domain.auth.application.dto.req.*;
import com.postgraduate.domain.auth.application.dto.res.KakaoUserInfoResponse;
import com.postgraduate.domain.auth.application.usecase.oauth.kakao.KakaoAccessTokenUseCase;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.entity.constant.Role;
import com.postgraduate.domain.user.domain.repository.UserRepository;
import com.postgraduate.domain.wish.domain.entity.Wish;
import com.postgraduate.domain.wish.domain.entity.constant.Status;
import com.postgraduate.domain.wish.domain.repository.WishRepository;
import com.postgraduate.global.config.security.jwt.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static java.time.LocalDateTime.now;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends IntegrationTest {
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    @Autowired
    private JwtUtils jwtUtil;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private KakaoAccessTokenUseCase kakaoAccessTokenUseCase;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WishRepository wishRepository;
    private User user;
    private final Long anonymousUserSocialId = 2L;


    @BeforeEach
    void setUp() {
        user = new User(0L, 1L, "mail", "후배", "011", "profile", 0, Role.USER, true, now(), now(), false);
        userRepository.save(user);
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
                .andExpect(jsonPath("$.code").value("AU204"))
                .andExpect(jsonPath("$.message").value("사용자 인증에 성공하였습니다."))
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
                .andExpect(jsonPath("$.code").value("AU205"))
                .andExpect(jsonPath("$.message").value("가입하지 않은 유저입니다."))
                .andExpect(jsonPath("$.data.accessToken").doesNotExist())
                .andExpect(jsonPath("$.data.socialId").value(anonymousUserSocialId));
    }

    @Test
    @DisplayName("대학생이 회원가입 한다.")
    void signUpUser() throws Exception {
        authLoginByAnonymousUser();

        String request = objectMapper.writeValueAsString(
                new SignUpRequest(anonymousUserSocialId, "01012345678", "nickname",
                        true, "major", "field", true)
        );

        mvc.perform(post("/auth/user/signup")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("AU202"))
                .andExpect(jsonPath("$.message").value("사용자 인증에 성공하였습니다."))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.role").value("USER"));
    }

    @Test
    @DisplayName("대학원생이 대학생으로 변경한다.")
    void changeUserToken() throws Exception {
        Wish wish = new Wish(0L, "major", "field", true, user, Status.MATCHED);
        wishRepository.save(wish);

        String token = jwtUtil.generateAccessToken(user.getUserId(), Role.SENIOR);

        mvc.perform(post("/auth/user/token")
                        .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("AU202"))
                .andExpect(jsonPath("$.message").value("사용자 인증에 성공하였습니다."))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.role").value("USER"));
    }

    @Test
    @DisplayName("선배가 후배로 추가 가입합니다.")
    void changeUser() throws Exception {
        String seniorAccessToken = jwtUtil.generateAccessToken(user.getUserId(), Role.SENIOR);

        String request = objectMapper.writeValueAsString(
                new UserChangeRequest("major", "field", true)
        );

        mvc.perform(post("/auth/user/change")
                        .header(AUTHORIZATION, BEARER + seniorAccessToken)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("AU202"))
                .andExpect(jsonPath("$.message").value("사용자 인증에 성공하였습니다."))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.role").value("USER"));
    }

    @Test
    void singUpSenior() throws Exception {

        authLoginByAnonymousUser();

        String request = objectMapper.writeValueAsString(
                new SeniorSignUpRequest(anonymousUserSocialId, "01012345678", "nickname",
                        true, "전공", "서울대학교", "교수", "연구실",
                        "AI", "키워드", "certification")
        );

        mvc.perform(post("/auth/senior/signup")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SNR202"))
                .andExpect(jsonPath("$.message").value("대학원생 가입에 성공하였습니다."))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.role").value("SENIOR"));
    }

    @Test
    @DisplayName("후배가 선배로 추가 가입합니다.")
    void changeSenior() throws Exception {
        String userAccessToken = jwtUtil.generateAccessToken(user.getUserId(), Role.USER);

        String request = objectMapper.writeValueAsString(
                new SeniorChangeRequest("major", "field", "교수", "연구실",
                        "AI", "키워드", "certification")
        );

        mvc.perform(post("/auth/senior/change")
                        .header(AUTHORIZATION, BEARER + userAccessToken)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SNR202"))
                .andExpect(jsonPath("$.message").value("대학원생 가입에 성공하였습니다."))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.role").value("SENIOR"));
    }

    @Test
    @DisplayName("대학생이 대학원생으로 변경한다.")
    void changeSeniorToken() throws Exception {
        String token = jwtUtil.generateAccessToken(user.getUserId(), Role.USER);

        mvc.perform(post("/auth/senior/token")
                        .header(AUTHORIZATION, BEARER + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("AU202"))
                .andExpect(jsonPath("$.message").value("사용자 인증에 성공하였습니다."))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.role").value("SENIOR"));
    }

    @Test
    void refresh() {
    }
}