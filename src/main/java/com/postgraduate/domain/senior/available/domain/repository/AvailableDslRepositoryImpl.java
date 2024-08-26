package com.postgraduate.domain.senior.available.domain.repository;

import com.postgraduate.domain.senior.available.domain.entity.Available;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.postgraduate.domain.available.domain.entity.QAvailable.available;

@RequiredArgsConstructor
@Repository
public class AvailableDslRepositoryImpl implements AvailableDslRepository {
    private final JPAQueryFactory queryFactory;
    @Override
    public List<Available> findAllBySenior(Senior senior) {
        return queryFactory.selectFrom(available)
                .distinct()
                .where(available.senior.eq(senior))
                .fetch();
    }

    @Override
    public List<Available> findAllByMine(Senior senior) {
        return queryFactory.selectFrom(available)
                .distinct()
                .where(available.senior.eq(senior))
                .fetch();
    }
}
