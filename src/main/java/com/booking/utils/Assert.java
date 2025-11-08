package com.booking.utils;


import com.booking.exceptions.BookingException;
import com.booking.exceptions.error.ExceptionCode;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Objects;

public interface Assert {

    static void assertNotNull(Object item, ExceptionCode exceptionCode) throws BookingException {
        if (Objects.isNull(item)) {
            throw new BookingException(exceptionCode);
        }
    }

    static void assertNotNull(Object item, ExceptionCode exceptionCode, String key) throws BookingException {
        if (Objects.isNull(item)) {
            throw new BookingException(exceptionCode, key);
        }
    }

    static void assertTrue(Boolean item, ExceptionCode exceptionCode) throws BookingException {
        if (!Boolean.TRUE.equals(item)) {
            throw new BookingException(exceptionCode);
        }
    }

    static void assertTrue(Boolean item, ExceptionCode exceptionCode, String key) throws BookingException {
        if (!Boolean.TRUE.equals(item)) {
            throw new BookingException(exceptionCode, key);
        }
    }

    static void assertNotEmpty(List<?> list, ExceptionCode exceptionCode, String key) throws BookingException {
        if (CollectionUtils.isEmpty(list)) {
            throw new BookingException(exceptionCode, key);
        }
    }
}
