package com.postgraduate.global.image.exception;

import com.postgraduate.global.exception.ApplicationException;

import static com.postgraduate.global.image.presentation.constant.ImageResponseCode.IMAGE_EMPTY;
import static com.postgraduate.global.image.presentation.constant.ImageResponseMessage.EMPTY_IMAGE;

public class EmptyFileException extends ApplicationException {
    public EmptyFileException() {
        super(EMPTY_IMAGE.getMessage(), IMAGE_EMPTY.getCode());
    }
}
