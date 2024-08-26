package com.postgraduate.domain.user.wish.domain.repository;

import com.postgraduate.domain.user.wish.domain.entity.Wish;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.postgraduate.domain.user.user.domain.entity.QUser.user;
import static com.postgraduate.domain.wish.domain.entity.QWish.wish;

@Repository
@RequiredArgsConstructor
public class WishDslRepositoryImpl implements WishDslRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Wish> findAllWish() {
        return queryFactory.selectFrom(wish)
                .distinct()
                .leftJoin(wish.user, user)
                .fetchJoin()
                .where(wish.user.isDelete.isFalse())
                .orderBy(wish.user.createdAt.desc())
                .fetch();
    }
}
