package com.postgraduate.domain.senior.domain.repository;

import com.postgraduate.domain.senior.domain.entity.Senior;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.postgraduate.domain.senior.domain.entity.QSenior.senior;
import static com.postgraduate.domain.senior.domain.entity.constant.Status.APPROVE;

@RequiredArgsConstructor
@Repository
public class SeniorDslRepositoryImpl implements SeniorDslRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Senior> findAllSenior(String search, String sort, Pageable pageable) {
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
            return new OrderSpecifier<>(Order.ASC, senior.hit);
        return switch (sort) {
            case "high" -> new OrderSpecifier<>(Order.DESC, senior.hit);
            default -> new OrderSpecifier<>(Order.ASC, senior.hit);
        };
    }

}
