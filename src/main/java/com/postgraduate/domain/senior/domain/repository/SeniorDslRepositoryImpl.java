package com.postgraduate.domain.senior.domain.repository;

import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.domain.account.domain.entity.QAccount;
import com.postgraduate.domain.salary.application.dto.SeniorAndAccount;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.QUser;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.postgraduate.domain.account.domain.entity.QAccount.account;
import static com.postgraduate.domain.senior.domain.entity.QSenior.senior;
import static com.postgraduate.domain.senior.domain.entity.constant.Status.APPROVE;
import static com.postgraduate.domain.user.domain.entity.QUser.user;
import static com.querydsl.core.types.Order.ASC;
import static com.querydsl.core.types.Order.DESC;
import static com.querydsl.core.types.dsl.Expressions.FALSE;
import static com.querydsl.core.types.dsl.Expressions.TRUE;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Repository
public class SeniorDslRepositoryImpl implements SeniorDslRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Senior> findAllBySearchSenior(String search, String sort, Pageable pageable) {
        List<Senior> seniors = queryFactory.selectFrom(senior)
                .distinct()
                .leftJoin(senior.user, user)
                .fetchJoin()
                .where(
                        senior.info.totalInfo.like("%" + search + "%"),
                        senior.status.eq(APPROVE),
                        senior.user.isDelete.eq(FALSE)
                )
                .orderBy(orderSpecifier(sort))
                .orderBy(senior.user.nickName.asc()).
                offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(senior.count())
                .from(senior)
                .distinct()
                .leftJoin(senior.user, user)
                .where(
                        senior.info.totalInfo.like("%" + search + "%"),
                        senior.status.eq(APPROVE),
                        senior.user.isDelete.eq(FALSE)
                )
                .fetchOne();


        return new PageImpl<>(seniors, pageable, total);
    }

    private OrderSpecifier<?> orderSpecifier(String sort) {
        if (sort == null)
            return new OrderSpecifier<>(DESC, senior.hit);
        if (sort.equals("low"))
            return new OrderSpecifier<>(ASC, senior.hit);
        return new OrderSpecifier<>(DESC, senior.hit);
    }

    @Override
    public Page<Senior> findAllByFieldSenior(String field, String postgradu, Pageable pageable) {
        List<Senior> seniors = queryFactory.selectFrom(senior)
                .distinct()
                .leftJoin(senior.user, user)
                .fetchJoin()
                .where(
                        fieldSpecifier(field),
                        postgraduSpecifier(postgradu),
                        senior.status.eq(APPROVE),
                        senior.user.isDelete.eq(FALSE)
                )
                .orderBy(senior.hit.desc())
                .orderBy(senior.user.nickName.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(senior.count())
                .from(senior)
                .distinct()
                .leftJoin(senior.user, user)
                .where(
                        fieldSpecifier(field),
                        postgraduSpecifier(postgradu),
                        senior.status.eq(APPROVE),
                        senior.user.isDelete.eq(FALSE)
                )
                .fetchOne();

        return new PageImpl<>(seniors, pageable, total);
    }

    private BooleanExpression fieldSpecifier(String fields) {
        String[] field = fields.split(",");
        if (fields.contains("others"))
            return Arrays.stream(field)
                    .map(fieldName -> senior.info.etcField.isTrue()
                            .or(senior.info.field.like("%"+fieldName+"%")))
                    .reduce(BooleanExpression::or)
                    .orElse(FALSE);
        return Arrays.stream(field)
                .map(fieldName -> (senior.info.field.like("%"+fieldName+"%")))
                .reduce(BooleanExpression::or)
                .orElse(FALSE);
    }

    private BooleanExpression postgraduSpecifier(String postgradu) {
        if (postgradu.contains("all"))
            return TRUE;

        String[] postgradus = postgradu.split(",");

        if (postgradu.contains("others")) {
            return Arrays.stream(postgradus)
                    .map(postgraduName -> senior.info.etcPostgradu.isTrue()
                            .or(senior.info.postgradu.like("%"+postgraduName+"%")))
                    .reduce(BooleanExpression::or)
                    .orElse(FALSE);
        }

        return Arrays.stream(postgradus)
                .map(postgraduName -> senior.info.postgradu.like("%"+postgraduName+"%"))
                .reduce(BooleanExpression::or)
                .orElse(FALSE);
    }

    @Override
    public Page<Senior> findAllBySearchSeniorWithAdmin(String search, Pageable pageable) {
        List<Senior> seniors = queryFactory.selectFrom(senior)
                .where(
                        searchLike(search),
                        senior.user.isDelete.eq(FALSE)
                )
                .distinct()
                .innerJoin(senior.user, user)
                .fetchJoin()
                .orderBy(senior.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(senior.count())
                .from(senior)
                .where(
                        searchLike(search),
                        senior.user.isDelete.eq(FALSE)
                )
                .distinct()
                .innerJoin(senior.user, user)
                .fetchOne();

        return new PageImpl<>(seniors, pageable, total);
    }

    private BooleanExpression searchLike(String search) {
        if (StringUtils.hasText(search)) {
            return senior.user.phoneNumber.contains(search)
                    .or(senior.user.nickName.contains(search));
        }
        return null;
    }

    @Override
    public Optional<Senior> findBySeniorId(Long seniorId) {
        return ofNullable(queryFactory.selectFrom(senior)
                .distinct()
                .leftJoin(senior.user, user)
                .fetchJoin()
                .where(
                        senior.seniorId.eq(seniorId),
                        senior.profile.isNotNull(),
                        senior.status.eq(APPROVE)
                        ,senior.user.isDelete.isFalse()
                )
                .fetchOne());
    }

    @Override
    public List<SeniorAndAccount> findAllSeniorAndAccount() {
        List<Senior> seniors = queryFactory.selectFrom(senior)
                .where(senior.user.isDelete.isFalse())
                .fetch();
        List<Account> accounts = queryFactory.selectFrom(account)
                .where(account.senior.in(seniors))
                .fetch();

        return seniors.stream()
                .map(senior -> {
                    Account account = accounts.stream()
                            .filter(a -> a.getSenior().equals(senior))
                            .findFirst()
                            .orElse(null);
                    return new SeniorAndAccount(senior, account);
                })
                .toList();
    }
}
