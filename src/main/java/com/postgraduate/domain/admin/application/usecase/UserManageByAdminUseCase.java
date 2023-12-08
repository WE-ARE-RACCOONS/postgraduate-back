package com.postgraduate.domain.admin.application.usecase;

import com.postgraduate.domain.admin.application.dto.res.UserResponse;
import com.postgraduate.domain.admin.application.mapper.AdminMapper;
import com.postgraduate.domain.wish.domain.entity.Wish;
import com.postgraduate.domain.wish.domain.service.WishGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserManageByAdminUseCase {
    private final WishGetService wishGetService;
    public List<UserResponse> getUsers() {
        List<Wish> wishes = wishGetService.all();

        return wishes.stream()
                .map(AdminMapper::mapToUserResponse)
                .toList();
    }
}
