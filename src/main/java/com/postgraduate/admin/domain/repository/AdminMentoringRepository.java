package com.postgraduate.admin.domain.repository;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.postgraduate.domain.mentoring.domain.entity.QMentoring.mentoring;
import static com.postgraduate.domain.payment.domain.entity.QPayment.payment;
import static com.postgraduate.domain.senior.domain.entity.QSenior.senior;
import static com.postgraduate.domain.user.domain.entity.QUser.user;
import static java.util.Optional.ofNullable;

@Repository
@RequiredArgsConstructor
public class AdminMentoringRepository {
    private final JPAQueryFactory queryFactory;

    public List<Mentoring> findAllBySeniorId(Long seniorId) {
        return queryFactory.selectFrom(mentoring)
                .distinct()
                .leftJoin(mentoring.senior, senior)
                .fetchJoin()
                .leftJoin(mentoring.user, user)
                .fetchJoin()
                .where(mentoring.senior.seniorId.eq(seniorId))
                .orderBy(mentoring.createdAt.desc())
                .fetch();
    }

    public List<Mentoring> findAllByUserId(Long userId) {
        return queryFactory.selectFrom(mentoring)
                .distinct()
                .leftJoin(mentoring.senior, senior)
                .fetchJoin()
                .leftJoin(mentoring.senior.user, user)
                .fetchJoin()
                .where(mentoring.user.userId.eq(userId))
                .orderBy(mentoring.createdAt.desc())
                .fetch();
    }

    public Optional<Mentoring> findByMentoringId(Long mentoringId) {
        return Optional.ofNullable(queryFactory.selectFrom(mentoring)
                .distinct()
                .join(mentoring.payment, payment)
                .fetchJoin()
                .where(mentoring.mentoringId.eq(mentoringId))
                .fetchOne());
    }

    public Optional<Mentoring> findByPaymentId(Long paymentId) {
        return ofNullable(queryFactory.selectFrom(mentoring)
                .distinct()
                .join(mentoring.payment, payment)
                .fetchJoin()
                .where(mentoring.payment.paymentId.eq(paymentId))
                .fetchOne());
    }
}
