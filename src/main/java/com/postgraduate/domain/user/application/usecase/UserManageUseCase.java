package com.postgraduate.domain.user.application.usecase;

import com.postgraduate.domain.auth.application.dto.req.SignOutRequest;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.user.application.dto.req.UserInfoRequest;
import com.postgraduate.domain.user.application.utils.QuitUtils;
import com.postgraduate.domain.user.application.utils.UserUtils;
import com.postgraduate.domain.user.domain.entity.Quit;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.service.QuitSaveService;
import com.postgraduate.domain.user.domain.service.UserGetService;
import com.postgraduate.domain.user.domain.service.UserUpdateService;
import com.postgraduate.domain.user.exception.DeletedUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.postgraduate.domain.user.application.mapper.QuitMapper.mapToQuit;
import static com.postgraduate.domain.user.domain.entity.constant.Role.SENIOR;

@Transactional
@Service
@RequiredArgsConstructor
public class UserManageUseCase {
    private final UserUpdateService userUpdateService;
    private final UserGetService userGetService;
    private final QuitSaveService quitSaveService;
    private final SeniorGetService seniorGetService;
    private final QuitUtils quitUtils;
    private final UserUtils userUtils;

    public void updateInfo(User user, UserInfoRequest userInfoRequest) {
        userUtils.checkPhoneNumber(userInfoRequest.phoneNumber());
        user = userGetService.byUserId(user.getUserId());
        userUpdateService.updateInfo(user, userInfoRequest);
    }

    public void updateDelete(User user, SignOutRequest signOutRequest) {
        user = userGetService.byUserId(user.getUserId());
        checkDeleteCondition(user);
        Quit quit = mapToQuit(user, signOutRequest);
        quitSaveService.save(quit);
        userUpdateService.updateDelete(user);
    }

    private void checkDeleteCondition(User user) {
        if (user.isDelete())
            throw new DeletedUserException();
        if (user.getRole().equals(SENIOR)) {
            Senior senior = seniorGetService.byUser(user);
            quitUtils.checkDeleteCondition(senior);
            return;
        }
        quitUtils.checkDeleteCondition(user);
    }

    @Transactional(readOnly = true)
    public Boolean duplicatedNickName(String nickName) {
        return userGetService.byNickName(nickName).isEmpty();
    }
}
