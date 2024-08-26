package com.postgraduate.global.auth.login.application.dto.req;

public record UserChangeRequest(String major,
                                String field,
                                boolean matchingReceive) {
}
