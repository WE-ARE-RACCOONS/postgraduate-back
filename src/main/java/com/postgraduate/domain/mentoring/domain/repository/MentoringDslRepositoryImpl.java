package com.postgraduate.domain.mentoring.domain.repository;

import com.postgraduate.domain.mentoring.domain.entity.Mentoring;
import com.postgraduate.domain.mentoring.domain.entity.constant.Status;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.postgraduate.domain.mentoring.domain.entity.QMentoring.mentoring;
import static com.postgraduate.domain.senior.domain.entity.QSenior.senior;
import static com.postgraduate.domain.user.domain.entity.QUser.user;

@RequiredArgsConstructor
@Repository
public class MentoringDslRepositoryImpl implements MentoringDslRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Mentoring> findAllBySenior(Long seniorId) {
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
                .leftJoin(mentoring.senior, senior)
                .fetchJoin()
                .leftJoin(mentoring.user, user)
                .fetchJoin()
                .where(mentoring.senior.eq(inputSenior), mentoring.status.eq(status))
                .fetch();
    }

    @Override
    public List<Mentoring> findAllByStatus(Status status) {
        return queryFactory.selectFrom(mentoring)
                .distinct()
                .leftJoin(mentoring.senior, senior)
                .fetchJoin()
                .leftJoin(mentoring.user, user)
                .fetchJoin()
                .where(mentoring.status.eq(status))
                .fetch();
    }
}
