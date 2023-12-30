package com.postgraduate.domain.available.domain.repository;

import com.postgraduate.domain.available.domain.entity.Available;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.postgraduate.domain.available.domain.entity.QAvailable.available;
import static com.postgraduate.domain.senior.domain.entity.QSenior.senior;

@RequiredArgsConstructor
@Repository
public class AvailableDslRepositoryImpl implements AvailableDslRepository {
    private final JPAQueryFactory queryFactory;
    @Override
    public List<Available> findAllBySenior(Senior inputSenior) {
        return queryFactory.selectFrom(available)
                .distinct()
                .leftJoin(available.senior, senior)
                .fetchJoin()
                .where(available.senior.eq(inputSenior))
                .fetch();
    }
}
