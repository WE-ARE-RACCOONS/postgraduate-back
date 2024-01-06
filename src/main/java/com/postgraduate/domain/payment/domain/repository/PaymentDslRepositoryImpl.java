package com.postgraduate.domain.payment.domain.repository;

import com.postgraduate.domain.payment.domain.entity.Payment;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.postgraduate.domain.payment.domain.entity.QPayment.payment;
import static com.querydsl.core.types.dsl.Expressions.FALSE;

@Repository
@RequiredArgsConstructor
public class PaymentDslRepositoryImpl implements PaymentDslRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Payment> findAllBySearchPayment(String search, Pageable pageable) {
        List<Payment> payments = queryFactory.selectFrom(payment)
                .where(
                        searchLike(search),
                        payment.mentoring.user.isDelete.eq(FALSE)
                )
                .orderBy(payment.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(payment.count())
                .distinct()
                .from(payment)
                .where(
                        searchLike(search),
                        payment.mentoring.user.isDelete.eq(FALSE)
                )
                .fetchOne();

        return new PageImpl<>(payments, pageable, total);
    }


    private BooleanExpression searchLike(String search) {
        if (StringUtils.hasText(search)) {
            return payment.mentoring.user.nickName.contains(search)
                    .or(payment.mentoring.user.phoneNumber.contains(search));
        }
        return null;
    }
}
