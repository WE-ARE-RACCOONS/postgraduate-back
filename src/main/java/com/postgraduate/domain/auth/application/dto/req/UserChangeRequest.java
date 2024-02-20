package com.postgraduate.domain.auth.application.dto.req;

public record UserChangeRequest(String major,
                                String field,
                                boolean matchingReceive) {
}
