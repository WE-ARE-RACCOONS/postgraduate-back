package com.postgraduate.admin.domain.repository;

import com.postgraduate.domain.salary.domain.entity.Salary;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.postgraduate.domain.member.senior.domain.entity.QSenior.senior;
import static com.postgraduate.domain.member.user.domain.entity.QUser.user;
import static com.postgraduate.domain.salary.domain.entity.QSalary.salary;

@RequiredArgsConstructor
@Repository
public class AdminSalaryRepository {
    private final JPAQueryFactory queryFactory;

    public List<Salary> findAllByDone() {
        return queryFactory.selectFrom(salary)
                .distinct()
                .where(
                        salary.status.isTrue(),
                        salary.totalAmount.gt(0)
                )
                .leftJoin(salary.senior, senior)
                .fetchJoin()
                .leftJoin(senior.user, user)
                .fetchJoin()
                .orderBy(salary.salaryDoneDate.desc())
                .fetch();
    }

    public Optional<Salary> findBySeniorId(Long seniorId, LocalDate salaryDate) {
        return Optional.ofNullable(queryFactory.selectFrom(salary)
                .distinct()
                .where(
                        salary.salaryDate.eq(salaryDate),
                        salary.senior.seniorId.eq(seniorId),
                        salary.senior.user.isDelete.isFalse()
                )
                .join(salary.senior, senior)
                .fetchJoin()
                .join(salary.senior.user, user)
                .fetchJoin()
                .fetchOne());
    }

    public Optional<Salary> findBySalaryId(Long salaryId) {
        return Optional.ofNullable(queryFactory.selectFrom(salary)
                .where(salary.salaryId.eq(salaryId))
                .fetchOne());
    }

    public List<Salary> findAllByNotDoneFromLast(LocalDate salaryDate) {
        return queryFactory.selectFrom(salary)
                .distinct()
                .where(
                        salary.status.isFalse(),
                        salary.salaryDate.lt(salaryDate),
                        salary.totalAmount.gt(0),
                        salary.senior.user.isDelete.isFalse()
                )
                .leftJoin(salary.senior, senior)
                .fetchJoin()
                .leftJoin(senior.user, user)
                .fetchJoin()
                .orderBy(salary.salaryDate.asc())
                .fetch();
    }
}
