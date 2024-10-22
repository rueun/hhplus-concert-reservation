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

@Slf4j
public class LoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper httpServletRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper httpServletResponse = new ContentCachingResponseWrapper(response);
        filterChain.doFilter(httpServletRequest, httpServletResponse);

        // request 로깅
        String uri = httpServletRequest.getRequestURI();
        String reqContent = new String(httpServletRequest.getContentAsByteArray());
        log.info("request uri : {}, request body: {}", uri, reqContent);

        // response 로깅
        int httpStatus = httpServletResponse.getStatus();
        String resContent = new String(httpServletResponse.getContentAsByteArray());
        httpServletResponse.copyBodyToResponse();
        log.info("response status: {}, response body {}", httpStatus, resContent);
    }
}

