package com.postgraduate.global.image.exception;

import com.postgraduate.global.exception.ApplicationException;
import com.postgraduate.global.image.presentation.constant.ImageResponseCode;
import com.postgraduate.global.image.presentation.constant.ImageResponseMessage;

public class DeleteException extends ApplicationException {
    public DeleteException() {
        super(ImageResponseMessage.DELETE_ERROR.getMessage(), ImageResponseCode.IMAGE_DELETE_ERROR.getCode());
    }
}
