package com.postgraduate.global.bizppurio.application.dto.req;

public record JuniorMatchingFailRequest(
        String phoneNumber, String name, String originPostgraduate, String originMajor, String alterPostgraduate, String alterMajor
) {
}
