package com.postgraduate.domain.auth.application.dto.req;

import jakarta.validation.constraints.NotNull;

public record UserChangeRequest(String major,
                                String field,
                                @NotNull
                                Boolean matchingReceive) {
}
