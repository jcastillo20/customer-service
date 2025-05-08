package com.example.customer_service.infra.handle.exception;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        Map<String, Object> errorBody = new LinkedHashMap<>();
        errorBody.put("path", request.getRequestURI());
        errorBody.put("error", "Unauthorized");
        errorBody.put("message", authException.getMessage());
        errorBody.put("timestamp", Instant.now().toString());
        errorBody.put("status", HttpStatus.UNAUTHORIZED.value());

        objectMapper.writeValue(response.getOutputStream(), errorBody);
    }
}
