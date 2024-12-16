package com.postgraduate.admin.domain.repository;

import com.postgraduate.domain.member.user.domain.entity.MemberRole;
import com.postgraduate.domain.member.user.domain.entity.User;
import com.postgraduate.domain.member.user.domain.entity.constant.Role;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.postgraduate.domain.member.user.domain.entity.QMemberRole.memberRole;
import static com.postgraduate.domain.member.user.domain.entity.QUser.user;

@RequiredArgsConstructor
@Repository
public class AdminUserRepository {
    private final JPAQueryFactory queryFactory;

    public Page<MemberRole> findAllJunior(Pageable pageable) {
        List<MemberRole> memberRoles = queryFactory.selectFrom(memberRole)
                .distinct()
                .where(memberRole.role.eq(Role.USER))
                .join(memberRole.user, user)
                .fetchJoin()
                .orderBy(user.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        if (CollectionUtils.isEmpty(memberRoles)) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        Long total = queryFactory.select(memberRole.count())
                .from(memberRole)
                .distinct()
                .where(memberRole.role.eq(Role.USER))
                .join(memberRole.user, user)
                .fetchOne();

        return new PageImpl<>(memberRoles, pageable, total);
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
