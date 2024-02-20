package com.postgraduate.domain.image.application.dto.req;

import jakarta.validation.constraints.NotBlank;

public record PreSignedUrlRequest(@NotBlank String fileName) {
}
