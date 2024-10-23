package com.hhplus.concertreservation.support.api.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
public class LoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper httpServletRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper httpServletResponse = new ContentCachingResponseWrapper(response);

        // request 로깅
        String uri = httpServletRequest.getRequestURI();
        String reqContent = new String(httpServletRequest.getContentAsByteArray());
        log.info("request uri : {}, request body: {}", uri, reqContent);

        // 다음 필터로 요청을 전달
        filterChain.doFilter(httpServletRequest, httpServletResponse);

        // response 로깅
        int httpStatus = httpServletResponse.getStatus();
        String resContent = new String(httpServletResponse.getContentAsByteArray());
        httpServletResponse.copyBodyToResponse();
        log.info("response status: {}, response body {}", httpStatus, resContent);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludePath = {"/swagger-ui", "/v3/api-docs"};
        String path = request.getRequestURI();
        return Arrays.stream(excludePath).anyMatch(path::startsWith);
    }
}

