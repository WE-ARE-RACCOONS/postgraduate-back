package com.postgraduate.global.config.security.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postgraduate.global.dto.ErrorResponse;
import com.postgraduate.global.logging.dto.LogRequest;
import com.postgraduate.global.logging.service.LogService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.postgraduate.domain.auth.presentation.constant.AuthResponseCode.AUTH_FAILED;
import static com.postgraduate.domain.auth.presentation.constant.AuthResponseMessage.FAILED_AUTH;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Value("${log.Type}")
    private String env;

    private final ObjectMapper objectMapper;
    private final LogService logService;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        logService.save(new LogRequest(env, CustomAuthenticationEntryPoint.class.getSimpleName(), FAILED_AUTH.getMessage()));
        objectMapper.writeValue(
                response.getOutputStream(),
                new ErrorResponse(AUTH_FAILED.getCode(), FAILED_AUTH.getMessage())
        );
    }
}
