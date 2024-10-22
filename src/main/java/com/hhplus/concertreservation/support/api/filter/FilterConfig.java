package com.hhplus.concertreservation.support.api.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<LoggingFilter> loggingFilter() {
        FilterRegistrationBean<LoggingFilter> regBean = new FilterRegistrationBean<>();
        regBean.setFilter(new LoggingFilter());
        regBean.addUrlPatterns("/*");
        return regBean;
    }
}