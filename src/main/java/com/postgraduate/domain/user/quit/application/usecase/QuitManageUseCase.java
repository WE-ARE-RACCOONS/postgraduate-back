package com.postgraduate.domain.user.quit.application.usecase;

import com.postgraduate.domain.auth.application.dto.req.SignOutRequest;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorDeleteService;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.user.quit.application.mapper.QuitMapper;
import com.postgraduate.domain.user.quit.application.utils.QuitUtils;
import com.postgraduate.domain.user.quit.domain.entity.Quit;
import com.postgraduate.domain.user.user.domain.entity.User;
import com.postgraduate.domain.user.user.domain.entity.constant.Role;
import com.postgraduate.domain.user.quit.domain.service.QuitSaveService;
import com.postgraduate.domain.user.user.domain.service.UserDeleteService;
import com.postgraduate.domain.user.user.domain.service.UserGetService;
import com.postgraduate.domain.user.user.domain.service.UserUpdateService;
import com.postgraduate.domain.user.user.exception.DeletedUserException;
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
    private final UserUpdateService userUpdateService;
    private final SeniorGetService seniorGetService;
    private final SeniorDeleteService seniorDeleteService;
    private final QuitSaveService quitSaveService;
    private final QuitUtils quitUtils;

    public void updateDelete(User user, SignOutRequest signOutRequest) {
        user = userGetService.byUserId(user.getUserId());
        checkDeleteCondition(user);
        Quit quit = QuitMapper.mapToQuit(user, signOutRequest);
        quitSaveService.save(quit);
        userUpdateService.updateDelete(user);
    }

    private void checkDeleteCondition(User user) {
        if (user.isDelete())
            throw new DeletedUserException();
        if (user.getRole().equals(Role.SENIOR)) {
            Senior senior = seniorGetService.byUser(user);
            quitUtils.checkDeleteCondition(senior);
            return;
        }
        quitUtils.checkDeleteCondition(user);
    }

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
