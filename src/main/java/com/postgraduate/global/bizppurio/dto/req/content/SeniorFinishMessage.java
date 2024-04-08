package com.postgraduate.global.bizppurio.dto.req.content;

public record SeniorFinishMessage(
        String message,
        String senderkey,
        String templatecode
) implements Message {}
