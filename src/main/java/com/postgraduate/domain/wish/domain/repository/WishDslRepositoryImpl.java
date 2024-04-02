package com.postgraduate.domain.wish.domain.repository;

import com.postgraduate.domain.wish.domain.entity.Wish;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.postgraduate.domain.user.domain.entity.QUser.user;
import static com.postgraduate.domain.wish.domain.entity.QWish.wish;
import static com.querydsl.core.types.dsl.Expressions.FALSE;

@Repository
@RequiredArgsConstructor
public class WishDslRepositoryImpl implements WishDslRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Wish> findAllBySearchWish(String search, Pageable pageable) {
        List<Wish> wishes = queryFactory.selectFrom(wish)
                .distinct()
                .leftJoin(wish.user, user)
                .fetchJoin()
                .where(
                        searchLike(search),
                        wish.user.isDelete.eq(FALSE)
                )
                .orderBy(wish.user.createdAt.desc()).offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(wish.count())
                .from(wish)
                .where(
                        searchLike(search),
                        wish.user.isDelete.eq(FALSE)
                )
                .fetchOne();

        return new PageImpl<>(wishes, pageable, total);
    }

    private BooleanExpression searchLike(String search) {
        if (StringUtils.hasText(search)) {
            return wish.user.phoneNumber.contains(search)
                    .or(wish.user.nickName.contains(search));
        }
        return null;
    }

    @Override
    public List<Wish> findAllWish() {
        return queryFactory.selectFrom(wish)
                .distinct()
                .leftJoin(wish.user, user)
                .fetchJoin()
                .where(wish.user.isDelete.isFalse())
                .fetch();
    }
}
