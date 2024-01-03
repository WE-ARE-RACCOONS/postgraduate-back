package com.postgraduate.domain.mentoring.domain.repository;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.postgraduate.domain.mentoring.domain.entity.QMentoring.mentoring;
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
                .fetch();
    }

    @Override
    public List<Mentoring> findAllBySeniorAndStatus(Senior inputSenior, Status status) {
        return queryFactory.selectFrom(mentoring)
                .distinct()
                .leftJoin(mentoring.user, user)
                .fetchJoin()
                .where(mentoring.senior.eq(inputSenior), mentoring.status.eq(status))
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
                .where(mentoring.mentoringId.eq(mentoringId), mentoring.user.isDelete.isFalse(), mentoring.senior.user.isDelete.isFalse())
                .fetchOne());
    }
}
