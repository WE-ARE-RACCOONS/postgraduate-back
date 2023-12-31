package com.postgraduate.domain.available.domain.repository;

import com.postgraduate.domain.available.domain.entity.Available;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.senior.domain.entity.constant.Status;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.postgraduate.domain.available.domain.entity.QAvailable.available;
import static com.postgraduate.domain.senior.domain.entity.QSenior.senior;
import static com.postgraduate.domain.senior.domain.entity.constant.Status.APPROVE;

@RequiredArgsConstructor
@Repository
public class AvailableDslRepositoryImpl implements AvailableDslRepository {
    private final JPAQueryFactory queryFactory;
    @Override
    public List<Available> findAllBySenior(Long seniorId) {
        return queryFactory.selectFrom(available)
                .distinct()
                .where(available.senior.seniorId.eq(seniorId), available.senior.status.eq(APPROVE))
                .fetch();
    }
}
