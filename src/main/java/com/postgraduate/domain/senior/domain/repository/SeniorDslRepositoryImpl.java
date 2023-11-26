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

import java.util.Arrays;
import java.util.List;

import static com.postgraduate.domain.senior.domain.entity.QSenior.senior;
import static com.postgraduate.domain.senior.domain.entity.constant.Status.APPROVE;
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
                        senior.status.eq(APPROVE)
                )
                .orderBy(orderSpecifier(sort));

        List<Senior> seniors = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = query.fetchCount();

        return new PageImpl<>(seniors, pageable, total);
    }

    private OrderSpecifier<?> orderSpecifier(String sort) {
        if (sort == null)
            return new OrderSpecifier<>(Order.DESC, senior.hit);
        return switch (sort) {
            case "low" -> new OrderSpecifier<>(Order.ASC, senior.hit);
            default -> new OrderSpecifier<>(Order.DESC, senior.hit);
        };
    }

    @Override
    public Page<Senior> findAllByFieldSenior(String field, String postgradu, Pageable pageable) {
        String[] fields = field.split(",");
        String[] postgradus = postgradu.split(",");

        JPAQuery<Senior> query = queryFactory.selectFrom(senior)
                .where(
                        fieldSpecifier(fields),
                        postgraduSpecifier(postgradus),
                        senior.status.eq(APPROVE)
                )
                .orderBy(senior.hit.desc());

        List<Senior> seniors = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = query.fetchCount();

        return new PageImpl<>(seniors, pageable, total);
    }

    private BooleanExpression fieldSpecifier(String[] fields) {
        return Arrays.stream(fields)
                .map(field -> senior.info.field.like("%" + field + "%"))
                .reduce(BooleanExpression::or)
                .orElse(TRUE);
    }

    private BooleanExpression postgraduSpecifier(String[] postgrauds) {
        if (postgrauds[0].equals("전체")) {
            return TRUE;
        }
        return Arrays.stream(postgrauds)
                .map(postgradu -> senior.info.postgradu.like("%" + postgradu + "%"))
                .reduce(BooleanExpression::or)
                .orElse(TRUE);
    }
}
