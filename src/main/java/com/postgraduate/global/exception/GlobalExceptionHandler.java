package com.postgraduate.global.exception;

import com.postgraduate.global.dto.ErrorResponse;
import com.postgraduate.global.dto.ResponseDto;
import com.postgraduate.global.exception.constant.ErrorCode;
import com.postgraduate.global.exception.constant.ErrorMessage;
import com.postgraduate.global.logging.dto.LogRequest;
import com.postgraduate.global.logging.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final LogService logService;
    private static final String LOG_FORMAT = "Class : {}, Code : {}, Message : {}";
    @ExceptionHandler(ApplicationException.class)
    public ResponseDto<ErrorResponse> handleApplicationException(ApplicationException ex) {
        log.error(LOG_FORMAT, ex.getClass().getSimpleName(), ex.getErrorCode(), ex.getMessage());
        return ResponseDto.create(ex.getErrorCode(), ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseDto<ErrorResponse> handleArgumentValidException() throws IOException {
        logService.save(new LogRequest("@Valid", ErrorMessage.VALID_BLANK.getMessage()));
        return ResponseDto.create(ErrorCode.VALID_BLANK.getCode(), ErrorMessage.VALID_BLANK.getMessage());
    }
}
