package com.postgraduate.domain.member.user.domain.repository;

import com.postgraduate.domain.member.user.domain.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.postgraduate.domain.member.user.domain.entity.QWish.wish;

@Repository
@RequiredArgsConstructor
public class UserDslRepositoryImpl implements UserDslRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public void deleteWish(User user) {
        queryFactory.delete(wish)
                .where(wish.user.eq(user));
    }
}
