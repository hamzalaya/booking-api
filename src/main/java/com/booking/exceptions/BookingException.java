package com.booking.exceptions;

import com.booking.exceptions.error.ErrorMessage;
import com.booking.exceptions.error.ExceptionCode;
import com.booking.exceptions.error.FieldError;

import java.util.List;
import java.util.function.Supplier;

public class BookingException extends RuntimeException {
    private ExceptionCode exceptionCode;
    private String code;
    private String internalMessage;
    private List<FieldError> fieldErrors;
    private ErrorMessage errorMessage;


    public static Supplier<BookingException> of(ExceptionCode ec, String key) {
        return () -> new BookingException(ec, key);
    }

    public static Supplier<BookingException> of(ExceptionCode ec) {
        return () -> new BookingException(ec);
    }

    public BookingException(ExceptionCode exceptionCode) {
        this(exceptionCode, exceptionCode.getCode(), null, null, null);
    }

    public BookingException(ExceptionCode exceptionCode, String code) {
        this(exceptionCode, code, null, null, null);
    }


    public BookingException(ExceptionCode exceptionCode, Throwable cause, String message) {
        this(exceptionCode, exceptionCode.getCode(), cause, message, message);
    }


    public BookingException(ExceptionCode exceptionCode, String code, String message) {
        this(exceptionCode, code, null, message, message);
    }

    public BookingException(ExceptionCode exceptionCode, String code, Throwable cause) {
        this(exceptionCode, code, cause, cause.getMessage(), null);
    }

    public BookingException(ExceptionCode exceptionCode, String code, Throwable cause, String message) {
        this(exceptionCode, code, cause, message, null);
    }

    public BookingException(ExceptionCode exceptionCode, String code, Throwable cause, String message, String internalMessage) {
        super(message, cause);
        this.exceptionCode = exceptionCode;
        this.code = code;
        this.internalMessage = internalMessage;
    }

    public static BookingException wrap(Throwable exception) {
        return wrap(exception, ExceptionCode.API_INTERNAL_SERVER_ERROR);
    }

    public static BookingException wrap(Throwable exception, ExceptionCode exceptionCode) {
        if (exception instanceof BookingException) {
            return (BookingException) exception;
        }
        return new BookingException(exceptionCode, exception.getCause(), exception.getMessage());
    }

    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }

    public BookingException setExceptionCode(ExceptionCode exceptionCode) {
        this.exceptionCode = exceptionCode;
        return this;
    }

    public String getCode() {
        return code;
    }

    public BookingException setCode(String code) {
        this.code = code;
        return this;
    }

    public String getInternalMessage() {
        return internalMessage;
    }

    public BookingException setInternalMessage(String internalMessage) {
        this.internalMessage = internalMessage;
        return this;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    public BookingException setFieldErrors(List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
        return this;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public BookingException setErrorMessage(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }
}
