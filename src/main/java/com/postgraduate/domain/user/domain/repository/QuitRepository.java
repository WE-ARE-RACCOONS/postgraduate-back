package com.postgraduate.domain.user.domain.repository;

import com.postgraduate.domain.user.domain.entity.Quit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuitRepository extends JpaRepository<Quit, Long> {
}
