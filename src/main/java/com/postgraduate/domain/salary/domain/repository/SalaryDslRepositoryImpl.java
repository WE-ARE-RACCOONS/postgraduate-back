package com.postgraduate.domain.salary.domain.repository;

import com.postgraduate.domain.member.user.domain.entity.QUser;
import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.member.senior.domain.entity.Senior;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.postgraduate.domain.member.senior.domain.entity.QSenior.senior;
import static com.postgraduate.domain.member.user.domain.entity.QUser.user;
import static com.postgraduate.domain.salary.domain.entity.QSalary.salary;

@Repository
@RequiredArgsConstructor
public class SalaryDslRepositoryImpl implements SalaryDslRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Salary> findAllLastSalary(LocalDate salaryDate) {
        return queryFactory.selectFrom(salary)
                .distinct()
                .where(
                        salary.salaryDate.eq(salaryDate),
                        salary.senior.user.isDelete.isFalse()
                )
                .leftJoin(salary.senior, senior)
                .fetchJoin()
                .leftJoin(salary.senior.user, user)
                .fetchJoin()
                .fetch();
    }

    @Override
    public List<Salary> findAllBySalaryNoneAccount(LocalDate salaryDate, Senior searchSenior) {
        List<Salary> nowSalaries = queryFactory.selectFrom(salary)
                .where(
                        salary.senior.eq(searchSenior),
                        salary.salaryDate.eq(salaryDate),
                        salary.senior.user.isDelete.isFalse()
                                .or(salary.salaryDate.goe(salaryDate))
                )
                .fetch();
        if (nowSalaries.get(0).getAccount() == null) {
            return queryFactory.selectFrom(salary)
                    .distinct()
                    .where(
                            salary.senior.eq(searchSenior),
                            salary.account.isNull(),
                            salary.senior.user.isDelete.isFalse()
                    )
                    .leftJoin(salary.senior, senior)
                    .fetchJoin()
                    .fetch();
        }
        return nowSalaries;
    }

    @Override
    public boolean existIncompleteSalary(Senior senior) {
        Integer fetchFirst = queryFactory.selectOne()
                .from(salary)
                .where(salary.senior.eq(senior),
                        salary.status.isFalse(),
                        salary.totalAmount.gt(0))
                .fetchFirst();
        return fetchFirst != null;
    }
}

