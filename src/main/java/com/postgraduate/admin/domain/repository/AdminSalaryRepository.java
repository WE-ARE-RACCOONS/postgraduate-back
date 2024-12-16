package com.postgraduate.admin.domain.repository;

import com.postgraduate.domain.salary.domain.entity.Salary;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.postgraduate.domain.member.senior.domain.entity.QSenior.senior;
import static com.postgraduate.domain.member.user.domain.entity.QUser.user;
import static com.postgraduate.domain.salary.domain.entity.QSalary.salary;

@RequiredArgsConstructor
@Repository
public class AdminSalaryRepository {
    private final JPAQueryFactory queryFactory;

    public Page<Salary> findAllByDone(Pageable pageable) {
        List<Salary> salaries = queryFactory.selectFrom(salary)
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
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        if (CollectionUtils.isEmpty(salaries)) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        Long total = queryFactory.select(salary.count())
                .from(salary)
                .distinct()
                .where(
                        salary.status.isTrue(),
                        salary.totalAmount.gt(0)
                )
                .leftJoin(salary.senior, senior)
                .leftJoin(senior.user, user)
                .fetchOne();

        return new PageImpl<>(salaries, pageable, total);
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

    public Page<Salary> findAllByNotDoneFromLast(LocalDate salaryDate, Pageable pageable) {
        List<Salary> salaries = queryFactory.selectFrom(salary)
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
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        if (CollectionUtils.isEmpty(salaries)) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        Long total = queryFactory.select(salary.count())
                .from(salary)
                .distinct()
                .where(
                        salary.status.isFalse(),
                        salary.salaryDate.lt(salaryDate),
                        salary.totalAmount.gt(0),
                        salary.senior.user.isDelete.isFalse()
                )
                .leftJoin(salary.senior, senior)
                .leftJoin(senior.user, user)
                .fetchOne();

        return new PageImpl<>(salaries, pageable, total);
    }
}
