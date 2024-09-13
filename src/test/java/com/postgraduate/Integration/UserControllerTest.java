//package com.postgraduate.Integration;
//
//import com.postgraduate.domain.member.user.application.dto.req.UserInfoRequest;
//import com.postgraduate.domain.member.user.domain.entity.User;
//import com.postgraduate.domain.member.user.domain.entity.constant.Role;
//import com.postgraduate.domain.member.user.presentation.constant.UserResponseCode;
//import com.postgraduate.support.IntegrationTest;
//import com.postgraduate.support.Resource;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ValueSource;
//import org.springframework.http.MediaType;
//
//import java.io.IOException;
//
//import static com.postgraduate.domain.member.user.presentation.constant.UserResponseCode.*;
//import static com.postgraduate.domain.member.user.presentation.constant.UserResponseMessage.*;
//import static com.postgraduate.domain.member.user.presentation.constant.UserResponseMessage.INVALID_PHONE_NUMBER;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.doNothing;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//class UserControllerTest extends IntegrationTest {
//    private static final String AUTHORIZATION = "Authorization";
//    private static final String BEARER = "Bearer ";
//    private Resource resource = new Resource();
//    private String token;
//
//    @BeforeEach
//    void setUp() throws IOException {
//        User user = resource.getUser();
//        userRepository.save(user);
//
//        token = jwtUtil.generateAccessToken(user.getUserId(), Role.USER);
//
//        doNothing().when(slackLogErrorMessage).sendSlackLog(any());
//    }
//
//    @Test
//    @DisplayName("유저 마이페이지 정보를 조회한다")
//    void getUserInfo() throws Exception {
//        mvc.perform(get("/user/me")
//                        .header(AUTHORIZATION, BEARER + token))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(USER_FIND.getCode()))
//                .andExpect(jsonPath("$.message").value(GET_USER_INFO.getMessage()))
//                .andExpect(jsonPath("$.data.nickName").isNotEmpty())
//                .andExpect(jsonPath("$.data.profile").isNotEmpty());
//    }
//
//    @Test
//    @DisplayName("유저 마이페이지 수정 전 기본 정보를 조회한다")
//    void getOriginUserInfo() throws Exception {
//        mvc.perform(get("/user/me/info")
//                        .header(AUTHORIZATION, BEARER + token))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(USER_FIND.getCode()))
//                .andExpect(jsonPath("$.message").value(GET_USER_INFO.getMessage()))
//                .andExpect(jsonPath("$.data.profile").isNotEmpty())
//                .andExpect(jsonPath("$.data.nickName").isNotEmpty())
//                .andExpect(jsonPath("$.data.phoneNumber").isNotEmpty());
//    }
//
//    @Test
//    @DisplayName("유저 마이페이지 정보를 수정한다.")
//    void updateInfo() throws Exception {
//        String request = objectMapper.writeValueAsString(
//                new UserInfoRequest("new_profile", "new후배", "01012345667")
//        );
//        mvc.perform(patch("/user/me/info")
//                        .header(AUTHORIZATION, BEARER + token)
//                        .content(request)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(USER_UPDATE.getCode()))
//                .andExpect(jsonPath("$.message").value(UPDATE_USER_INFO.getMessage()));
//    }
//
//    @ParameterizedTest
//    @ValueSource(strings = {"0101234567", "번호를한글열하나글자로"})
//    @DisplayName("잘못된 번호로 수정할 수 없다")
//    void updateInvalidPhoneNumber(String phoneNumber) throws Exception {
//        String request = objectMapper.writeValueAsString(
//                new UserInfoRequest("new_profile", "new후배", phoneNumber)
//        );
//        mvc.perform(patch("/user/me/info")
//                        .header(AUTHORIZATION, BEARER + token)
//                        .content(request)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(UserResponseCode.INVALID_PHONE_NUMBER.getCode()))
//                .andExpect(jsonPath("$.message").value(INVALID_PHONE_NUMBER.getMessage()));
//    }
//
//    @Test
//    @DisplayName("선배 전환 가능 여부를 확인한다")
//    void checkRole() throws Exception {
//        mvc.perform(get("/user/me/role")
//                        .header(AUTHORIZATION, BEARER + token))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(USER_FIND.getCode()))
//                .andExpect(jsonPath("$.message").value(GET_SENIOR_CHECK.getMessage()))
//                .andExpect(jsonPath("$.data.possible").isNotEmpty())
//                .andExpect(jsonPath("$.data.socialId").isNotEmpty());
//    }
//
//    @Test
//    @DisplayName("사용 가능한 닉네임 중복체크를 한다")
//    void duplicatedPossibleNickName() throws Exception {
//        mvc.perform(get("/user/nickname")
//                        .param("nickName", "new후배"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(USER_FIND.getCode()))
//                .andExpect(jsonPath("$.message").value(GET_NICKNAME_CHECK.getMessage()))
//                .andExpect(jsonPath("$.data").value(true));
//    }
//
//    @Test
//    @DisplayName("사용 불가능한 닉네임 중복체크를 한다")
//    void duplicatedImpossibleNickName() throws Exception {
//        mvc.perform(get("/user/nickname")
//                        .param("nickName", "후배"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(USER_FIND.getCode()))
//                .andExpect(jsonPath("$.message").value(GET_NICKNAME_CHECK.getMessage()))
//                .andExpect(jsonPath("$.data").value(false));
//    }
//}