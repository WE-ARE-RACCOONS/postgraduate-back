package com.postgraduate.domain.user.presentation;

import com.postgraduate.support.ControllerTest;
import com.postgraduate.domain.user.application.dto.req.UserInfoRequest;
import com.postgraduate.domain.user.application.dto.res.UserInfoResponse;
import com.postgraduate.domain.user.application.dto.res.UserMyPageResponse;
import com.postgraduate.domain.user.application.dto.res.UserPossibleResponse;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.exception.PhoneNumberException;
import com.postgraduate.domain.user.presentation.constant.UserResponseCode;
import com.postgraduate.domain.user.presentation.constant.UserResponseMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static com.postgraduate.domain.user.presentation.constant.UserResponseCode.USER_FIND;
import static com.postgraduate.domain.user.presentation.constant.UserResponseCode.USER_UPDATE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends ControllerTest {
    private static final String BEARER = "Bearer token";

    User user= resource.getUser();
    @Test
    @DisplayName("유저 마이페이지 정보를 조회한다")
    @WithMockUser
    void getUserInfo() throws Exception {
        given(userMyPageUseCase.getUserInfo(any()))
                .willReturn(new UserMyPageResponse(user.getNickName(), user.getProfile()));

        mvc.perform(get("/user/me")
                        .header(AUTHORIZATION, BEARER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(USER_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_USER_INFO.getMessage()))
                .andExpect(jsonPath("$.data.nickName").value(user.getNickName()))
                .andExpect(jsonPath("$.data.profile").value(user.getProfile()));
    }

    @Test
    @DisplayName("유저 마이페이지 수정 전 기본 정보를 조회한다")
    @WithMockUser
    void getOriginUserInfo() throws Exception {
        UserInfoResponse userInfoResponse = new UserInfoResponse(user.getProfile(), user.getNickName(), user.getPhoneNumber());
        given(userMyPageUseCase.getUserOriginInfo(any()))
                .willReturn(userInfoResponse);
        mvc.perform(get("/user/me/info")
                        .header(HttpHeaders.AUTHORIZATION, BEARER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(USER_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_USER_INFO.getMessage()))
                .andExpect(jsonPath("$.data.profile").value(userInfoResponse.profile()))
                .andExpect(jsonPath("$.data.nickName").value(userInfoResponse.nickName()))
                .andExpect(jsonPath("$.data.phoneNumber").value(userInfoResponse.phoneNumber()));
    }

    @Test
    @DisplayName("유저 마이페이지 정보를 수정한다.")
    @WithMockUser
    void updateInfo() throws Exception {
        String request = objectMapper.writeValueAsString(
                new UserInfoRequest("new_profile", "new후배", "01012345667")
        );

        willDoNothing().given(userManageUseCase)
                .updateInfo(any(), any());

        mvc.perform(patch("/user/me/info")
                        .with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, BEARER )
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(USER_UPDATE.getCode()))
                .andExpect(jsonPath("$.message").value(UPDATE_USER_INFO.getMessage()));
    }

    @Test
    @DisplayName("잘못된 번호로 수정할 수 없다")
    @WithMockUser
    void updateInvalidPhoneNumber() throws Exception {
        UserInfoRequest userInfoRequest = new UserInfoRequest("new_profile", "new후배", "phoneNumber");
        String request = objectMapper.writeValueAsString(
                userInfoRequest
        );

        willThrow(new PhoneNumberException())
                .given(userManageUseCase)
                .updateInfo(any(), any());

        mvc.perform(patch("/user/me/info")
                        .with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, BEARER )
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(UserResponseCode.INVALID_PHONE_NUMBER.getCode()))
                .andExpect(jsonPath("$.message").value(UserResponseMessage.INVALID_PHONE_NUMBER.getMessage()));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @WithMockUser
    @DisplayName("선배 전환 가능 여부 테스트")
    void checkRoleTrue(boolean tf) throws Exception {
        UserPossibleResponse possibleResponse = new UserPossibleResponse(tf, user.getSocialId());

        given(userMyPageUseCase.checkSenior(any()))
                .willReturn(possibleResponse);

        mvc.perform(get("/user/me/role")
                        .header(HttpHeaders.AUTHORIZATION, BEARER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(USER_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_SENIOR_CHECK.getMessage()))
                .andExpect(jsonPath("$.data.possible").value(possibleResponse.possible()))
                .andExpect(jsonPath("$.data.socialId").value(possibleResponse.socialId()));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @DisplayName("사용 가능한 닉네임 중복체크를 한다")
    @WithMockUser
    void duplicatedPossibleNickNameWithTrue(boolean tf) throws Exception {
        given(userManageUseCase.duplicatedNickName(any()))
                .willReturn(tf);

        mvc.perform(get("/user/nickname")
                        .param("nickName", "new후배"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(USER_FIND.getCode()))
                .andExpect(jsonPath("$.message").value(GET_NICKNAME_CHECK.getMessage()))
                .andExpect(jsonPath("$.data").value(tf));
    }
}