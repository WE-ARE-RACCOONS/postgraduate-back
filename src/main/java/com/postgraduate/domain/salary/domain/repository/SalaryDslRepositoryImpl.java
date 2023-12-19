package com.postgraduate.domain.salary.domain.repository;

import com.postgraduate.domain.senior.domain.entity.Senior;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.postgraduate.domain.account.domain.entity.QAccount.account;
import static com.postgraduate.domain.salary.domain.entity.QSalary.salary;
import static com.postgraduate.domain.senior.domain.entity.QSenior.senior;
import static com.querydsl.core.types.dsl.Expressions.FALSE;

@Repository
@RequiredArgsConstructor
public class SalaryDslRepositoryImpl implements SalaryDslRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Senior> findDistinctBySearchSenior(LocalDate salaryDate, String search, Pageable pageable) {
        JPAQuery<Long> accountQuery = queryFactory.select(account.senior.seniorId)
                .from(account)
                .where(account.accountHolder.like("%" + search + "%"));

        JPAQuery<Senior> query = queryFactory.selectFrom(salary)
                .join(salary.senior, senior)
                .where(
                        salary.senior.user.nickName.like("%" + search + "%")
                                .or(salary.senior.user.phoneNumber.like("%" + search + "%"))
                                .or(salary.senior.seniorId.in(accountQuery)),
                        salary.senior.user.isDelete.eq(FALSE),
                        salary.salaryDate.eq(salaryDate)
                )
                .distinct()
                .limit(pageable.getPageSize())
                .select(salary.senior);

        List<Senior> wishes = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = query.fetchCount();

        return new PageImpl<>(wishes, pageable, total);
    }
}
