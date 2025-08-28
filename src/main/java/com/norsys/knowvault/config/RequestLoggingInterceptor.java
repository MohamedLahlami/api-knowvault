package com.norsys.knowvault.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class RequestLoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);

        String userAgent = request.getHeader("User-Agent");
        String clientIp = getClientIpAddress(request);

        log.info("HTTP Request: {} {} from IP: {} - User-Agent: {}",
                request.getMethod(), request.getRequestURI(), clientIp, userAgent);

        // Log request parameters if present
        if (!request.getParameterMap().isEmpty()) {
            log.debug("Request parameters: {}", request.getParameterMap());
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                               Object handler, Exception ex) {
        Long startTime = (Long) request.getAttribute("startTime");
        long duration = System.currentTimeMillis() - startTime;

        String logLevel = response.getStatus() >= 400 ? "ERROR" : "INFO";

        if (response.getStatus() >= 400) {
            log.error("HTTP Response: {} {} - Status: {} - Duration: {}ms",
                    request.getMethod(), request.getRequestURI(), response.getStatus(), duration);
        } else {
            log.info("HTTP Response: {} {} - Status: {} - Duration: {}ms",
                    request.getMethod(), request.getRequestURI(), response.getStatus(), duration);
        }

        if (ex != null) {
            log.error("Request resulted in exception: {}", ex.getMessage(), ex);
        }
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }
}
