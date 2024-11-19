package com.postgraduate.domain.member.user.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserDslRepositoryImpl implements UserDslRepository{
    private final JPAQueryFactory queryFactory;
}
