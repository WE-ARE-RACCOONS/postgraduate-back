package com.postgraduate.domain.salary.domain.repository;

import com.postgraduate.domain.salary.application.dto.SeniorSalary;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

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
                        searchLike(search),
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

    private BooleanExpression searchLike(String search) {
        JPAQuery<Long> accountQuery = queryFactory.select(account.senior.seniorId)
                .from(account)
                .where(searchAccount(search));

        if (StringUtils.hasText(search)) {
            return salary.senior.user.nickName.contains(search)
                    .or(salary.senior.user.phoneNumber.contains(search))
                    .or(salary.senior.seniorId.in(accountQuery));
        }
        return null;
    }

    private BooleanExpression searchAccount(String search) {
        if (StringUtils.hasText(search)) {
            return account.accountHolder.contains(search);
        }
        return null;
    }
}

