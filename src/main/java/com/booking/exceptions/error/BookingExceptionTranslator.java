package com.booking.exceptions.error;

import com.booking.exceptions.BookingException;
import com.booking.utils.ApiMessageSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Component
@Data
@AllArgsConstructor
public class BookingExceptionTranslator {
    private ApiMessageSource apiMessageSource;


    public ApiErrorDto toApiError(BookingException exception) {
        ApiErrorDto apiErrorDto = new ApiErrorDto();
        apiErrorDto.setExceptionCode(exception.getExceptionCode());
        apiErrorDto.setStatus(exception.getExceptionCode().getStatus());
        apiErrorDto.setCode(exception.getCode() != null ? exception.getCode() : exception.getExceptionCode().getCode());
        apiErrorDto.setMessage(exception.getCode() != null ? apiMessageSource.getMessage(exception.getCode()) : apiMessageSource.getMessage(exception.getExceptionCode().getCode()));
        if (exception.getErrorMessage() != null) {
            ErrorMessage errorMessage = new ErrorMessage().setCode(exception.getErrorMessage().getCode())
                    .setMessage(apiMessageSource.getMessage(exception.getErrorMessage().getCode()));
            apiErrorDto.setErrorMessage(errorMessage);
        }
        apiErrorDto.setTimestamp(LocalDateTime.now());
        apiErrorDto.setRequestId(MDC.get("requestId"));
        if (exception.getFieldErrors() != null && !exception.getFieldErrors().isEmpty()) {
            apiErrorDto.setFieldErrors(exception.getFieldErrors().stream().map(err -> err.setMessage(apiMessageSource.getMessage(err.getCode()))).collect(Collectors.toList()));
        }
        return apiErrorDto;
    }

}
