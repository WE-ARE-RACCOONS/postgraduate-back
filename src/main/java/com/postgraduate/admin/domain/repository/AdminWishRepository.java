package com.postgraduate.admin.domain.repository;

import com.postgraduate.domain.wish.domain.entity.constant.Status;
import com.postgraduate.domain.wish.domain.entity.Wish;
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

import static com.postgraduate.domain.wish.domain.entity.QWish.wish;

@RequiredArgsConstructor
@Repository
public class AdminWishRepository {
    private final JPAQueryFactory queryFactory;

    public Page<Wish> findAllWaitingWish(Pageable pageable) {
        List<Wish> wishes = queryFactory.selectFrom(wish)
                .where(wish.status.eq(Status.WAITING))
                .orderBy(wish.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        if (CollectionUtils.isEmpty(wishes)) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        Long total = queryFactory.select(wish.count())
                .from(wish)
                .where(wish.status.eq(Status.WAITING))
                .fetchOne();

        return new PageImpl<>(wishes, pageable, total);
    }

    public Page<Wish> findAllMatchingWish(Pageable pageable) {
        List<Wish> wishes = queryFactory.selectFrom(wish)
                .where(wish.status.eq(Status.MATCHED))
                .orderBy(wish.updatedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        if (CollectionUtils.isEmpty(wishes)) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        Long total = queryFactory.select(wish.count())
                .from(wish)
                .where(wish.status.eq(Status.MATCHED))
                .fetchOne();

        return new PageImpl<>(wishes, pageable, total);
    }

    public Optional<Wish> findByWishId(Long wishId) {
        return Optional.ofNullable(queryFactory.selectFrom(wish)
                .where(wish.wishId.eq(wishId))
                .fetchOne());
    }
}
