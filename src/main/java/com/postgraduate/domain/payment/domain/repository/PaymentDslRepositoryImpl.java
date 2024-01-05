package com.postgraduate.domain.payment.domain.repository;

import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.postgraduate.domain.mentoring.domain.entity.QMentoring.mentoring;
import static com.postgraduate.domain.payment.domain.entity.QPayment.payment;
import static com.postgraduate.domain.user.domain.entity.QUser.user;
import static com.querydsl.core.types.dsl.Expressions.FALSE;

@Repository
@RequiredArgsConstructor
public class PaymentDslRepositoryImpl implements PaymentDslRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Payment> findAllBySeniorAndStatus(Senior senior, Boolean status) {
        return queryFactory.selectFrom(payment)
                .where(
                        payment.mentoring.senior.eq(senior),
                        payment.salary.status.eq(status)
                )
                .join(payment.mentoring, mentoring)
                .join(payment.mentoring.user, user)
                .fetchJoin()
                .orderBy(payment.salary.salaryDate.desc())
                .fetch();
    }

    @Override
    public Page<Payment> findAllBySearchPayment(String search, Pageable pageable) {
        List<Payment> payments = queryFactory.selectFrom(payment)
                .where(
                        searchLike(search),
                        payment.mentoring.user.isDelete.eq(FALSE)
                )
                .orderBy(payment.paidAt.desc())
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
