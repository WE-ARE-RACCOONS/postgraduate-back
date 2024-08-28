package com.postgraduate.domain.user.domain.repository;

import com.postgraduate.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserDslRepository {
    List<User> findAllByIsDelete(boolean isDelete);
    Optional<User> findBySocialId(Long socialId);
    Optional<User> findByNickName(String nickName);
}
