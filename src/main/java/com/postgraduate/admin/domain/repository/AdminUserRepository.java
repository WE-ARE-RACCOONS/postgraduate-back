package com.postgraduate.admin.domain.repository;

import com.postgraduate.domain.member.user.domain.entity.User;
import com.postgraduate.domain.member.user.domain.entity.constant.Role;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.postgraduate.domain.member.user.domain.entity.QUser.user;

@RequiredArgsConstructor
@Repository
public class AdminUserRepository {
    private final JPAQueryFactory queryFactory;

    public List<User> findAllJunior() {
        return queryFactory.selectFrom(user)
                .where(user.role.eq(Role.USER))
                .fetch();
    }

    public Optional<User> findUserByUserId(Long userId) {
        return Optional.ofNullable(queryFactory.selectFrom(user)
                .where(user.userId.eq(userId))
                .fetchOne());
    }

    public Optional<User> login(String id, String pw) {
        return Optional.ofNullable(queryFactory.selectFrom(user)
                .where(user.nickName.eq(id),
                        user.phoneNumber.eq(pw))
                .fetchOne());
    }
}
