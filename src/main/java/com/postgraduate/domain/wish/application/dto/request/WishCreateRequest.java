package com.postgraduate.domain.wish.application.dto.request;

public record WishCreateRequest(
    String field,
    String postgradu,
    String lab,
    String phoneNumber
) {}
