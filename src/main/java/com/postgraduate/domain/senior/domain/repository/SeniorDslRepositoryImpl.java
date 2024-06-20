package com.postgraduate.domain.senior.domain.repository;

import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.postgraduate.domain.senior.domain.entity.QSenior.senior;
import static com.postgraduate.domain.user.domain.entity.QUser.user;
import static com.querydsl.core.types.Order.ASC;
import static com.querydsl.core.types.Order.DESC;
import static com.querydsl.core.types.dsl.Expressions.FALSE;
import static com.querydsl.core.types.dsl.Expressions.TRUE;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Repository
@Slf4j
public class SeniorDslRepositoryImpl implements SeniorDslRepository{
    private final JPAQueryFactory queryFactory;
    private static final String ALL = "all";
    private static final String ETC = "others";

    @Override
    public Page<Senior> findAllBySearchSenior(String search, String sort, Pageable pageable) {
        List<Tuple> results = queryFactory.select(senior.seniorId, senior.user.nickName, senior.hit)
                .from(senior)
                .distinct()
                .leftJoin(senior.user, user)
                .where(
                        senior.info.totalInfo.like("%" + search + "%")
                                .or(senior.user.nickName.like("%" + search + "%")),
                        senior.user.isDelete.eq(FALSE)
                )
                .orderBy(orderSpecifier(sort))
                .orderBy(senior.user.nickName.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        if (CollectionUtils.isEmpty(results)) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        List<Senior> seniors = queryFactory.selectFrom(senior)
                .where(senior.seniorId.in(results.stream()
                        .map(tuple -> tuple.get(senior.seniorId))
                        .toList()))
                .leftJoin(senior.user, user)
                .fetchJoin()
                .orderBy(orderSpecifier(sort))
                .orderBy(senior.user.nickName.asc())
                .fetch();


        Long total = queryFactory.select(senior.count())
                .from(senior)
                .distinct()
                .leftJoin(senior.user, user)
                .where(
                        senior.info.totalInfo.like("%" + search + "%")
                                .or(senior.user.nickName.like("%" + search + "%")),
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
        List<Tuple> results = queryFactory.select(senior.seniorId, senior.user.nickName, senior.hit)
                .from(senior)
                .distinct()
                .leftJoin(senior.user, user)
                .where(
                        fieldSpecifier(field),
                        postgraduSpecifier(postgradu),
                        senior.user.isDelete.eq(FALSE)
                )
                .orderBy(senior.hit.desc())
                .orderBy(senior.user.nickName.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        if (CollectionUtils.isEmpty(results)) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        List<Senior> seniors = queryFactory.selectFrom(senior)
                .where(senior.seniorId.in(results.stream()
                        .map(tuple -> tuple.get(senior.seniorId))
                        .toList()))
                .leftJoin(senior.user, user)
                .fetchJoin()
                .orderBy(senior.hit.desc())
                .orderBy(senior.user.nickName.asc())
                .fetch();

        Long total = queryFactory.select(senior.count())
                .from(senior)
                .distinct()
                .leftJoin(senior.user, user)
                .where(
                        fieldSpecifier(field),
                        postgraduSpecifier(postgradu),
                        senior.user.isDelete.eq(FALSE)
                )
                .fetchOne();

        return new PageImpl<>(seniors, pageable, total);
    }

    private BooleanExpression fieldSpecifier(String fields) {
        if (fields.contains(ALL))
            return TRUE;

        String[] field = fields.split(",");
        if (fields.contains(ETC))
            return Arrays.stream(field)
                    .map(fieldName -> senior.info.etcField.isTrue())
                    .reduce(BooleanExpression::or)
                    .orElse(FALSE);
        return Arrays.stream(field)
                .map(fieldName -> (senior.info.field.like("%"+fieldName+"%")))
                .reduce(BooleanExpression::or)
                .orElse(FALSE);
    }

    private BooleanExpression postgraduSpecifier(String postgradu) {
        if (postgradu.contains(ALL))
            return TRUE;

        String[] postgradus = postgradu.split(",");

        if (postgradu.contains(ETC)) {
            return Arrays.stream(postgradus)
                    .map(postgraduName -> senior.info.etcPostgradu.isTrue())
                    .reduce(BooleanExpression::or)
                    .orElse(FALSE);
        }

        return Arrays.stream(postgradus)
                .map(postgraduName -> senior.info.postgradu.like("%"+postgraduName+"%"))
                .reduce(BooleanExpression::or)
                .orElse(FALSE);
    }

    @Override
    public Optional<Senior> findByUserWithAll(User seniorUser) {
        return ofNullable(queryFactory.selectFrom(senior)
                .distinct()
                .join(senior.user, user)
                .fetchJoin()
                .where(senior.user.eq(seniorUser))
                .fetchOne()
        );
    }

    @Override
    public List<Senior> findAllSenior() {
        return queryFactory.selectFrom(senior)
                .distinct()
                .leftJoin(senior.user, user)
                .fetchJoin()
                .where(senior.user.isDelete.eq(FALSE))
                .orderBy(senior.createdAt.desc())
                .fetch();
    }

    @Override
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
