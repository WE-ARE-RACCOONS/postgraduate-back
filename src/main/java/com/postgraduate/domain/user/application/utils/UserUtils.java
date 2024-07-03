package com.postgraduate.domain.user.application.utils;

import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.exception.PhoneNumberException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {
    @Value("${profile.default}")
    private String profile;

    private User archiveUser;

    @PostConstruct
    public void init() {
        archiveUser = new User(-100L, -100L, null, "알수없음", null, profile, 0, null, false, null, null, false);
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
