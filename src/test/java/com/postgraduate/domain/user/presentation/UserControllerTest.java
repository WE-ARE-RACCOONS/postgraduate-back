package com.postgraduate.domain.user.presentation;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserController.class)
class UserControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    protected ObjectMapper objectMapper;
//    @MockBean
//    private UserMyPageUseCase userMyPageUseCase;
//    private User user;
//
//    @BeforeEach
//    public void setUp(final WebApplicationContext context) throws Exception {
//        user = User.builder()
//                .userId(1000000000L)
//                .socialId(1000000000L)
//                .email("test.com")
//                .role(USER)
//                .nickName("test")
//                .profile("test")
//                .point(0)
//                .createdAt(LocalDate.now())
//                .updatedAt(LocalDate.now())
//                .build();
//
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
//                .alwaysDo(MockMvcResultHandlers.print()) // andDo(print()) 코드 포함
//                .addFilters(new CharacterEncodingFilter("UTF-8", true)) // 한글 깨짐 방지
//                .build();
//    }
//
//    @Test
//    @DisplayName("대학생 기본 정보 조회")
//    void getUserInfo() throws Exception {
//        //given
//        UserInfoResponse infoResponse = UserInfoResponse.builder()
//                .profile(user.getProfile())
//                .nickName(user.getNickName())
//                .point(user.getPoint())
//                .build();
//        given(userMyPageUseCase.getUserInfo(any())).willReturn(infoResponse);
//
//        //when
//        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
//                .get("/user/me")
//                .with(SecurityMockMvcRequestPostProcessors.user(user.getUserId().toString()))
//                .contentType(MediaType.APPLICATION_JSON)
//                .header("Authorization", "Bearer ACCESS_TOKEN"));
//
//        //then
//        resultActions.andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.nickName").value(user.getNickName()))
//                .andExpect(jsonPath("$.data.profile").value(user.getProfile()))
//                .andExpect(jsonPath("$.data.point").value(user.getPoint()));
//    }
//
//    @Test
//    @DisplayName("닉네임 변경")
//    void updateNickName() throws Exception {
//        // given
//        UserNickNameRequest userNickNameRequest = new UserNickNameRequest("change");
//        String body = objectMapper.writeValueAsString(userNickNameRequest);
//
//        //when
//        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
//                .patch("/user/nickname")
//                .with(SecurityMockMvcRequestPostProcessors.user(user.getUserId().toString()))
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(body)
//                .header("Authorization", "Bearer ACCESS_TOKEN"));
//
//        //then
//        resultActions.andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(USER_UPDATE.getCode()))
//                .andExpect(jsonPath("$.message").value(UPDATE_USER_INFO.getMessage()));
//    }
//
//    @Test
//    @DisplayName("닉네임 중복체크 - true")
//    void dupNickWithTrue() throws Exception {
//        // given
//        given(userMyPageUseCase.duplicatedNickName("check")).willReturn(true);
//
//        //when
//        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
//                .get("/user/nickname")
//                .with(SecurityMockMvcRequestPostProcessors.user(user.getUserId().toString()))
//                .contentType(MediaType.APPLICATION_JSON)
//                .param("nickName", "check")
//                .header("Authorization", "Bearer ACCESS_TOKEN"));
//
//        //then
//        resultActions.andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(USER_FIND.getCode()))
//                .andExpect(jsonPath("$.message").value(GET_NICKNAME_CHECK.getMessage()))
//                .andExpect(jsonPath("$.data").value(true));
//    }
//
//    @Test
//    @DisplayName("닉네임 중복체크 - false")
//    void dupNickWithFalse() throws Exception {
//        // given
//        given(userMyPageUseCase.duplicatedNickName("check")).willReturn(false);
//
//        //when
//        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
//                .get("/user/nickname")
//                .with(SecurityMockMvcRequestPostProcessors.user(user.getUserId().toString()))
//                .contentType(MediaType.APPLICATION_JSON)
//                .param("nickName", "check")
//                .header("Authorization", "Bearer ACCESS_TOKEN"));
//
//        //then
//        resultActions.andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(USER_FIND.getCode()))
//                .andExpect(jsonPath("$.message").value(GET_NICKNAME_CHECK.getMessage()))
//                .andExpect(jsonPath("$.data").value(false));
//    }
//
//    @Test
//    @DisplayName("프로필 업데이트")
//    void updateProfile() throws Exception {
//        //given
//        UserProfileRequest userProfileRequest = new UserProfileRequest("change");
//        String body = objectMapper.writeValueAsString(userProfileRequest);
//
//        //when
//        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
//                .patch("/user/profile")
//                .with(SecurityMockMvcRequestPostProcessors.user(user.getUserId().toString()))
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(body)
//                .header("Authorization", "Bearer ACCESS_TOKEN"));
//
//        //then
//        resultActions.andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(USER_UPDATE.getCode()))
//                .andExpect(jsonPath("$.message").value(UPDATE_USER_INFO.getMessage()));
//    }
}