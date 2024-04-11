package com.postgraduate.global.bizppurio.dto.req.content;

public record JuniorApplyMessage(
        String message,
        String senderkey,
        String templatecode
) implements Message {}
