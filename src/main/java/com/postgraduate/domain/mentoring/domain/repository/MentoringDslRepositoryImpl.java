package com.postgraduate.domain.mentoring.domain.repository;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.Refuse;
import com.postgraduate.domain.mentoring.domain.entity.constant.MentoringStatus;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.user.domain.entity.QUser;
import com.postgraduate.domain.user.user.domain.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.postgraduate.domain.mentoring.domain.entity.QMentoring.mentoring;
import static com.postgraduate.domain.mentoring.domain.entity.constant.MentoringStatus.EXPECTED;
import static com.postgraduate.domain.mentoring.domain.entity.constant.MentoringStatus.WAITING;
import static com.postgraduate.domain.payment.domain.entity.QPayment.payment;
import static com.postgraduate.domain.salary.domain.entity.QSalary.salary;
import static com.postgraduate.domain.senior.domain.entity.QSenior.senior;
import static com.postgraduate.domain.user.user.domain.entity.QUser.user;

@RequiredArgsConstructor
@Repository
public class MentoringDslRepositoryImpl implements MentoringDslRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    @Override
    public List<Mentoring> findAllBySeniorAndStatus(Senior inputSenior, MentoringStatus mentoringStatus) {
        return queryFactory.selectFrom(mentoring)
                .distinct()
                .leftJoin(mentoring.user, user)
                .fetchJoin()
                .where(mentoring.senior.eq(inputSenior), mentoring.status.eq(mentoringStatus))
                .orderBy(mentoring.createdAt.desc())
                .fetch();
    }

    @Override
    public List<Mentoring> findAllByUserAndStatus(User inputUser, MentoringStatus mentoringStatus) {
        return queryFactory.selectFrom(mentoring)
                .distinct()
                .leftJoin(mentoring.senior, senior)
                .fetchJoin()
                .leftJoin(mentoring.senior.user, user)
                .fetchJoin()
                .where(mentoring.user.eq(inputUser), mentoring.status.eq(mentoringStatus))
                .orderBy(mentoring.createdAt.desc())
                .fetch();
    }

    @Override
    public List<Mentoring> findAllBySeniorAndSalaryStatus(Senior senior, Boolean status) {
        return queryFactory.selectFrom(mentoring)
                .distinct()
                .where(
                        mentoring.senior.eq(senior),
                        mentoring.status.eq(MentoringStatus.DONE),
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

    @Override
    public boolean existSeniorMentoring(Senior senior) {
        Integer fetchFirst = queryFactory.selectOne()
                .from(mentoring)
                .where(mentoring.senior.eq(senior)
                        .and(
                                mentoring.status.eq(WAITING).or(mentoring.status.eq(EXPECTED))
                        ))
                .fetchFirst();
        return fetchFirst != null;
    }

    @Override
    public boolean existUserMentoring(User user) {
        Integer fetchFirst = queryFactory.selectOne()
                .from(mentoring)
                .where(mentoring.user.eq(user)
                        .and(
                                mentoring.status.eq(WAITING).or(mentoring.status.eq(EXPECTED))
                        ))
                .fetchFirst();
        return fetchFirst != null;
    }

    @Override
    public void saveRefuse(Refuse refuse) {
        entityManager.persist(refuse);
    }
}
