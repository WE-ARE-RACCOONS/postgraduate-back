package com.postgraduate.global.bizppurio.dto.req.content;

public record JuniorRefuseMessage(
        String message,
        String senderkey,
        String templatecode
) implements Message {}
