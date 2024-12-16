package com.postgraduate.admin.domain.repository;

import com.postgraduate.admin.application.dto.res.PaymentWithMentoringQuery;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static com.postgraduate.domain.member.user.domain.entity.QUser.user;
import static com.postgraduate.domain.mentoring.domain.entity.QMentoring.mentoring;
import static com.postgraduate.domain.payment.domain.entity.QPayment.payment;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Repository
public class AdminPaymentRepository {
    private final JPAQueryFactory queryFactory;

    public Page<PaymentWithMentoringQuery> findAllPayment(Pageable pageable) {
        List<Tuple> fetch = queryFactory.select(payment, mentoring)
                .from(payment)
                .distinct()
                .leftJoin(payment.user, user)
                .fetchJoin()
                .leftJoin(mentoring)
                .on(mentoring.payment.eq(payment))
                .fetchJoin()
                .orderBy(payment.paidAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<PaymentWithMentoringQuery> results = fetch.stream()
                .map(tuple -> new PaymentWithMentoringQuery(tuple.get(payment), ofNullable(tuple.get(mentoring))))
                .toList();

        if (CollectionUtils.isEmpty(results)) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        Long total = queryFactory.select(payment.count())
                .from(payment)
                .distinct()
                .leftJoin(payment.user, user)
                .leftJoin(mentoring)
                .on(mentoring.payment.eq(payment))
                .fetchOne();

        return new PageImpl<>(results, pageable, total);
    }
}
