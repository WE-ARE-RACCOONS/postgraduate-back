package com.postgraduate.domain.mentoring.presentation;

import com.postgraduate.IntegrationTest;
import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.mentoring.domain.repository.MentoringRepository;
import com.postgraduate.domain.senior.domain.entity.Info;
import com.postgraduate.domain.senior.domain.entity.Profile;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.repository.SeniorRepository;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.entity.constant.Role;
import com.postgraduate.domain.user.domain.repository.UserRepository;
import com.postgraduate.global.config.security.jwt.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import static com.postgraduate.domain.senior.domain.entity.constant.Status.WAITING;
import static java.time.LocalDateTime.now;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MentoringControllerTest extends IntegrationTest {

    @Autowired
    private JwtUtils jwtUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SeniorRepository seniorRepository;
    @Autowired
    private MentoringRepository mentoringRepository;
    private User user;
    private Senior senior;
    private Long userId;
    private String accessToken;

    @BeforeEach
    void setUp() {
        user = new User(0L, 1L, "mail", "후배", "011", "profile", 0, Role.USER, true, now(), now(), false);
        userRepository.save(user);
        userId = user.getUserId();

        User userOfSenior = new User(0L, 2L, "mail", "선배", "012", "profile", 0, Role.SENIOR, true, now(), now(), false);
        userRepository.save(userOfSenior);

        Info info = new Info("major", "서울대학교", "교수님", "키워드1,키워드2", "랩실", "인공지능", false, false, "인공지능,키워드1,키워드2");
        Profile profile = new Profile("저는요", "한줄소개", "대상", "chatLink", 40);
        senior = new Senior(0L, userOfSenior, "certification", WAITING, 0, info, profile, now(), now());
        seniorRepository.save(senior);

        accessToken = jwtUtil.generateAccessToken(userId, Role.USER);
    }

    @Test
    @DisplayName("대학생이 신청한 멘토링 목록을 조회한다")
    @WithMockUser
    void getWaitingMentorings() throws Exception {
        Mentoring waitingMentoring = new Mentoring(0L, user, senior, "topic", "question", "date", 40, Status.WAITING, now(), now());
        mentoringRepository.save(waitingMentoring);

        mvc.perform(get("/mentoring/me/waiting")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("MT200"))
                .andExpect(jsonPath("$.message").value("멘토링 리스트 조회에 성공하였습니다."))
                .andExpect(jsonPath("$.data.mentoringInfos[0].profile").value(containsString("profile")))
                .andExpect(jsonPath("$.data.mentoringInfos[0].nickName").value(containsString("선배")))
                .andExpect(jsonPath("$.data.mentoringInfos[0].postgradu").value(containsString("서울대학교")))
                .andExpect(jsonPath("$.data.mentoringInfos[0].major").value(containsString("major")))
                .andExpect(jsonPath("$.data.mentoringInfos[0].lab").value(containsString("랩실")))
                .andExpect(jsonPath("$.data.mentoringInfos[0].term").value(40))
                .andExpect(jsonPath("$.data.mentoringInfos[0].chatLink").doesNotExist());
    }

    @Test
    @DisplayName("대학생이 예정된 멘토링 목록을 조회한다")
    @WithMockUser
    void getExpectedMentorings() throws Exception {
        Mentoring expectedMentoring = new Mentoring(0L, user, senior, "topic", "question", "date", 40, Status.EXPECTED, now(), now());
        mentoringRepository.save(expectedMentoring);

        mvc.perform(get("/mentoring/me/expected")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("MT200"))
                .andExpect(jsonPath("$.message").value("멘토링 리스트 조회에 성공하였습니다."))
                .andExpect(jsonPath("$.data.mentoringInfos[0].chatLink").value("chatLink"))
                .andExpect(jsonPath("$.data.mentoringInfos[0].date").value("date"));
    }

    @Test
    @DisplayName("대학생이 완료된 멘토링 목록을 조회한다")
    @WithMockUser
    void getDoneMentorings() throws Exception {
        Mentoring doneMentoring = new Mentoring(0L, user, senior, "topic", "question", "date", 40, Status.DONE, now(), now());
        mentoringRepository.save(doneMentoring);

        mvc.perform(get("/mentoring/me/done")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("MT200"))
                .andExpect(jsonPath("$.message").value("멘토링 리스트 조회에 성공하였습니다."))
                .andExpect(jsonPath("$.data.mentoringInfos[0].chatLink").doesNotExist());
    }
}