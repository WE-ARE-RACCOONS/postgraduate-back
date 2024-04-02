package com.postgraduate.domain.payment.domain.repository;

import com.postgraduate.domain.payment.domain.entity.Payment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.postgraduate.domain.payment.domain.entity.QPayment.payment;
import static com.postgraduate.domain.user.domain.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class PaymentDslRepositoryImpl implements PaymentDslRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Payment> findAllPayment() {
        return queryFactory.selectFrom(payment)
                .distinct()
                .leftJoin(payment.user, user)
                .fetchJoin()
                .fetch();
    }
}
