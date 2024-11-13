package com.postgraduate.domain.member.user.domain.repository;

import com.postgraduate.domain.member.user.domain.entity.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRoleRepository extends JpaRepository<MemberRole, Long> {
}
