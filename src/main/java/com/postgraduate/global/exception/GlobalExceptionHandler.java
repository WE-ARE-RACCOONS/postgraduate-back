package com.postgraduate.global.exception;

import com.postgraduate.global.dto.ErrorResponse;
import com.postgraduate.global.dto.ResponseDto;
import com.postgraduate.global.constant.ErrorCode;
import com.postgraduate.global.aop.logging.dto.LogRequest;
import com.postgraduate.global.aop.logging.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.postgraduate.global.dto.ResponseDto.create;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final LogService logService;

    @Value("${log.Type}")
    private String env;
    private static final String LOG_FORMAT = "Code : {}, Message : {}";

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ResponseDto<ErrorResponse>> handleApplicationException(ApplicationException ex) {
        return ResponseEntity.ok(create(ex.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<ErrorResponse>> handleArgumentValidException(MethodArgumentNotValidException ex) {
        logService.save(new LogRequest(env, "@Valid", ex.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
        log.error(LOG_FORMAT, "@Valid Error", "MethodArgumentNotValidException");
        return ResponseEntity.ok(create(ErrorCode.VALID_BLANK.getCode(), ex.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<ErrorResponse>> handleInternalServerException(Exception ex) {
        log.error(LOG_FORMAT, "500", ex.getStackTrace());
        log.error("errorMessage : {}", ex.getMessage());
        return ResponseEntity.internalServerError().build();
    }
}
