package com.postgraduate.domain.member.user.domain.service;

import com.postgraduate.domain.member.user.domain.entity.MemberRole;
import com.postgraduate.domain.member.user.domain.repository.MemberRoleRepository;
import com.postgraduate.domain.member.user.domain.repository.UserRepository;
import com.postgraduate.domain.member.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSaveService {
    private final UserRepository userRepository;
    private final MemberRoleRepository memberRoleRepository;

    public void save(User user, MemberRole memberRole) {
        userRepository.save(user);
        user.updateRole(memberRole);
        memberRoleRepository.save(memberRole);
    }
}
