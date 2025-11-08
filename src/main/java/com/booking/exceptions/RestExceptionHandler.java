package com.booking.exceptions;

import com.booking.exceptions.error.ApiErrorDto;
import com.booking.exceptions.error.BookingExceptionTranslator;
import com.booking.exceptions.error.ExceptionCode;
import com.booking.utils.ApiMessageSource;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Data
public class RestExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

    private final BookingExceptionTranslator bookingExceptionTranslator;
    private final ApiMessageSource apiMessageSource;

    @ResponseBody
    @ExceptionHandler(BookingException.class)
    public ResponseEntity<ApiErrorDto> handle(BookingException exception) {
        LOGGER.debug("Exception processed by handle(BookingException exception)");
        ApiErrorDto apiErrorDto = bookingExceptionTranslator.toApiError(exception);
        switch (apiErrorDto.getExceptionCode()) {
            case API_FORBIDDEN -> {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiErrorDto);
            }
            case API_NOT_FOUND -> {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiErrorDto);
            }
            case API_BAD_REQUEST, API_VALIDATION -> {
                return ResponseEntity.badRequest().body(apiErrorDto);
            }
            case API_UNAUTHORIZED -> {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiErrorDto);
            }
            default -> {
                LOGGER.error("Api internal error server caused by", exception.getCause());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiErrorDto);
            }
        }
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDto> handle(Exception exception) {
        LOGGER.error("Exception occurred ", exception);
        BookingException bookingException = new BookingException(ExceptionCode.API_INTERNAL_SERVER_ERROR, exception, exception.getMessage());
        ApiErrorDto apiErrorDto = bookingExceptionTranslator.toApiError(bookingException);
        LOGGER.error("Api internal error server caused by", exception.getCause());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiErrorDto);

    }
}
