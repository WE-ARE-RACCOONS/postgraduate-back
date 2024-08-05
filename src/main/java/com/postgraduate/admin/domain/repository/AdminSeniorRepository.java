package com.postgraduate.admin.domain.repository;

import com.postgraduate.admin.application.dto.res.SeniorInfoQuery;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.postgraduate.domain.salary.domain.entity.QSalary.salary;
import static com.postgraduate.domain.senior.domain.entity.QSenior.senior;
import static com.postgraduate.domain.user.user.domain.entity.QUser.user;
import static com.postgraduate.domain.wish.domain.entity.QWish.wish;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Repository
public class AdminSeniorRepository {
    private final JPAQueryFactory queryFactory;

    public List<SeniorInfoQuery> allSeniorInfo(LocalDate salaryDate) {
        return queryFactory.select(salary, wish)
                .from(salary)
                .distinct()
                .join(salary.senior, senior)
                .fetchJoin()
                .join(salary.senior.user, user)
                .fetchJoin()
                .leftJoin(wish)
                .on(wish.user.eq(salary.senior.user))
                .fetchJoin()
                .where(salary.senior.user.isDelete.isFalse(), salary.salaryDate.eq(salaryDate))
                .orderBy(salary.senior.createdAt.desc())
                .fetch()
                .stream()
                .map(tuple -> new SeniorInfoQuery(tuple.get(salary), ofNullable(tuple.get(wish))))
                .toList();
    }

    public Optional<Senior> findBySeniorId(Long seniorId) {
        return ofNullable(queryFactory.selectFrom(senior)
                .distinct()
                .leftJoin(senior.user, user)
                .fetchJoin()
                .where(
                        senior.seniorId.eq(seniorId),
                        senior.user.isDelete.isFalse()
                )
                .fetchOne());
    }
}
