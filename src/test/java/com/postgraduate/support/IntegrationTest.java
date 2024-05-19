package com.postgraduate.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postgraduate.domain.account.domain.repository.AccountRepository;
import com.postgraduate.domain.auth.application.usecase.oauth.kakao.KakaoAccessTokenUseCase;
import com.postgraduate.domain.available.domain.repository.AvailableRepository;
import com.postgraduate.domain.mentoring.domain.repository.MentoringRepository;
import com.postgraduate.domain.payment.domain.repository.PaymentRepository;
import com.postgraduate.domain.salary.domain.repository.SalaryRepository;
import com.postgraduate.domain.senior.domain.repository.SeniorRepository;
import com.postgraduate.domain.user.domain.repository.UserRepository;
import com.postgraduate.domain.wish.domain.repository.WishRepository;
import com.postgraduate.global.aop.lock.DistributeLockAspect;
import com.postgraduate.global.config.redis.RedisRepository;
import com.postgraduate.global.config.security.jwt.util.JwtUtils;
import com.postgraduate.global.slack.SlackLogErrorMessage;
import com.postgraduate.global.slack.SlackSignUpMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Disabled
public class IntegrationTest {

    @Autowired
    protected MockMvc mvc;
    @Autowired
    protected WebApplicationContext ctx;
    @Autowired
    protected JwtUtils jwtUtil;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    protected KakaoAccessTokenUseCase kakaoAccessTokenUseCase;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected WishRepository wishRepository;
    @MockBean
    protected RedisRepository redisRepository;
    @MockBean
    protected SlackLogErrorMessage slackLogErrorMessage;
    @Autowired
    protected SeniorRepository seniorRepository;
    @Autowired
    protected MentoringRepository mentoringRepository;
    @Autowired
    protected SalaryRepository salaryRepository;
    @Autowired
    protected AccountRepository accountRepository;
    @Autowired
    protected AvailableRepository availableRepository;
    @Autowired
    protected PaymentRepository paymentRepository;
    @MockBean
    protected SlackSignUpMessage slackSignUpMessage;
    @MockBean
    protected DistributeLockAspect distributeLockAspect;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(ctx)
                .apply(springSecurity())
                .alwaysExpect(status().isOk())
                .alwaysExpect(content().contentType(MediaType.APPLICATION_JSON))
                .build();
    }
}
