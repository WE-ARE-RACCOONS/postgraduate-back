package com.postgraduate.global.bizppurio.dto.req.content;

public record SeniorAcceptMessage(
        String message,
        String senderkey,
        String templatecode
) implements Message {}
