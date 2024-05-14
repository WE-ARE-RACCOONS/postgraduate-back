package com.postgraduate.global.bizppurio.application.dto.req.content;

import com.postgraduate.global.bizppurio.application.dto.req.content.button.WebLinkButton;

public record SeniorSingUpMessage(
        String message,
        String senderkey,
        String templatecode,
        WebLinkButton[] button
) implements Message {
}