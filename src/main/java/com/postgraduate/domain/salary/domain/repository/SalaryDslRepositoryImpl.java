package com.postgraduate.domain.salary.domain.repository;

import com.postgraduate.domain.salary.application.dto.SeniorSalary;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.postgraduate.domain.account.domain.entity.QAccount.account;
import static com.postgraduate.domain.salary.domain.entity.QSalary.salary;
import static java.lang.Boolean.FALSE;

@Repository
@RequiredArgsConstructor
public class SalaryDslRepositoryImpl implements SalaryDslRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<SeniorSalary> findDistinctBySearchSenior(String search, Pageable pageable) {
        JPAQuery<Long> accountQuery = queryFactory.select(account.senior.seniorId)
                .from(account)
                .where(account.accountHolder.like("%" + search + "%"));

        JPAQuery<SeniorSalary> query = queryFactory
                .select(
                        Projections.constructor(
                                SeniorSalary.class,
                                salary.senior,
                                salary.salaryDate
                        )
                )
                .from(salary)
                .where(
                        salary.senior.user.nickName.like("%" + search + "%")
                                .or(salary.senior.user.phoneNumber.like("%" + search + "%"))
                                .or(salary.senior.seniorId.in(accountQuery)),
                        salary.senior.user.isDelete.eq(FALSE)
                )
                .orderBy(salary.salaryDate.desc())
                .groupBy(salary.senior.seniorId, salary.salaryDate);


        List<SeniorSalary> seniorSalaries = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = query.fetchCount();

        return new PageImpl<>(seniorSalaries, pageable, total);
    }
}

