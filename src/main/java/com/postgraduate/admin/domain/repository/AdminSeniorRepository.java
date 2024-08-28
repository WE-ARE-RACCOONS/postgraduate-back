package com.postgraduate.admin.domain.repository;

import com.postgraduate.domain.salary.domain.entity.Salary;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.postgraduate.domain.salary.domain.entity.QSalary.salary;
import static com.postgraduate.domain.senior.domain.entity.QSenior.senior;
import static com.postgraduate.domain.user.domain.entity.QUser.user;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Repository
public class AdminSeniorRepository {
    private final JPAQueryFactory queryFactory;

    public List<Salary> allSeniorInfo(LocalDate salaryDate) {
        return queryFactory.selectFrom(salary)
                .distinct()
                .join(salary.senior, senior)
                .fetchJoin()
                .join(salary.senior.user, user)
                .fetchJoin()
                .where(salary.senior.user.isDelete.isFalse(), salary.salaryDate.eq(salaryDate))
                .orderBy(salary.senior.createdAt.desc())
                .fetch();
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
