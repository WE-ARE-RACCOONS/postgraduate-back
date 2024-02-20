package com.postgraduate.domain.image.exception;

import com.postgraduate.global.exception.ApplicationException;

import static com.postgraduate.domain.image.presentation.constant.ImageResponseCode.IMAGE_UPLOAD_ERROR;
import static com.postgraduate.domain.image.presentation.constant.ImageResponseMessage.UPLOAD_ERROR;

public class UploadException extends ApplicationException {
    public UploadException() {
        super(UPLOAD_ERROR.getMessage(), IMAGE_UPLOAD_ERROR.getCode());
    }
}
