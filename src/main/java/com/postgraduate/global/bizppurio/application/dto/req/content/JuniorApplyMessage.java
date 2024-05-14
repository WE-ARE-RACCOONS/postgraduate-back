package com.postgraduate.global.bizppurio.application.dto.req.content;

public record JuniorApplyMessage(
        String message,
        String senderkey,
        String templatecode
) implements Message {}
