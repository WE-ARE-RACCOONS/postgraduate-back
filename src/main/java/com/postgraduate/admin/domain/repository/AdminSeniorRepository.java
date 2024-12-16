package com.postgraduate.admin.domain.repository;

import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.member.senior.domain.entity.Senior;
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
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Repository
public class AdminSeniorRepository {
    private final JPAQueryFactory queryFactory;

    public Page<Salary> allSeniorInfo(LocalDate salaryDate, Pageable pageable) {
        List<Salary> salaries = queryFactory.selectFrom(salary)
                .distinct()
                .join(salary.senior, senior)
                .fetchJoin()
                .join(salary.senior.user, user)
                .fetchJoin()
                .where(salary.senior.user.isDelete.isFalse(), salary.salaryDate.eq(salaryDate))
                .orderBy(salary.senior.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        if (CollectionUtils.isEmpty(salaries)) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        Long total = queryFactory.select(salary.count())
                .distinct()
                .join(salary.senior, senior)
                .join(salary.senior.user, user)
                .where(salary.senior.user.isDelete.isFalse(), salary.salaryDate.eq(salaryDate))
                .orderBy(salary.senior.createdAt.desc())
                .fetchOne();

        return new PageImpl<>(salaries, pageable, total);
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
