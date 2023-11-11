package com.postgraduate.domain.admin.application.usecase;

import com.postgraduate.domain.admin.application.dto.res.UserResponse;
import com.postgraduate.domain.admin.application.mapper.AdminMapper;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.service.SeniorGetService;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.entity.constant.Role;
import com.postgraduate.domain.user.domain.service.UserGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserManageByAdminUseCase {
    private final UserGetService userGetService;
    private final SeniorGetService seniorGetService;
    public List<UserResponse> getUsers() {
        List<User> users = userGetService.all();
        List<UserResponse> userResponses = new ArrayList<>();
        users.forEach(user -> {
            if (user.getRole() == Role.SENIOR) {
                Senior senior = seniorGetService.byUser(user);
                userResponses.add(AdminMapper.mapToUserWithSeniorResponse(user, senior.getSeniorId()));
            }
            userResponses.add(AdminMapper.mapToUserResponse(user));
        });
        return userResponses;
    }
}
