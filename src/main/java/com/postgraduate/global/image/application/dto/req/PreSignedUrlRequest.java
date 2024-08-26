package com.postgraduate.global.image.application.dto.req;

import jakarta.validation.constraints.NotBlank;

public record PreSignedUrlRequest(@NotBlank String fileName) {
}
