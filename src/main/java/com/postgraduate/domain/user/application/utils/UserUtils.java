package com.postgraduate.domain.user.application.utils;

import com.postgraduate.domain.user.exception.PhoneNumberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserUtils {
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
