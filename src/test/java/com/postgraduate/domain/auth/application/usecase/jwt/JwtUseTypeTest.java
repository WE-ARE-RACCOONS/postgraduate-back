//package com.postgraduate.domain.auth.application.usecase.jwt;
//
//import com.postgraduate.domain.member.user.domain.entity.MemberRole;
//import com.postgraduate.global.auth.login.application.dto.res.JwtTokenResponse;
//import com.postgraduate.global.auth.login.application.usecase.jwt.JwtUseCase;
//import com.postgraduate.domain.member.user.domain.entity.User;
//import com.postgraduate.domain.member.user.exception.DeletedUserException;
//import com.postgraduate.global.config.security.jwt.exception.InvalidRefreshTokenException;
//import com.postgraduate.global.config.security.jwt.exception.NoneRefreshTokenException;
//import com.postgraduate.global.config.security.jwt.util.JwtUtils;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.mock.web.MockHttpServletRequest;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static com.postgraduate.domain.member.user.domain.entity.constant.Role.SENIOR;
//import static com.postgraduate.domain.member.user.domain.entity.constant.Role.USER;
//import static java.lang.Boolean.FALSE;
//import static java.lang.Boolean.TRUE;
//import static java.time.LocalDate.now;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.BDDMockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class JwtUseTypeTest {
//    @Mock
//    private JwtUtils jwtUtils;
//    @InjectMocks
//    private JwtUseCase jwtUseCase;
//
//    private User user;
//
//    @BeforeEach
//    void setting() {
//        user = new User(1L, 1L, "a",
//                "a", "123", "a",
//                1, USER, TRUE, LocalDateTime.now(), LocalDateTime.now(), FALSE, FALSE);
//    }
//
//    @Test
//    @DisplayName("USER 로그인")
//    void signInWithUser() {
//        given(jwtUtils.generateAccessToken(user.getUserId(), USER))
//                .willReturn("accessToken");
//        given(jwtUtils.generateRefreshToken(user.getUserId(), USER))
//                .willReturn("refreshToken");
//
//        JwtTokenResponse jwtTokenResponse = jwtUseCase.signIn(user);
//
//        assertThat(jwtTokenResponse.accessToken())
//                .isEqualTo("accessToken");
//        assertThat(jwtTokenResponse.refreshToken())
//                .isEqualTo("refreshToken");
//        assertThat(jwtTokenResponse.role())
//                .isEqualTo(USER);
//    }
//
//    @Test
//    @DisplayName("SENIOR 로그인")
//    void signInWithSenior() {
//        user = new User(1L, 1L, "a",
//                "a", "123", "a",
//                1, List.of(new MemberRole(-1l, SENIOR, user)), TRUE, LocalDateTime.now(), LocalDateTime.now(), FALSE, FALSE);
//        given(jwtUtils.generateAccessToken(user.getUserId(), ))
//                .willReturn("accessToken");
//        given(jwtUtils.generateRefreshToken(user.getUserId(), user.getRole()))
//                .willReturn("refreshToken");
//
//        JwtTokenResponse jwtTokenResponse = jwtUseCase.signIn(user);
//
//        assertThat(jwtTokenResponse.accessToken())
//                .isEqualTo("accessToken");
//        assertThat(jwtTokenResponse.refreshToken())
//                .isEqualTo("refreshToken");
//        assertThat(jwtTokenResponse.role())
//                .isEqualTo(SENIOR);
//    }
//
//    @Test
//    @DisplayName("로그아웃 실행 테스트")
//    void logout() {
//        jwtUseCase.logout(user);
//
//        verify(jwtUtils, times(1))
//                .makeExpired(user.getUserId());
//    }
//
////    @Test
////    @DisplayName("탈퇴한 USER 로그인")
////    void signInWithUserDelete() {
////        user = new User(1L, 1L, "a",
////                "a", "123", "a",
////                1, USER, TRUE, LocalDateTime.now(), LocalDateTime.now().minusDays(20), TRUE, TRUE);
////
////        assertThatThrownBy(() -> jwtUseCase.signIn(user))
////                .isInstanceOf(UserNotFoundException.class);
////    }
//
//    @Test
//    @DisplayName("탈퇴한 SENIOR 로그인")
//    void signInWithSeniorDelete() {
//        user = new User(1L, 1L, "a",
//                "a", "123", "a",
//                1, SENIOR, TRUE, LocalDateTime.now(), LocalDateTime.now().minusDays(20), TRUE, TRUE);
//
//        assertThatThrownBy(() -> jwtUseCase.signIn(user))
//                .isInstanceOf(DeletedUserException.class);
//    }
//
//    @Test
//    @DisplayName("리프레시 토큰 재발급 테스트")
//    void regenerateToken() {
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        given(jwtUtils.generateAccessToken(user.getUserId(), user.getRole()))
//                .willReturn("accessToken");
//        given(jwtUtils.generateRefreshToken(user.getUserId(), user.getRole()))
//                .willReturn("refreshToken");
//        given(jwtUtils.checkRedis(user.getUserId(), request))
//                .willReturn(USER.toString());
//
//        JwtTokenResponse jwtTokenResponse = jwtUseCase.regenerateToken(user, request);
//
//        verify(jwtUtils, times(1))
//                .checkRedis(eq(user.getUserId()), eq(request));
//        assertThat(jwtTokenResponse.accessToken())
//                .isEqualTo("accessToken");
//        assertThat(jwtTokenResponse.refreshToken())
//                .isEqualTo("refreshToken");
//        assertThat(jwtTokenResponse.role())
//                .isEqualTo(USER);
//    }
//
//    @Test
//    @DisplayName("Redis 비었을 경우 실패")
//    void regenerateTokenWithEmptyRedisToken() {
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        willThrow(NoneRefreshTokenException.class)
//                .given(jwtUtils)
//                .checkRedis(user.getUserId(), request);
//        assertThatThrownBy(() -> jwtUseCase.regenerateToken(user, request))
//                .isInstanceOf(NoneRefreshTokenException.class);
//    }
//
//    @Test
//    @DisplayName("다른 RefreshToken 실패")
//    void regenerateTokenWithDifferentRedisToken() {
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        willThrow(InvalidRefreshTokenException.class)
//                .given(jwtUtils)
//                .checkRedis(user.getUserId(), request);
//
//        assertThatThrownBy(() -> jwtUseCase.regenerateToken(user, request))
//                .isInstanceOf(InvalidRefreshTokenException.class);
//    }
//}