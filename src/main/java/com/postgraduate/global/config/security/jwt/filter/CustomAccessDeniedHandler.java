package com.postgraduate.global.config.security.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postgraduate.global.dto.ErrorResponse;
import com.postgraduate.global.logging.dto.LogRequest;
import com.postgraduate.global.logging.service.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.postgraduate.domain.auth.presentation.constant.AuthResponseCode.AUTH_DENIED;
import static com.postgraduate.domain.auth.presentation.constant.AuthResponseMessage.PERMISSION_DENIED;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;
    private final LogService logService;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        logService.save(new LogRequest(CustomAccessDeniedHandler.class.getSimpleName(), PERMISSION_DENIED.getMessage()));
        objectMapper.writeValue(
                response.getOutputStream(),
                new ErrorResponse(AUTH_DENIED.getCode(), PERMISSION_DENIED.getMessage())
        );
    }
}
