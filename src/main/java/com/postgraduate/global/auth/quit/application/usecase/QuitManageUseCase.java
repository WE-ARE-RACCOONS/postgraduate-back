package com.postgraduate.global.auth.quit.application.usecase;

import com.postgraduate.domain.senior.domain.service.SeniorDeleteService;
import com.postgraduate.domain.user.user.domain.entity.User;
import com.postgraduate.domain.user.user.domain.service.UserDeleteService;
import com.postgraduate.domain.user.user.domain.service.UserGetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class QuitManageUseCase {
    private final UserGetService userGetService;
    private final UserDeleteService userDeleteService;
    private final SeniorDeleteService seniorDeleteService;

    @Scheduled(cron = "0 0 1 * * *", zone = "Asia/Seoul")
    public void updateRealDelete() {
        userGetService.byDelete()
                .stream()
                .filter(User::isRealDelete)
                .forEach(
                        user -> {
                            if (user.isSenior()) {
                                seniorDeleteService.deleteSenior(user);
                            }
                            userDeleteService.deleteUser(user);
                        }
                );
    }
}
