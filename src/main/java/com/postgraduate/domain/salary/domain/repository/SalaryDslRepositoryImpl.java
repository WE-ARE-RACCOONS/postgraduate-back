package com.postgraduate.domain.salary.domain.repository;

import com.postgraduate.domain.payment.domain.entity.Payment;
import com.postgraduate.domain.salary.application.dto.SalaryDetails;
import com.postgraduate.domain.salary.application.dto.SeniorSalary;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.senior.domain.entity.QSenior;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.QUser;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

import static com.postgraduate.domain.account.domain.entity.QAccount.account;
import static com.postgraduate.domain.mentoring.domain.entity.QMentoring.mentoring;
import static com.postgraduate.domain.payment.domain.entity.QPayment.payment;
import static com.postgraduate.domain.salary.application.mapper.SalaryMapper.mapToSalaryDetail;
import static com.postgraduate.domain.salary.domain.entity.QSalary.salary;
import static com.postgraduate.domain.senior.domain.entity.QSenior.senior;
import static com.postgraduate.domain.user.domain.entity.QUser.user;
import static com.querydsl.core.types.Projections.constructor;
import static java.lang.Boolean.FALSE;

@Repository
@RequiredArgsConstructor
public class SalaryDslRepositoryImpl implements SalaryDslRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<SeniorSalary> findDistinctBySearchSenior(String search, Pageable pageable) {
        List<SeniorSalary> seniorSalaries = queryFactory
                .select(
                        constructor(
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
                .groupBy(salary.senior.seniorId, salary.salaryDate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(salary.count())
                .from(salary)
                .where(
                        searchLike(search),
                        salary.senior.user.isDelete.eq(FALSE)
                )
                .fetchOne();

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


    @Override
    public List<Salary> findAllSalary() {
        return queryFactory.selectFrom(salary)
                .distinct()
                .join(salary.senior, senior)
                .fetchJoin()
                .join(salary.senior.user, user)
                .fetchJoin()
                .fetch();
    }
}

