package com.postgraduate.domain.user.application.usecase;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class UserMyPageUseCaseTest {
//    @Mock
//    UserGetService userGetService;
//    @InjectMocks
//    UserMyPageUseCase userMyPageUseCase;
//
//    private User testUser;
//    @BeforeEach
//    void setTestUser() {
//        Hope hope = new Hope("computer","ai", true);
//        testUser = new User(100000000L, 12345L, "test.com",
//                "test", "test.png", "01012341234", 0, Role.USER, hope, false,
//                LocalDate.now(), LocalDate.now());
//    }
//
//    @Test
//    @DisplayName("유저 기본 정보 반환 테스트")
//    void getUserInfo() {
//        UserInfoResponse expected = new UserInfoResponse(testUser.getNickName(), testUser.getProfile(), testUser.getPoint());
//        UserInfoResponse actual = userMyPageUseCase.getUserInfo(testUser);
//
//        assertThat(expected.getNickName())
//                .isEqualTo(actual.getNickName());
//        assertThat(expected.getProfile())
//                .isEqualTo(actual.getProfile());
//        assertThat(expected.getPoint())
//                .isEqualTo(actual.getPoint());
//    }
//
//    @Test
//    @DisplayName("닉네임 중복되는 경우 테스트")
//    void duplicatedNick() {
//        given(userGetService.byNickName("test"))
//                .willReturn(Optional.ofNullable(testUser));
//
//        assertThat(userMyPageUseCase.duplicatedNickName("test"))
//                .isFalse();
//    }
//
//    @Test
//    @DisplayName("닉네임 중복되지 않는 경우 테스트")
//    void notDuplicatedNick() {
//        given(userGetService.byNickName("test"))
//                .willReturn(Optional.ofNullable(null));
//
//        assertThat(userMyPageUseCase.duplicatedNickName("test"))
//                .isTrue();
//    }
}
