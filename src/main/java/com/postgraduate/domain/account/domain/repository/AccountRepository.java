package com.postgraduate.domain.account.domain.repository;

import com.postgraduate.domain.account.domain.entity.Account;
import com.postgraduate.domain.senior.domain.entity.Senior;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findBySenior(Senior senior);
}
