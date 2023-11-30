package com.postgraduate.domain.admin.application.usecase;

import com.postgraduate.domain.admin.application.dto.res.UserResponse;
import com.postgraduate.domain.admin.application.mapper.AdminMapper;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.service.UserGetService;
import com.postgraduate.domain.wish.domain.entity.Wish;
import com.postgraduate.domain.wish.domain.repository.WishRepository;
import com.postgraduate.domain.wish.exception.WishNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.postgraduate.domain.user.domain.entity.constant.Role.ADMIN;

@Service
@Transactional
@RequiredArgsConstructor
public class UserManageByAdminUseCase {
    private final UserGetService userGetService;
    private final WishRepository wishRepository;
    public List<UserResponse> getUsers() {
        List<User> users = userGetService.all();
        return users.stream()
                .filter(user -> user.getRole() != ADMIN)
                .map(user -> {
                    Wish wish = wishRepository.findByUser(user).orElseThrow(WishNotFoundException::new);
                    return AdminMapper.mapToUserResponse(user, wish);
                })
                .toList();
    }
}
