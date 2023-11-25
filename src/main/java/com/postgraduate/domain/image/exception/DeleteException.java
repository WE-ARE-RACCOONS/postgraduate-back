package com.postgraduate.domain.image.exception;

import static com.postgraduate.domain.image.presentation.constant.ImageResponseCode.IMAGE_DELETE_ERROR;
import static com.postgraduate.domain.image.presentation.constant.ImageResponseMessage.DELETE_ERROR;

public class DeleteException extends ImageException{
    public DeleteException() {
        super(DELETE_ERROR.getMessage(), IMAGE_DELETE_ERROR.getCode());
    }
}
