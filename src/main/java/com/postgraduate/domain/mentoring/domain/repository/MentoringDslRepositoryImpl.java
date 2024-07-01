package com.postgraduate.domain.mentoring.domain.repository;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.QUser;
import com.postgraduate.domain.user.domain.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.postgraduate.domain.mentoring.domain.entity.QMentoring.mentoring;
import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.EXPECTED;
import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.WAITING;
import static com.postgraduate.domain.payment.domain.entity.QPayment.payment;
import static com.postgraduate.domain.salary.domain.entity.QSalary.salary;
import static com.postgraduate.domain.senior.domain.entity.QSenior.senior;
import static com.postgraduate.domain.user.domain.entity.QUser.user;

@RequiredArgsConstructor
@Repository
public class MentoringDslRepositoryImpl implements MentoringDslRepository {
    private final JPAQueryFactory queryFactory;

    @Override
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

    @Override
    public List<Mentoring> findAllBySeniorAndStatus(Senior inputSenior, Status status) {
        return queryFactory.selectFrom(mentoring)
                .distinct()
                .leftJoin(mentoring.user, user)
                .fetchJoin()
                .where(mentoring.senior.eq(inputSenior), mentoring.status.eq(status))
                .orderBy(mentoring.createdAt.desc())
                .fetch();
    }

    @Override
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

    @Override
    public List<Mentoring> findAllByUserAndStatus(User inputUser, Status status) {
        return queryFactory.selectFrom(mentoring)
                .distinct()
                .leftJoin(mentoring.senior, senior)
                .fetchJoin()
                .leftJoin(mentoring.senior.user, user)
                .fetchJoin()
                .where(mentoring.user.eq(inputUser), mentoring.status.eq(status))
                .orderBy(mentoring.createdAt.desc())
                .fetch();
    }

    @Override
    public Optional<Mentoring> findByMentoringId(Long mentoringId) {
        return Optional.ofNullable(queryFactory.selectFrom(mentoring)
                .distinct()
                .leftJoin(mentoring.senior, senior)
                .fetchJoin()
                .leftJoin(mentoring.user, user)
                .fetchJoin()
                .where(mentoring.mentoringId.eq(mentoringId))
                .fetchOne());
    }

    @Override
    public List<Mentoring> findAllBySeniorAndSalaryStatus(Senior senior, Boolean status) {
        return queryFactory.selectFrom(mentoring)
                .distinct()
                .where(
                        mentoring.senior.eq(senior),
                        mentoring.status.eq(Status.DONE),
                        mentoring.salary.status.eq(status)
                )
                .leftJoin(mentoring.payment, payment)
                .fetchJoin()
                .leftJoin(mentoring.salary, salary)
                .fetchJoin()
                .leftJoin(mentoring.user, user)
                .fetchJoin()
                .orderBy(mentoring.salary.salaryDate.desc(), mentoring.updatedAt.desc())
                .fetch();
    }

    @Override
    public Optional<Mentoring> findByMentoringIdAndUserForDetails(Long mentoringId, User user) {
        return Optional.ofNullable(queryFactory.selectFrom(mentoring)
                .where(
                        mentoring.user.eq(user)
                                .and(mentoring.mentoringId.eq(mentoringId))
                                .and(
                                        mentoring.status.eq(WAITING)
                                                .or(mentoring.status.eq(EXPECTED))
                                )
                )
                .fetchOne()
        );
    }

    @Override
    public Optional<Mentoring> findByMentoringIdAndSeniorForDetails(Long mentoringId, Senior senior) {
        return Optional.ofNullable(queryFactory.selectFrom(mentoring)
                .where(
                        mentoring.senior.eq(senior)
                                .and(mentoring.mentoringId.eq(mentoringId))
                                .and(
                                        mentoring.status.eq(WAITING)
                                                .or(mentoring.status.eq(EXPECTED))
                                )
                )
                .fetchOne()
        );
    }

    @Override
    public List<Mentoring> findAllForMessage() {
        QUser user1 = new QUser("user1");
        QUser user2 = new QUser("user2");

        return queryFactory.selectFrom(mentoring)
                .distinct()
                .leftJoin(mentoring.user, user1)
                .fetchJoin()
                .leftJoin(mentoring.senior, senior)
                .fetchJoin()
                .leftJoin(mentoring.senior.user, user2)
                .fetchJoin()
                .where(mentoring.status.eq(EXPECTED))
                .fetch();
    }
}
