package com.postgraduate.global.bizppurio.application.dto.req.content;

public record JuniorMatchingWaitingMessage(
        String message,
        String senderkey,
        String templatecode
) implements Message {
}
