package com.postgraduate.global.bizppurio.dto.req.content;

public record JuniorAcceptMessage(
        String message,
        String senderkey,
        String templatecode
) implements Message {}
