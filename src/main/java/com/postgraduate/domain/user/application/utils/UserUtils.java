package com.postgraduate.domain.user.application.utils;

import com.postgraduate.domain.senior.domain.entity.Info;
import com.postgraduate.domain.senior.domain.entity.Senior;
import com.postgraduate.domain.user.domain.entity.User;
import com.postgraduate.domain.user.domain.entity.constant.Role;
import com.postgraduate.domain.user.exception.PhoneNumberException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserUtils {
    @Value("${profile.default}")
    private String profile;

    private User archiveUser = new User(-100L, -100L, "email", "탈퇴한 사용자", "01000000000", profile, 0, Role.USER, false, LocalDateTime.now(), LocalDateTime.now(), false);
    private Senior archiveSenior = new Senior(-100L, archiveUser, null, null, 0, new Info("탈퇴한사용자","탈퇴한사용자","탈퇴한사용자","탈퇴한사용자","탈퇴한사용자","탈퇴한사용자",false, false, "탈퇴한사용자", "탈퇴한사용자", 0), null, LocalDateTime.now(), LocalDateTime.now());

    public User getArchiveUser() {
        return archiveUser;
    }

    public Senior getArchiveSenior() {
        return archiveSenior;
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
