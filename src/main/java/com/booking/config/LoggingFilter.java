package com.booking.config;

import com.booking.utils.UserContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
public class LoggingFilter extends GenericFilterBean {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        MDC.put("username", UserContext.userName());
        MDC.put("ip", request.getRemoteHost());

        MDC.put("requestId", RandomStringUtils.secure().nextAlphanumeric(6));
        MDC.put("language", UserContext.getLanguage());
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove("username");
            MDC.remove("ip");
            MDC.remove("requestId");
            MDC.remove("language");
        }
    }

}
