package com.postgraduate.admin.domain.repository;

import com.postgraduate.admin.application.dto.res.PaymentWithMentoringQuery;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.postgraduate.domain.mentoring.domain.entity.QMentoring.mentoring;
import static com.postgraduate.domain.payment.domain.entity.QPayment.payment;
import static com.postgraduate.domain.user.domain.entity.QUser.user;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Repository
public class AdminPaymentRepository {
    private final JPAQueryFactory queryFactory;

    public List<PaymentWithMentoringQuery> findAllPayment() {
        List<Tuple> fetch = queryFactory.select(payment, mentoring)
                .from(payment)
                .distinct()
            .leftJoin(payment.user, user)
                .fetchJoin()
                .leftJoin(mentoring)
                .on(mentoring.payment.eq(payment))
                .fetchJoin()
                .fetch();

        return fetch.stream()
                .map(tuple -> new PaymentWithMentoringQuery(tuple.get(payment), ofNullable(tuple.get(mentoring))))
                .toList();
    }
}
