package com.postgraduate.global.auth.quit.domain.repository;

import com.postgraduate.global.auth.quit.domain.entity.Quit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuitRepository extends JpaRepository<Quit, Long> {
}
