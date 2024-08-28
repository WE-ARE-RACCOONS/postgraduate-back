package com.postgraduate.domain.user.domain.repository;

import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.entity.Wish;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.postgraduate.domain.user.domain.entity.QWish.wish;

@Repository
@RequiredArgsConstructor
public class UserDslRepositoryImpl implements UserDslRepository{
    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    @Override
    public void saveJunior(User user, Wish wish) {
        entityManager.persist(user);
        entityManager.persist(wish);
    }

    @Override
    public void changeJunior(Wish wish) {
        entityManager.persist(wish);
    }

    @Override
    public void deleteWish(User user) {
        queryFactory.delete(wish)
                .where(wish.user.eq(user));
    }
}
