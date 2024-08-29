package com.postgraduate.domain.member.senior.domain.repository;

import com.postgraduate.domain.member.senior.domain.entity.Senior;
import com.postgraduate.domain.member.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeniorRepository extends JpaRepository<Senior, Long>, SeniorDslRepository {
    Optional<Senior> findByUserAndUser_IsDeleteIsFalse(User user);
    Optional<Senior> findByUser_NickNameAndUser_IsDelete(String nickName, Boolean isDelete);
    List<Senior> findAllByUser_IsDelete(Boolean isDelete);
    Optional<Senior> findByUserAndUser_IsDelete(User user, Boolean isDelete);
}
