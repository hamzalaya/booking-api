package com.booking.security;

import com.booking.exceptions.BookingException;
import com.booking.exceptions.error.ApiErrorDto;
import com.booking.exceptions.error.BookingExceptionTranslator;
import com.booking.exceptions.error.ExceptionCode;
import com.booking.utils.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Data
@AllArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    private BookingExceptionTranslator bookingExceptionTranslator;


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException {
        LOGGER.debug("Handling authenticationException", authenticationException);
        printResponse(response);

    }

    public void commence(HttpServletRequest request, HttpServletResponse response, Exception exception) throws IOException {
        LOGGER.debug("Handling exception fro request {}:", request.toString(), exception);
        printResponse(response);
    }

    private void printResponse(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        ApiErrorDto apiErrorDto = bookingExceptionTranslator.toApiError(new BookingException(ExceptionCode.API_UNAUTHORIZED, "authentication.error.token.invalid"));
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().println(JsonUtils.asJsonString(apiErrorDto));
    }
}
