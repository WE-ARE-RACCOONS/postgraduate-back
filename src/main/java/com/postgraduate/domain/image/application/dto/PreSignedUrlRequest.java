package com.postgraduate.domain.image.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PreSignedUrlRequest {
    private String fileName;
}
