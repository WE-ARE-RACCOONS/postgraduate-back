package com.postgraduate.domain.refuse.domain.repository;

import com.postgraduate.domain.refuse.domain.entity.Refuse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefuseRepository extends JpaRepository<Refuse, Long> {
}
