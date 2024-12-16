package com.postgraduate.admin.domain.repository;

import com.postgraduate.domain.wish.domain.entity.constant.Status;
import com.postgraduate.domain.wish.domain.entity.Wish;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.postgraduate.domain.wish.domain.entity.QWish.wish;

@RequiredArgsConstructor
@Repository
public class AdminWishRepository {
    private final JPAQueryFactory queryFactory;

    public List<Wish> findAllWaitingWish() {
        return queryFactory.selectFrom(wish)
                .where(wish.status.eq(Status.WAITING))
                .fetch();
    }

    public List<Wish> findAllMatchingWish() {
        return queryFactory.selectFrom(wish)
                .where(wish.status.eq(Status.MATCHED))
                .fetch();
    }

    public Optional<Wish> findByWishId(Long wishId) {
        return Optional.ofNullable(queryFactory.selectFrom(wish)
                .where(wish.wishId.eq(wishId))
                .fetchOne());
    }
}
