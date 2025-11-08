package com.booking.validation;

import com.booking.exceptions.BookingException;
import com.booking.utils.ApplicationContextProvider;

import java.time.LocalDate;

import static com.booking.exceptions.error.ExceptionCode.API_VALIDATION;

public class DateValidation {
    private static Integer MAX_ALLOWED_PERIOD_BY_YEARS;

    private DateValidation() {

    }

    public static void validateDates(LocalDate start, LocalDate end, String codeIfNotValid) {
        if (MAX_ALLOWED_PERIOD_BY_YEARS == null) {
            MAX_ALLOWED_PERIOD_BY_YEARS = Integer.valueOf(ApplicationContextProvider.getProperty("max.allowed.period"));
        }
        LocalDate now = LocalDate.now();
        if (now.isAfter(start) || start.isAfter(end) || end.isAfter(now.plusYears(MAX_ALLOWED_PERIOD_BY_YEARS))) {
            throw new BookingException(API_VALIDATION, codeIfNotValid);
        }
    }
}
