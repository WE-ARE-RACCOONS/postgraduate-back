package com.postgraduate.domain.payment.domain.repository;

import com.postgraduate.domain.payment.domain.entity.Payment;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.postgraduate.domain.payment.domain.entity.QPayment.payment;
import static com.querydsl.core.types.dsl.Expressions.FALSE;

@Repository
@RequiredArgsConstructor
public class PaymentDslRepositoryImpl implements PaymentDslRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Payment> findAllBySearchPayment(String search, Pageable pageable) {
        JPAQuery<Payment> query = queryFactory.selectFrom(payment)
                .where(
                        payment.mentoring.user.nickName.like("%" + search + "%")
                                .or(payment.mentoring.user.phoneNumber.like("%" + search + "%"))
                        , payment.mentoring.user.isDelete.eq(FALSE)
                        );

        List<Payment> seniors = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = query.fetchCount();

        return new PageImpl<>(seniors, pageable, total);
    }
}
