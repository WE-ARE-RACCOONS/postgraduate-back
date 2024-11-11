package com.postgraduate.domain.member.user.application.utils;

import com.postgraduate.domain.member.user.domain.entity.User;
import com.postgraduate.domain.member.user.exception.PhoneNumberException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserUtils {
    @Value("${profile.senior.default1}")
    private String profile;
    private User archiveUser;

    @PostConstruct
    public void init() {
        archiveUser = new User(-100L, -100L, null, "알수없음", "탈퇴한회원", profile, 0, null, false, null, null, false, false);
    }

    public User getArchiveUser() {
        return archiveUser;
    }

    public void checkPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() != 11)
            throw new PhoneNumberException();
        try {
            Long.parseLong(phoneNumber);
        } catch (Exception e) {
            throw new PhoneNumberException();
        }
    }
}
