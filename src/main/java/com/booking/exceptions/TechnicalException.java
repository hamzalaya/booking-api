package com.booking.exceptions;

public class TechnicalException extends RuntimeException {

    public TechnicalException(Throwable cause) {
        super(cause);
    }

    public TechnicalException(String s) {
        super(s);
    }
}
