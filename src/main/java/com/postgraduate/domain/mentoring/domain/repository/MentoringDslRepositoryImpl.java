package com.postgraduate.domain.mentoring.domain.repository;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.postgraduate.domain.mentoring.domain.entity.QMentoring.mentoring;
import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.EXPECTED;
import static com.postgraduate.domain.mentoring.domain.entity.constant.Status.WAITING;
import static com.postgraduate.domain.payment.domain.entity.QPayment.payment;
import static com.postgraduate.domain.salary.domain.entity.QSalary.salary;
import static com.postgraduate.domain.senior.domain.entity.QSenior.senior;
import static com.postgraduate.domain.user.domain.entity.QUser.user;
import static com.querydsl.core.types.dsl.Expressions.FALSE;

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
                .join(mentoring.user, user)
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
                .where(
                        mentoring.senior.eq(senior),
                        mentoring.status.eq(Status.DONE),
                        mentoring.salary.status.eq(status)
                )
                .join(mentoring.salary, salary)
                .fetchJoin()
                .join(mentoring.user, user)
                .fetchJoin()
                .orderBy(mentoring.salary.salaryDate.desc(), mentoring.updatedAt.desc())
                .fetch();
    }

    @Override
    public Page<Mentoring> findAllBySearchPayment(String search, Pageable pageable) {
        List<Mentoring> mentorings = queryFactory.selectFrom(mentoring)
                .where(
                        searchLike(search),
                        mentoring.user.isDelete.eq(FALSE)
                )
                .join(mentoring.user, user)
                .fetchJoin()
                .join(mentoring.payment, payment)
                .fetchJoin()
                .orderBy(mentoring.payment.paidAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(mentoring.count())
                .distinct()
                .from(mentoring)
                .where(
                        searchLike(search),
                        mentoring.user.isDelete.eq(FALSE)
                )
                .fetchOne();

        return new PageImpl<>(mentorings, pageable, total);
    }


    private BooleanExpression searchLike(String search) {
        if (StringUtils.hasText(search)) {
            return mentoring.user.nickName.contains(search)
                    .or(mentoring.user.phoneNumber.contains(search));
        }
        return null;
    }

    @Override
    public List<Mentoring> findAllByStatusAndCreatedAtIsBefore(Status status, LocalDateTime now) {
        return queryFactory.selectFrom(mentoring)
                .distinct()
                .join(mentoring.user, user)
                .fetchJoin()
                .join(mentoring.payment, payment)
                .fetchJoin()
                .where(mentoring.status.eq(status), mentoring.createdAt.before(now))
                .fetch();
    }

    @Override
    public List<Mentoring> findAllByStatus(Status status) {
        return queryFactory.selectFrom(mentoring)
                .distinct()
                .join(mentoring.senior, senior)
                .fetchJoin()
                .join(mentoring.salary, salary)
                .fetchJoin()
                .where(mentoring.status.eq(status))
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
}
