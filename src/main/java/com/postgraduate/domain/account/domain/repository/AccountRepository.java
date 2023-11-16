package com.postgraduate.domain.account.domain.repository;

import com.postgraduate.domain.account.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
