package com.postgraduate.domain.senior.domain.repository;

import com.postgraduate.domain.senior.domain.entity.Senior;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

import static com.postgraduate.domain.account.domain.entity.QAccount.account;
import static com.postgraduate.domain.senior.domain.entity.QSenior.senior;
import static com.postgraduate.domain.senior.domain.entity.constant.Status.APPROVE;
import static com.querydsl.core.types.Order.ASC;
import static com.querydsl.core.types.Order.DESC;
import static com.querydsl.core.types.dsl.Expressions.FALSE;
import static com.querydsl.core.types.dsl.Expressions.TRUE;

@RequiredArgsConstructor
@Repository
public class SeniorDslRepositoryImpl implements SeniorDslRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Senior> findAllBySearchSenior(String search, String sort, Pageable pageable) {
        JPAQuery<Senior> query = queryFactory.selectFrom(senior)
                .where(
                        senior.info.totalInfo.like("%" + search + "%"),
                        senior.status.eq(APPROVE),
                        senior.user.isDelete.eq(FALSE)
                )
                .orderBy(orderSpecifier(sort))
                .orderBy(senior.user.nickName.asc());

        List<Senior> seniors = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = query.fetchCount();

        return new PageImpl<>(seniors, pageable, total);
    }

    private OrderSpecifier<?> orderSpecifier(String sort) {
        if (sort == null)
            return new OrderSpecifier<>(DESC, senior.hit);
        if (sort.equals("low"))
            return new OrderSpecifier<>(ASC, senior.hit);
        return new OrderSpecifier<>(DESC, senior.hit);
    }

    @Override
    public Page<Senior> findAllByFieldSenior(String field, String postgradu, Pageable pageable) {
        JPAQuery<Senior> query = queryFactory.selectFrom(senior)
                .where(
                        fieldSpecifier(field),
                        postgraduSpecifier(postgradu),
                        senior.status.eq(APPROVE),
                        senior.user.isDelete.eq(FALSE)
                )
                .orderBy(senior.hit.desc())
                .orderBy(senior.user.nickName.asc());

        List<Senior> seniors = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = query.fetchCount();

        return new PageImpl<>(seniors, pageable, total);
    }

    private BooleanExpression fieldSpecifier(String field) {
        if (field.equals("others"))
            return senior.info.etcField.isTrue();
        return senior.info.field.like("%"+field+"%");
    }

    private BooleanExpression postgraduSpecifier(String postgradu) {
        if (postgradu.contains("all"))
            return TRUE;

        String[] postgradus = postgradu.split(",");

        if (postgradu.contains("others")) {
            return Arrays.stream(postgradus)
                    .map(postgraduName -> senior.info.etcPostgradu.isTrue()
                            .or(senior.info.postgradu.like("%"+postgraduName+"%")))
                    .reduce(BooleanExpression::or)
                    .orElse(FALSE);
        }

        return Arrays.stream(postgradus)
                .map(postgraduName -> senior.info.postgradu.like("%"+postgraduName+"%"))
                .reduce(BooleanExpression::or)
                .orElse(FALSE);
    }

    @Override
    public Page<Senior> findAllBySearchSenior(String search, Pageable pageable) {
        JPAQuery<Senior> query = queryFactory.selectFrom(senior)
                .where(
                        searchLike(search),
                        senior.user.isDelete.eq(FALSE)
                )
                .orderBy(senior.createdAt.desc());

        List<Senior> seniors = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = query.fetchCount();

        return new PageImpl<>(seniors, pageable, total);
    }

    private BooleanExpression searchLike(String search) {
        if (StringUtils.hasText(search)) {
            return senior.user.phoneNumber.contains(search)
                    .or(senior.user.nickName.contains(search));
        }
        return null;
    }
}
