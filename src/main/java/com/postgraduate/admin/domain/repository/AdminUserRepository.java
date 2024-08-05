package com.postgraduate.admin.domain.repository;

import com.postgraduate.domain.wish.domain.entity.Wish;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.postgraduate.domain.user.user.domain.entity.QUser.user;
import static com.postgraduate.domain.wish.domain.entity.QWish.wish;

@RequiredArgsConstructor
@Repository
public class AdminUserRepository {
    private final JPAQueryFactory queryFactory;

    public List<Wish> findAllJunior() {
        return queryFactory.selectFrom(wish)
                .join(wish.user, user)
                .fetchJoin()
                .fetch();
    }

    public Optional<Wish> findByUserId(Long userId) {
        return Optional.ofNullable(queryFactory.selectFrom(wish)
                .where(
                        wish.matchingReceive.isTrue(),
                        wish.user.userId.eq(userId),
                        wish.user.isDelete.isFalse()
                )
                .join(wish.user, user)
                .fetchJoin()
                .fetchOne());
    }

    public Optional<Wish> findByWishId(Long wishId) {
        return Optional.ofNullable(queryFactory.selectFrom(wish)
                .where(wish.wishId.eq(wishId))
                .fetchOne());
    }
}
