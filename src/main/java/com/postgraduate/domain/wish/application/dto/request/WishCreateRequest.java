package com.postgraduate.domain.wish.application.dto.request;

public record WishCreateRequest(
    String field,
    String postgradu,
    String professor,
    String lab,
    String phoneNumber
) {}
